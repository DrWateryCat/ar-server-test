package database

import objects.Texture
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.CopyOnWriteArraySet
import java.util.concurrent.atomic.AtomicInteger
import kotlin.collections.ArrayList

object Textures {
    var textures = ArrayList<Texture>()
    var index = 0

    init {
        textures = ArrayList(1)
        readAll(File("C:/Textures"))
    }

    fun getAll() = textures.toList()

    fun get(id: Int): Texture {
        return textures.find { it.id.toString() == id.toString() } ?:
                throw IllegalArgumentException("No texture found for that ID!")
    }

    private fun readAll(directory: File) {
        directory.listFiles().forEach {
            if (it.isFile) {
                println("File: " + it.absolutePath)
                val fileInput = FileInputStream(it)
                val data = fileInput.bufferedReader(Charsets.UTF_8).use { it.readText() }
                val encoded = Base64.getEncoder().encodeToString(data.toByteArray())
                val tex = Texture(index, encoded)
                index++

                textures.add(tex)
            }
        }
    }
}