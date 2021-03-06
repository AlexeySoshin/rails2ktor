package routes

import io.ktor.http.HttpMethod
import java.nio.file.Paths
import kotlin.streams.toList

class RailsRoutesParser {
    companion object {
        fun parse(pathToRailsProject: String): List<Route> {
            // Run rake on pathToRailsProject
            // Capture the output
            val rakeOutput = runRake(pathToRailsProject)
            // Skip header
            val routes = skipHeader(rakeOutput)
            // For each line parse into routes.Route
            // Allow skipping Rails/Sidekiq paths?

            return routes.map {
                parseRakeRoute(it)
            }
                    .filterNot { it.path.startsWith("/rails") }
                    .filterNot { it.method == HttpMethod("sidekiq") }
        }
    }
}

/**
 * Filters `rake routes` output from noisy gems and header
 */
fun skipHeader(rakeOutput: List<String>): List<String> {
    val headerIndex = rakeOutput.indexOfFirst {
        it.contains("Prefix Verb   URI Pattern")
    }

    return rakeOutput.subList(headerIndex + 1, rakeOutput.size)
}


/**
 * Runs `rake routes` on a specified Ruby project directory and returns the output
 */
fun runRake(pathToRailsProject: String): List<String> {
    val directory = Paths.get(pathToRailsProject).toAbsolutePath().toFile()

    // bash --login will apply any RVM configurations user has
    val process = ProcessBuilder("bash", "--login", "-c", "bundle install && rake routes")
            .directory(directory)
            .start()

    process.waitFor()
    val errors = process.errorStream.bufferedReader().lines().toList()
    if (errors.isNotEmpty()) {
        System.err.println("Error running rake: ${errors.joinToString()}")
    }

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
    return Route(method, path, split[startIndex])
}

