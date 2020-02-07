import java.nio.file.Paths
import io.ktor.http.HttpMethod
import java.util.*
import kotlin.streams.toList

class RoutesConverter(private val pathToRailsProject: String) {
    fun convert(): List<String> {
        // Run rake on pathToRailsProject
        // Capture the output
        val rakeOutput = runRake(pathToRailsProject)
        // Skip header
        // For each line parse into Route
        // Allow skipping Rails/Sidekiq paths?

       return convert(Paths.get(pathToRailsProject).toFile().readLines())
    }

    private fun convert(routeLines: List<String>): List<String> {
        return routeLines
    }

}

fun runRake(pathToRailsProject: String): List<String> {
    val directory = Paths.get(pathToRailsProject).toAbsolutePath().toFile()
    if (!directory.exists()) {
        throw RuntimeException("Directory $pathToRailsProject doesn't exist")
    }
    if (!directory.isDirectory) {
        throw RuntimeException("Not a directory: $pathToRailsProject")
    }
    val process = ProcessBuilder("rake", "routes")
            .directory(directory)
            .start()

    // TODO handle error stream?

    return process.inputStream.bufferedReader().lines().toList()
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