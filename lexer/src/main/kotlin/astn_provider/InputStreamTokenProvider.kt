import lexer.Lexer
import provider.TokenProvider
import token.Token
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

class InputStreamTokenProvider(
    inputStream: InputStream,
    private val lexer: Lexer
) : TokenProvider {

    private val reader = BufferedReader(InputStreamReader(inputStream))
    private var currentLine: String? = reader.readLine()
    private var currentLineNumber = 1
    private var currentCharIndex = 0

    override fun readToken(): Token? {
        try {
            if (currentLine != null && currentCharIndex >= currentLine!!.length) setNextLine()
            if (currentLine?.isBlank() == true) return readToken()
        } catch (e: NoMoreTokensException) {
            return null
        }
        val result = lexer.lex(currentLine!!, currentCharIndex, currentLineNumber)
        currentCharIndex = result.second
        return result.first
    }

    private fun setNextLine() {
        currentLine = reader.readLine()
        currentCharIndex = 0
        if (currentLine == null) {
            throw NoMoreTokensException("No more tokens to read")
        }
        currentLineNumber++
    }

    private class NoMoreTokensException(message: String) : Exception(message)
}
