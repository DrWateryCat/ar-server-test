import com.google.gson.JsonObject
import database.Textures
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.gson
import io.ktor.html.respondHtml
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.pipeline.PipelineContext
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.*
import kotlinx.html.*
import java.text.DateFormat

fun Application.main() {
    install(DefaultHeaders)
    install(CallLogging)
    install(ContentNegotiation) {
        gson {
            setDateFormat(DateFormat.LONG)
            setPrettyPrinting()
        }
    }
    routing {
        get("/api/textures/{id}") {
            errorAware {
                val id = call.parameters["id"] ?: throw IllegalArgumentException("You need id in your call.")
                call.respond(Textures.get(id.toInt()))
            }
        }

        get("/api/textures") {
            errorAware {
                call.respond(Textures.getAll())
            }
        }
    }
}

private suspend fun <R> PipelineContext<*, ApplicationCall>.errorAware(block: suspend () -> R): R? {
    return try {
        block()
    } catch (e: Exception) {
        val o = JsonObject()
        o.addProperty("error", e.toString())
        call.respondText(o.toString(), ContentType.parse("application/json"), HttpStatusCode.InternalServerError)
        null
    }
}