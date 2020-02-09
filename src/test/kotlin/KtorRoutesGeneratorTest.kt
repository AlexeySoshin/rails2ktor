import io.ktor.http.HttpMethod
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class KtorRoutesGeneratorTest {

    @Test
    fun `wraps all routes into a routing block`() {
        val routes = listOf(Route(method= HttpMethod.Get, path="/", controller="welcome#index"),
                Route(method=HttpMethod.Post, path="/articles", controller="articles#create"))

        assertEquals("""          routing {
          		get("/") { }
post("/articles") { }
          }""".trimIndent(), KtorRoutesGenerator.generate(routes))
    }
}