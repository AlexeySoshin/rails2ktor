package proxy

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.client.HttpClient
import io.ktor.client.request.request
import io.ktor.client.statement.HttpStatement
import io.ktor.client.statement.readText
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.request.receiveChannel
import io.ktor.request.receiveText
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProxyTest {

    val proxyServer = embeddedServer(Netty, 28080, module = Application::proxyModule)
    val server = embeddedServer(Netty, 28081, module = Application::mainModule)

    @BeforeAll
    fun start() {
        proxyServer.start()
        server.start()
    }

    @AfterAll
    fun stop() {
        proxyServer.stop(10, 100)
        server.stop(10, 100)
    }

    @Test
    fun `proxies get calls`() {
        runBlocking {
            val client = HttpClient()

            val response = client.request<HttpStatement>("http://localhost:28080/articles/123/edit").execute()

            assertEquals("123", response.readText())
        }
    }

    @Test
    fun `proxies post calls`() {
        runBlocking {
            val client = HttpClient()

            val response = client.request<HttpStatement>("http://localhost:28080/articles/123") {
                this.method = HttpMethod.Post
                this.body = "ABC"
            }.execute()

            assertEquals("ABC", response.readText())
        }
    }
}

fun Application.mainModule() {
    routing {
        get("/articles/{id}/edit") {
            call.respond(HttpStatusCode.Accepted, call.parameters["id"] ?: "")
        }
        post("/articles/{id}") {
            call.respond(call.receiveText())
        }
    }
}

fun Application.proxyModule() {

    val target = "http://localhost:28081"
    routing {
        get("/articles/{id}/edit") {
            call.proxy(target)
        }
        post("/articles/{id}") {
            call.proxy(target)
        }
    }
}
