package lexer

import token.Location
import token.Token
import token.TokenType
import java.io.File
import java.util.*

class RegularLexer(private val tokenReaderList: List<Pair<TokenVerifierFunc, StringToTokenFunc>>) : Lexer {

    override fun lex(line: String, startIndex: Int, lineNumber: Int): Pair<Token, Int> {
        for ((checker, tokenParser) in tokenReaderList) {
            if (checker.invoke(line, startIndex)) {
                val result = tokenParser.invoke(line, Location(lineNumber, startIndex))
                return Pair(result, startIndex + result.length)
            }
        }
        val token = Token(TokenType.UNKNOWN, Location(lineNumber, startIndex), line[startIndex].toString(), 1)
        return Pair(token, startIndex + token.length)
    }
}