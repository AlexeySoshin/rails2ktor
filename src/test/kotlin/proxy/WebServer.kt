package proxy
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.client.HttpClient
import io.ktor.client.request.request
import io.ktor.client.statement.HttpStatement
import io.ktor.client.statement.readText
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test


class ProxyTest {

    @Test
    fun `proxies get calls`() {
        runBlocking {
            val proxyServer = embeddedServer(Netty, 28080, module = Application::proxyModule)
            val server = embeddedServer(Netty, 28081, module = Application::mainModule)
            try {
                proxyServer.start()
                server.start()

                val client = HttpClient()

                val response = client.request<HttpStatement>("http://localhost:28080/articles/123/edit").execute()

                assertEquals("123", response.readText())
            }
            finally {
                proxyServer.stop(10, 100)
                server.stop(10, 100)
            }
        }
    }
}

fun Application.mainModule() {
    routing {
        get("/articles/{id}/edit") {
            call.respond(HttpStatusCode.Accepted, call.parameters["id"] ?: "")
        }
    }
}

fun Application.proxyModule() {

    val target = "http://localhost:28081"
    routing {
        get("/articles/{id}/edit") {
            call.proxy(target)
        }
    }
}
