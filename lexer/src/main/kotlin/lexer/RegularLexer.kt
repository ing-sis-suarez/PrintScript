package lexer

import token.Location
import token.Token
import token.TokenType
import java.io.File
import java.util.*

class RegularLexer(private val tokenReaderList: List<Pair<TokenVerifierFunc, StringToTokenFunc>>) : Lexer {

    override fun lex(src: File) {
        val scanner = Scanner(src)
        var currentLineNumber = 1
        val fileToWrite = File("Tokens.txt")
        while (scanner.hasNextLine()) {
            val actualLine = scanner.nextLine()
            val lineTokenList = evaluateLine(actualLine, currentLineNumber, mutableListOf())
            currentLineNumber++
            fileToWrite.appendText(lineTokenList.joinToString("\n") { it.toString() })
            fileToWrite.appendText("\n")
        }
    }

    private fun evaluateLine(
        line: String,
        lineNumber: Int,
        tokens: MutableList<Token>
    ): MutableList<Token> {
        var i = 0
        while (i < line.length) {
            i = matchWord(line, i, tokens, lineNumber)
        }
        return tokens
    }

    private fun matchWord(
        line: String,
        startIndex: Int,
        tokens: MutableList<Token>,
        lineNumber: Int
    ): Int {
        var i = startIndex
        i = calculateToken(line, i, tokens, lineNumber)
        return if (i == startIndex) {
            tokens.add(Token(TokenType.UNKNOWN, Location(lineNumber, i), line[i].toString(), 1))
            i + 1
        } else {
            i
        }
    }

    private fun calculateToken(
        line: String,
        acutalIndex: Int,
        tokens: MutableList<Token>,
        lineNumber: Int
    ): Int {
        var i = acutalIndex
        for ((checker, tokenParser) in tokenReaderList) {
            if (checker.invoke(line, i)) {
                val result = tokenParser.invoke(line, Location(lineNumber, i))
                tokens.add(result)
                i += result.length
                break
            }
        }
        return i
    }
}
