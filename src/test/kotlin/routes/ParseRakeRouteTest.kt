package routes

import io.ktor.http.HttpMethod
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import routes.Route
import routes.parseRakeRoute

class ParseRakeRouteTest {


    @Test
    fun `route with prefix`() {
        val route = parseRakeRoute("   edit_article GET    /articles/:id/edit(.:format)   articles#edit  ")

        assertEquals(Route(HttpMethod.Get, "/articles/:id/edit", "articles#edit"), route)
    }

    @Test
    fun `route without prefix`() {
        val route = parseRakeRoute("                PATCH  /articles/:id(.:format)        articles#update")

        assertEquals(Route(HttpMethod.Patch, "/articles/:id", "articles#update"), route)
    }
}