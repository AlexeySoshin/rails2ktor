package routes

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class FilterRoutesTest {

    @Test
    fun `filters noisy gems output and header`() {
        val header = "                                        Prefix Verb   URI Pattern                                                                              Controller#Action"
        val route = "                                       health GET    /health(.:format)                                                                        health#show"
        val routes = skipHeader(listOf(" noisy gem ", " noisy gem", header, route))

        assertEquals(listOf(route), routes)
    }
}