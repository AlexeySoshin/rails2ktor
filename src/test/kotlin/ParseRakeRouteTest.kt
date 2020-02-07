import io.ktor.http.HttpMethod
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ParseRakeRouteTest {


    @Test
    fun `route with prefix`() {
        val route = parseRakeRoute("   health GET    /health(.:format)                   health#show       ")

        assertEquals(Route(HttpMethod.Get,"/health", "health#show"), route)
    }

    @Test
    fun `route without prefix`() {
        val route = parseRakeRoute("          PUT    /api/v1/:cat_id/status(.:format)    api/v1/cat#status")

        assertEquals(Route(HttpMethod.Put, "/api/v1/:cat_id/status", "api/v1/cat#status"), route)
    }
}