package routes

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class RunRakeTest {

    @Test
    fun `returns rake routes output if the directory exists`() {
        val output = runRake("./src/test/resources/blog")
        assertTrue(output.containsAll(listOf("                   Prefix Verb   URI Pattern                                                                              Controller#Action",
                "            welcome_index GET    /welcome/index(.:format)                                                                 welcome#index")))

    }
}