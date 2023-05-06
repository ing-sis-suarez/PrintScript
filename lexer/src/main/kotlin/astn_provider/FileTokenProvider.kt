import lexer.Lexer
import provider.TokenProvider
import token.Token
import java.io.File
import java.util.Scanner

class FileTokenProvider(
    src: File,
    private val lexer: Lexer
) : TokenProvider {

    private val scanner = Scanner(src)
    private var currentLine = scanner.nextLine()
    private var currentLineNumber = 1
    private var currentCharIndex = 0

    override fun readToken(): Token? {
        try {
            if (currentCharIndex >= currentLine.length) setNextLine()
            if (currentLine.isBlank()) return readToken()
        } catch (e: NoMoreTokensException) {
            return null
        }
        val result = lexer.lex(currentLine, currentCharIndex, currentLineNumber)
        currentCharIndex = result.second
        return result.first
    }

    private fun setNextLine() {
        if (scanner.hasNextLine()) {
            currentLine = scanner.nextLine()
            currentLineNumber++
            currentCharIndex = 0
        } else {
            throw NoMoreTokensException("No more tokens to read")
        }
    }

    private class NoMoreTokensException(message: String) : Exception(message)
}
