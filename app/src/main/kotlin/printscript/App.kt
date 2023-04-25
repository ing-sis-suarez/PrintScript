package printscript

// import Evaluator.Evaluator
import FileTokenProvider
import interpreter.Interpreter
import lexer.RegularLexer
import lexer.TokenReadersProvider
import parser.RegularParser
import provider.ASTNodeProviderImpl
import things.ErrorHandler
import things.InputProvider
import things.PrintEmitter
import things.PrintScriptInterpreter
import token.Location
import token.Token
import token.TokenType
import java.io.File

class App : PrintScriptInterpreter {

    override fun execute(src: File, version: String, emitter: PrintEmitter, handler: ErrorHandler, provider: InputProvider) {
        val tokenMap = TokenReadersProvider().getTokenMap(version)
        if (tokenMap == null) handler.reportError("Unsupported version")
        val tokenProvider = FileTokenProvider(src, RegularLexer(tokenMap!!))
        val astProvider = ASTNodeProviderImpl(tokenProvider, RegularParser.createDefaultParser())
        // val interpreter: Interpreter = Evaluator(astProvider)
    }

    private fun runInterpretation(interpreter: Interpreter, handler: ErrorHandler) {
    }

    private fun interpret(interpreter: Interpreter) {
        interpreter.interpret()
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
