package routes

import io.ktor.http.HttpMethod

data class Route(val method: HttpMethod, val path: String, val controller: String) {

    /**
     * Returns a string representaing Ktor route
     */
    fun asKtor(): String {
        val split = path.split("/").map {
            it.asKtorParameter()
        }
        val method = method.value.toLowerCase()
        return """
            $method("${split.joinToString("/")}") { }
        """.trimIndent()
    }

    /**
     * Converts Rails parameters, :param1, into Ktor parameters, {param1}
     */
    private fun String.asKtorParameter(): String {
        return if (this.startsWith(":")) {
            "{${this.substring(1 until this.length)}}"
        }
        else {
            this
        }
    }
}
