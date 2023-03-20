package lexer

import utilities.Token

interface Lexer {
    fun lex(text: String): List<Token>
}