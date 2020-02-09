package routes

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import routes.runRake

class RunRakeTest {

    @Test
    fun `returns rake routes output if the directory exists`() {
        val output = runRake("./src/test/resources/blog")
        println(output)
        assertEquals(17, output.size)
        assertEquals("                   Prefix Verb   URI Pattern                                                                              Controller#Action", output[0])
        assertEquals("            welcome_index GET    /welcome/index(.:format)                                                                 welcome#index", output[1])

    }
}