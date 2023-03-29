package lexer

import token.Location
import token.Token
import token.TokenType


class RegularLexer(private val tokenReaderList: List<Pair<TokenVerifierFunc, StringToTokenFunc>>) : Lexer {


    override fun lex(text: String): List<Token>{
        return generateTokens(text)
    }

    private fun breakIntoLines(text: String): List<String>{
        return text.trim().split(System.lineSeparator())
    }

    private fun generateTokens(text: String): List<Token> {
        val lines = breakIntoLines(text)
        val tokens = mutableListOf<Token>()
        for (j in lines.indices) {
            evaluateLine(lines[j], j, tokens)
        }
        return tokens
    }

    private fun evaluateLine(
        line: String,
        lineNumber: Int,
        tokens: MutableList<Token>
    ) {
        var i = 0
        while (i < line.length) {
            i = matchWord(line, i, tokens, lineNumber)
        }
    }

    private fun matchWord(
        line: String,
        startIndex: Int,
        tokens: MutableList<Token>,
        lineNumber: Int
    ): Int {
        var i = startIndex
        for ((checker, tokenParser) in tokenReaderList) {
            if (checker.invoke(line, i)) {
                val result = tokenParser.invoke(tokens.size, line, Location(lineNumber, i))
                tokens.add(result)
                i += result.length()
                break
            }
        }
        return if (i == startIndex) {
            tokens.add(Token(tokens.size, TokenType.UNKNOWN, Location(lineNumber, i), line[i].toString()))
            i + 1
        } else i
    }
}

