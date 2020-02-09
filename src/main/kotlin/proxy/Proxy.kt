package proxy

import io.ktor.application.ApplicationCall
import io.ktor.client.HttpClient
import io.ktor.client.request.request
import io.ktor.client.statement.HttpStatement
import io.ktor.client.statement.readBytes
import io.ktor.request.httpMethod
import io.ktor.request.receiveChannel
import io.ktor.request.uri
import io.ktor.response.respond
import io.ktor.util.filter
import io.ktor.util.toByteArray


private val client = HttpClient()

private val readOnlyHeaders = listOf("Content-Length", "Content-Type")

suspend fun ApplicationCall.proxy(target: String) {
    val call = this
    val response = client.request<HttpStatement>("$target/${call.request.uri}") {
        this.method = call.request.httpMethod
        this.body = call.receiveChannel().toByteArray()
        call.request.headers.filter { key, _ -> !readOnlyHeaders.contains(key) }
                .forEach { key, values ->
            this.headers[key] = values.first()
        }
    }.execute()

    call.respond(response.status, response.readBytes())
}