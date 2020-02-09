package routes

class KtorRoutesGenerator {
    companion object {
        fun generate(routes: List<Route>): String {
            return """
            routing {
            ${routes.joinToString("\n") { it.asKtor()
                    .replace("{ }", "{ call.proxy(target) }") }}
            }
        """.trimIndent()
        }
    }
}