package cli

import FileTokenProvider
import Printer
import StaticCodeAnalyzer
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.default
import consumer.ASTNodeConsumer
import consumer.ConsumerResponseEnd
import consumer.ConsumerResponseError
import consumer.ConsumerResponseSuccess
import formatter.RegularFormatter
import interpreter.Interpret
import lexer.Lexer
import lexer.RegularLexer
import lexer.TokenReadersProvider
import parser.RegularParser
import provider.ASTNodeProviderImpl
import rules.FormatterConfig
import java.io.File

class PrintScript : CliktCommand() {
    override fun run() = Unit
}

val supportedVersions = listOf("1.0")
class Run : CliktCommand("Runs the program") {
    private val originalFile by argument("--initalFile", help = "The file to run")
    private val resultFile by argument("--outputFile", help = "the file to print").default("")
    private val version by argument(help = "The version of the program")

    override fun run() {
        if (version !in supportedVersions) {
            echo("Invalid version")
            return
        }
        val realFile = File(originalFile)
        if (!realFile.exists()) {
            echo("File does not exist")
            return
        }
        val interpreter: ASTNodeConsumer = initializeInterpreter(version, realFile)
        runConsumer(interpreter)
    }

    private fun runConsumer(consumer: ASTNodeConsumer) {
        val handler = Printer(resultFile)
        var result = consumer.consume()
        while (result !is ConsumerResponseEnd) {
            when (result) {
                is ConsumerResponseSuccess -> {
                    if (result.msg != null) {
                        handler.print(result.msg!!)
                    }
                }
                is ConsumerResponseError -> {
                    handler.print(result.error)
                    return
                }
            }
            result = consumer.consume()
        }
    }

    private fun initializeInterpreter(version: String, file: File): ASTNodeConsumer {
        return Interpret(Config().generateASTNprovider(version, file))
    }
}

class Format : CliktCommand("Formats the program") {
    private val originalFile by argument(help = "The file to run")
    private val resultFile by argument("--outputFile", help = "the file to print").default("")
    private val version by argument(help = "The version of the program")
    override fun run() {
        val realFile = File(originalFile)
        if (!realFile.exists()) {
            echo("File does not exist")
            return
        }
        val formater: ASTNodeConsumer = initializeFormater(version, realFile)
        runConsumer(formater)
    }

    private fun runConsumer(consumer: ASTNodeConsumer) {
        val handler = Printer(resultFile)
        var result = consumer.consume()
        while (result !is ConsumerResponseEnd) {
            when (result) {
                is ConsumerResponseSuccess -> {
                    if (result.msg != null) {
                        handler.print(result.msg!!)
                    }
                }
                is ConsumerResponseError -> {
                    handler.print(result.error)
                    return
                }
            }
            result = consumer.consume()
        }
    }

    private fun initializeFormater(version: String, file: File): ASTNodeConsumer {
        val tokenMap = TokenReadersProvider().getTokenMap(version)
        if (tokenMap == null) {
            echo("Invalid version")
            throw IllegalArgumentException("Invalid version")
        }
        val lexer: Lexer = RegularLexer(tokenMap)
        val astNodeProvider = ASTNodeProviderImpl(FileTokenProvider(file, lexer), RegularParser.createDefaultParser())
        return RegularFormatter(astNodeProvider, FormatterConfig())
    }
}

class Analyze : CliktCommand("Analyzes the program") {
    private val originalFile by argument(help = "The file to run")
    private val configFile by argument(help = "the configuration the analyzer").default("")
    private val resultFile by argument("--outputFile", help = "the file to print").default("")
    private val version by argument(help = "The version of the program")
    override fun run() {
        val realFile = File(originalFile)
        if (!realFile.exists()) {
            echo("File does not exist")
            return
        }
        val jsonFile = File(configFile)
        val sCA: ASTNodeConsumer = initializeSCA(version, realFile, jsonFile)
        runConsumer(sCA)
    }

    private fun runConsumer(consumer: ASTNodeConsumer) {
        val handler = Printer(resultFile)
        var result = consumer.consume()
        while (result !is ConsumerResponseEnd) {
            when (result) {
                is ConsumerResponseError -> {
                    handler.print(result.error)
                }
            }
            result = consumer.consume()
        }
    }

    private fun initializeSCA(version: String, file: File, config: File): ASTNodeConsumer {
        return if (!config.exists()) {
            StaticCodeAnalyzer("", Config().generateASTNprovider(version, file))
        } else {
            StaticCodeAnalyzer(config.readText(), Config().generateASTNprovider(version, file))
        }
    }
}

class Config {
    fun generateASTNprovider(version: String, file: File): ASTNodeProviderImpl {
        val tokenMap = TokenReadersProvider().getTokenMap(version) ?: throw IllegalArgumentException("Invalid version")
        val lexer: Lexer = RegularLexer(tokenMap)
        return ASTNodeProviderImpl(FileTokenProvider(file, lexer), RegularParser.createDefaultParser())
    }
}
fun main(args: Array<String>) = PrintScript()
    .subcommands(Run(), Format(), Analyze())
    .main(args)
