package lexer

import java.io.File

interface Lexer {
    fun lex(src: File)
}
