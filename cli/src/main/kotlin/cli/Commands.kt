package cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.option

class PrintScript : CliktCommand() {
    override fun run() = Unit
}

class Run : CliktCommand("Runs the program") {
    val file by argument(help = "The file to run")
    val version by option("1.0", "1.1")

    override fun run() {
        // TODO: Implement
    }
}

class Format : CliktCommand("Formats the program") {
    val file by argument(help = "The file to run")

    override fun run() {
        // TODO: Implement
    }
}

class Analyze : CliktCommand("Analyzes the program") {
    val file by argument(help = "The file to run")
    override fun run() {
        // TODO: Implement
    }
}
fun main(args: Array<String>) = PrintScript()
    .subcommands(Run(), Format(), Analyze())
    .main(args)
