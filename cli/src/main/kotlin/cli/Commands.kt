package cli

import FileTokenProvider
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.option
import interpreter.*
import lexer.RegularLexer
import lexer.TokenReadersProvider
import main.kotlin.token_provider.TokenProvider
import node.ASTNodeProvider
import parser.RegularParser
import provider.ASTNodeProviderImpl
import java.io.File

class PrintScript : CliktCommand() {
    override fun run() = Unit
}

val supportedVersions = listOf("1.0")
class Run : CliktCommand("Runs the program") {
    private val file by argument(help = "The file to run")
    private val version by option(*supportedVersions.toTypedArray())

    override fun run() {
        if (version in supportedVersions) {
            echo("Invalid version")
            return
        }
        val realFile = File(file)
        if (!realFile.exists()) {
            echo("File does not exist")
            return
        }
        val interpreter = initializeInterpreter(realFile, version!!)
        val result = interpreter.interpret()
        while (result !is InterpreterEndResponse) {
            when (result) {
                is InterpreterSuccessResponse -> {
                    if (result.message != null) echo(result.message)
                }
                is InterpreterFailResponse -> {
                    echo(result.error)
                    return
                }
            }
        }
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


private fun initializeInterpreter(src: File, version: String): Interpreter {
    val tokenProvider: TokenProvider = FileTokenProvider(src, RegularLexer(TokenReadersProvider().getTokenMap(version)!!))
    val astNodeProvider: ASTNodeProvider = ASTNodeProviderImpl(tokenProvider, RegularParser.createDefaultParser())
    return Interpret(astNodeProvider)
}