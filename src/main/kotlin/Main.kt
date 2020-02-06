
fun main(vararg args: String) {
    require(args.isNotEmpty()) {
        "Usage: rails2ktor <path-to-routes.rb>"
    }

    RoutesConverter(args[0]).convert()
}