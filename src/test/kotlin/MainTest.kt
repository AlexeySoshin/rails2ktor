import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class MainTest {

    @Test
    fun `main requires an argument`() {
        val exception = assertThrows<RuntimeException> {
            main()
        }

        assertEquals("Usage: rails2ktor <path-to-routes.rb>", exception.message)
    }

    @Test
    fun `main accepts an argument`() {
        assertDoesNotThrow {
            main("")
        }
    }
}