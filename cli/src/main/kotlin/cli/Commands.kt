package cli

import FileTokenProvider
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.arguments.argument
import consumer.ASTNodeConsumer
import consumer.ConsumerResponseEnd
import consumer.ConsumerResponseError
import consumer.ConsumerResponseSuccess
import interpreter.Interpret
import lexer.Lexer
import lexer.RegularLexer
import lexer.TokenReadersProvider
import parser.RegularParser
import provider.ASTNodeProviderImpl
import java.io.File

class PrintScript : CliktCommand() {
    override fun run() = Unit
}

val supportedVersions = listOf("1.0")
class Run : CliktCommand("Runs the program") {
    private val file by argument(help = "The file to run")
    private val version by argument(help = "The version of the program")

    override fun run() {
        if (version !in supportedVersions) {
            echo("Invalid version")
            return
        }
        val realFile = File(file)
        if (!realFile.exists()) {
            echo("File does not exist")
            return
        }
        val interpreter: ASTNodeConsumer = initializeInterpreter(version, realFile)
        runConsumer(interpreter)
    }

    private fun runConsumer(consumer: ASTNodeConsumer) {
        var result = consumer.consume()
        while (result !is ConsumerResponseEnd) {
            when (result) {
                is ConsumerResponseSuccess -> {
                    if (result.msg != null) echo(result.msg)
                }
                is ConsumerResponseError -> {
                    echo(result.error)
                    return
                }
            }
            result = consumer.consume()
        }
    }

    private fun initializeInterpreter(version: String, file: File): ASTNodeConsumer {
        val tokenMap = TokenReadersProvider().getTokenMap(version)
        if (tokenMap == null) {
            echo("Invalid version")
            throw IllegalArgumentException("Invalid version")
        }
        val lexer: Lexer = RegularLexer(tokenMap)
        val astNodeProvider = ASTNodeProviderImpl(FileTokenProvider(file, lexer), RegularParser.createDefaultParser())
        return Interpret(astNodeProvider)
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
