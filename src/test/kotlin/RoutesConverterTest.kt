import io.ktor.http.HttpMethod
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class RoutesConverterTest {

    @Test
    fun `does nothing`() {
        val routes = RoutesConverter("./src/test/resources/blog").convert()

        assertTrue(routes.contains(Route(HttpMethod.Get, "/welcome/index", "welcome#index")))
    }
}