package com.kp_backend

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.http.*
import com.fasterxml.jackson.databind.*
import com.kp_backend.Auth.SimpleJWT
import com.kp_backend.models.LoginRegister
import com.kp_backend.repository.UserController
import io.ktor.auth.Authentication
import io.ktor.auth.UserIdPrincipal
import io.ktor.auth.authenticate
import io.ktor.auth.basic
import io.ktor.auth.jwt.jwt
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

    val simpleJwt = SimpleJWT(System.getenv("SECRET_KEY"))


    install(Authentication) {
        jwt {
            verifier(simpleJwt.verifier)
            validate {
                UserIdPrincipal(it.payload.getClaim("name").asString())
            }
        }




    }
    install(CORS) {
        method(HttpMethod.Options)
        method(HttpMethod.Get)
        method(HttpMethod.Post)
        method(HttpMethod.Put)
        method(HttpMethod.Delete)
        method(HttpMethod.Patch)
        header(HttpHeaders.Authorization)
        allowCredentials = true
        anyHost()
    }


    routing {
        get("/") {
            call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
        }

        route("/users") {
            get {
                call.respond(userController.getAll())
            }
            post {
                call.respond(userController.getAll())
            }
        }
        route("/login-register") {
            post {

                val post: LoginRegister = call.receive<LoginRegister>()
                val loginRegister: LoginRegister = LoginRegister(post.name, post.password)
                val user = userController.getLoginData(post)
                if(null != user){
                    call.respond(mapOf("token" to simpleJwt.sign(user.email)))
                }
                else {
                    println("redirect")
                    call.respondRedirect("/users", permanent = true)
                }
            }
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

