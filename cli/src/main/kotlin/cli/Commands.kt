package cli

import FileTokenProvider
import SCAJsonReader
import StaticCodeAnalyzer
import cli.PrintScript.Companion.runConsumer
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.default
import consumer.ASTNodeConsumer
import consumer.ASTNodeConsumerInterpreter
import consumer.ConsumerResponseEnd
import consumer.ConsumerResponseError
import consumer.ConsumerResponseImput
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

    companion object {
        fun runConsumer(consumer: ASTNodeConsumer, resultFile: String) {
            val handler: OutputHandler = Printer(resultFile)
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
    }
}

val supportedVersions = listOf("1.0", "1.1")

class Run : CliktCommand("Runs the program") {
    private val originalFile by argument("--initalFile", help = "The file to run")
    private val version by argument(help = "The version of the program")
    private val resultFile by argument("--outputFile", help = "the file to print").default("")
    private val inputFile by argument("--imputFile", help = "the file to ask input").default("")

    override fun run() {
        if (version !in supportedVersions) {
            echo("Invalid version")
            return
        }
        val realFile = File(originalFile)
        if (!realFile.exists()) {
            echo("Origin file does not exist")
            return
        }

        val interpreter: ASTNodeConsumerInterpreter = initializeInterpreter(version, realFile)
        runConsumerInterpreter(interpreter, resultFile)
    }

    private fun initializeInterpreter(version: String, file: File): ASTNodeConsumerInterpreter {
        return Interpret(Config().generateASTNprovider(version, file))
    }

    fun runConsumerInterpreter(consumer: ASTNodeConsumerInterpreter, resultFile: String) {
        val handler: OutputHandler = Printer(resultFile)
        val imputHandler: InputHandler = Inputer(inputFile)
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

                is ConsumerResponseImput -> {
                    val input: String = imputHandler.Input(result.msg)
                    consumer.getValue(input)
                }
            }
            result = consumer.consume()
        }
        deleteLastEmptyLine(resultFile)
    }

    fun deleteLastEmptyLine(filePath: String) {
        val file = File(filePath)
        if (!file.exists() || file.isDirectory) {
            return
        }
        val lines = file.readLines()
        val updatedLines = lines.dropLastWhile { it.isBlank() }
        file.writeText(updatedLines.joinToString(System.lineSeparator()))
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
        runConsumer(formater, resultFile)
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
    private val originalFile by argument("--initalFile", help = "The file to run")
    private val version by argument(help = "The version of the program")
    private val resultFile by argument("--outputFile", help = "the file to print").default("")
    private val configFile by argument("--configFile", help = "the configuration the analyzer").default("")

    override fun run() {
        val realFile = File(originalFile)
        if (!realFile.exists()) {
            echo("File does not exist")
            return
        }
        val jsonFile = SCAJsonReader(configFile)
        val sCA: ASTNodeConsumer = initializeSCA(version, realFile, jsonFile)
        runConsumer(sCA, resultFile)
    }

    private fun initializeSCA(version: String, file: File, config: SCAJsonReader): ASTNodeConsumer {
        val map = config.readJson()
        return StaticCodeAnalyzer(map, Config().generateASTNprovider(version, file))
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
