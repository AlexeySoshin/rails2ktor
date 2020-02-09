import io.ktor.http.HttpMethod
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class RailsRoutesParserTest {

    @Test
    fun `parses routes of a project`() {
        val routes = RailsRoutesParser.parse("./src/test/resources/blog")

        assertTrue(routes.contains(Route(HttpMethod.Get, "/welcome/index", "welcome#index")))
    }
}