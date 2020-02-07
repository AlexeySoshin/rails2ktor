import java.nio.file.Paths
import io.ktor.http.HttpMethod
import java.util.*
import kotlin.streams.toList

class RoutesConverter(private val pathToRoutes: String) {
    fun convert() {
       convert(pathToRoutes.reader().readLines())
    }

    private fun convert(routeLines: List<String>) {

    }

}


private val multipleSpaces = Regex("(\\s)+")

/**
 * Parses a single like from `rake routes`
 */
fun parseRakeRoute(routeLine: String): Route {

    val split = routeLine.trim()
            .replace(multipleSpaces, " ") // Replace multiple spaces with single space
            .split(" ")

    // Skip prefix if there's one
    var startIndex = 0
    if (split.size > 3) {
        startIndex++
    }

    val method = HttpMethod.parse(split[startIndex++])
    val path = split[startIndex++].replace("(.:format)", "")
    return Route(method, path, split[startIndex++])
}

data class Route(val method: HttpMethod, val path: String, val controller: String)