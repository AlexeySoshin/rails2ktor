
fun main(vararg args: String) {
    require(args.isNotEmpty()) {
        "Usage: rails2ktor <path-to-routes.rb>"
    }

    RailsRoutesParser().parse(args[0])
}