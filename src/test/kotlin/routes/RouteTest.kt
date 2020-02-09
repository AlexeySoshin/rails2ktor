package routes

import io.ktor.http.HttpMethod
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class RouteTest {

    @Test
    fun `asKtor returns route as stringified Ktor route`() {
        val route = Route(method = HttpMethod.Get, path = "/articles/:id/edit", controller = "articles#edit")

        assertEquals("""
            get("/articles/{id}/edit") { }
        """.trimIndent(), route.asKtor())
    }
}