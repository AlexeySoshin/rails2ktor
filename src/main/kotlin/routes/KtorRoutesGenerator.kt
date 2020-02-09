package routes

class KtorRoutesGenerator {
    companion object {
        fun generate(routes: List<Route>): String {
            return """
            routing {
            ${routes.joinToString("\n") { "\t\t" + it.asKtor() }}
            }
        """.trimIndent()
        }
    }
}