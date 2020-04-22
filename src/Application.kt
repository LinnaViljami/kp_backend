package com.kp_backend

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.http.*
import com.fasterxml.jackson.databind.*
import com.kp_backend.repository.UserController
import io.ktor.jackson.*
import io.ktor.features.*
import kotlinx.coroutines.time.delay
import java.time.Duration

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }
    DatabaseFactory.init()
    val userController = UserController()
    intercept(ApplicationCallPipeline.Features) {
        delay(Duration.ofSeconds(1L))
    }
    routing {
        get("/") {
            call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
        }


        get("/users") {

            call.respond(userController.getAll());
        }
    }
}


/*fun Application.module(testing: Boolean = false) {
    initDB()
    install(ContentNegotiation) { ... }

    routing { ... }
}

fun initDB() {
    val config = HikariConfig("/hikari.properties")
    config.schema = <dbSchema>
    val ds = HikariDataSource(config)
    Database.connect(ds)
}
*/

