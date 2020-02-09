import routes.KtorRoutesGenerator
import routes.RailsRoutesParser

fun main(vararg args: String) {
    require(args.isNotEmpty()) {
        "Usage: rails2ktor <path-to-routes.rb>"
    }

    println(KtorRoutesGenerator.generate(RailsRoutesParser.parse(args[0])))
}