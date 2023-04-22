package lexer

import token.Token
import java.io.File

interface Lexer {
    fun lex(line: String, startIndex: Int, lineNumber: Int): Pair<Token, Int>
}
