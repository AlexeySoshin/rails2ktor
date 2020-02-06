import java.io.File

class RoutesConverter(private val pathToRoutes: String) {
    fun convert() {
       convert(pathToRoutes.reader().readLines())
    }

    private fun convert(routeLines: List<String>) {

    }

}
