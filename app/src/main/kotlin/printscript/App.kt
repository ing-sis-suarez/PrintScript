package printscript

import Evaluator
import lexer.RegularLexer
import lexer.TokenReadersProvider
import parser.Parser
import parser.RegularParser
import things.ErrorHandler
import things.InputProvider
import things.PrintEmitter
import things.PrintScriptInterpreter
import token.Location
import token.Token
import token.TokenType
import java.io.File
import java.util.*

class App : PrintScriptInterpreter{

    override fun execute(src: File, version: String, emitter: PrintEmitter, handler: ErrorHandler, provider: InputProvider) {
        val file = File("Tokens.txt")
        file.delete()
        RegularLexer(TokenReadersProvider().getTokenMap("1.0")!!).lex(src)

        val listOfTokensInLine = mutableListOf<Token>()
        val scanner = Scanner(file)
        val interpreter = Evaluator()
        val parser: Parser = RegularParser.createDefaultParser()
        while (scanner.hasNextLine()) {
            val token = toToken(scanner.nextLine())
            listOfTokensInLine.add(token)

            if (token.type == TokenType.SEMICOLON) {
                try {
                    interpreter.evaluate(parser.parse(listOfTokensInLine))
                    listOfTokensInLine.clear()
                } catch (exception: Exception) {
                    handler.reportError(exception.message)
                    return
                }
            }
            if (!scanner.hasNextLine() && token.type != TokenType.SEMICOLON) {
                throw java.lang.Exception("There is a semicolon missing in the last line of the file")
            }
        }
    }

    private fun toToken(tokenString: String): Token { // reads a token.toString() and returns a token
        val tokenRegex =
            """Token\(type=([A-Z_]+),\s*location=Location\(row=(\d+),\s*column=(\d+)\),\s*originalValue=(.*),\s*length=(\d+)\)""".toRegex()

        val matchResult = tokenRegex.matchEntire(tokenString)
        if (matchResult != null) {
            val (typeStr, row, column, originalValue, length) = matchResult.destructured
            val type = TokenType.valueOf(typeStr)
            val location = Location(row.toInt(), column.toInt())
            return Token(type, location, originalValue.trim(), length.toInt())
        }
        throw IllegalArgumentException("Invalid token string: $tokenString")
    }
}
