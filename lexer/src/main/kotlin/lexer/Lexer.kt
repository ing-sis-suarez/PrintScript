package lexer

import token.Token


interface Lexer {
    fun lex(text: String): List<Token>
}