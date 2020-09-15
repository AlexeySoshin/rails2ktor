package proxy

import io.ktor.application.ApplicationCall
import io.ktor.client.HttpClient
import io.ktor.client.request.request
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.HttpStatement
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.content.OutgoingContent
import io.ktor.request.ApplicationRequest
import io.ktor.request.httpMethod
import io.ktor.request.receiveText
import io.ktor.request.uri
import io.ktor.response.respond
import io.ktor.util.filter
import io.ktor.utils.io.ByteWriteChannel
import io.ktor.utils.io.copyAndClose


suspend fun ApplicationCall.proxy(target: String) {
    val url = "$target/${request.uri}"
    val response = request.proxy(url)

    proxyResponse(response)
}

suspend fun ApplicationRequest.proxy(url: String): HttpResponse {
    return client.request<HttpStatement>(url) {
        method = call.request.httpMethod
        body = call.receiveText()

        val originalHeaders = call.request.headers

        // There must be a content type for us to proxy the request
        // If there was no content type set, use Any
        val contentType = originalHeaders[HttpHeaders.ContentType]?.let { ContentType.parse(it) }
                ?: ContentType.Any

        // Some headers should not be set by the proxy, since they are intended for internal use only
        val safeHeaders = if (contentType.match(ContentType.Text.Plain)) {
            originalHeaders.filter { key, _ -> !HttpHeaders.isUnsafe(key) }
        } else {
            originalHeaders.filter { key, _ -> key != HttpHeaders.ContentLength }
        }

        this.headers.appendAll(safeHeaders)
    }.execute()
}

suspend fun ApplicationCall.proxyResponse(response: HttpResponse) {
    respond(ProxyResponse(response))
}

val ignoredHeaders = setOf(HttpHeaders.ContentType, HttpHeaders.TransferEncoding, HttpHeaders.ContentLength)

data class ProxyResponse(private val response: HttpResponse) : OutgoingContent.WriteChannelContent() {
    private val originalHeaders = response.headers
    override val contentLength = originalHeaders[HttpHeaders.ContentLength]?.toLong()
    override val contentType = originalHeaders[HttpHeaders.ContentType]?.let { ContentType.parse(it) }

    // We shouldn't change some headers, since they'll be set based on contentLength and contentType variables instead
    override val headers = Headers.build {
        // originalHeaders is a map, but we filter only by keys, so we ignore values by using _
        appendAll(originalHeaders.filter { key, _ -> !ignoredHeaders.contains(key) })
    }
    override val status = response.status
    override suspend fun writeTo(channel: ByteWriteChannel) {
        response.content.copyAndClose(channel)
    }
}

private val client = HttpClient {
    expectSuccess = false // Proxy error codes instead of throwing an exception
}