package lexer

import token.Token
import java.io.File
import java.io.InputStreamReader

interface Lexer {
    fun lex(src: File)
}
