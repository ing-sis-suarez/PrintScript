package lexer

import token.Token

interface Lexer {
    fun lex(line: String, startIndex: Int, lineNumber: Int): Pair<Token, Int>
}
