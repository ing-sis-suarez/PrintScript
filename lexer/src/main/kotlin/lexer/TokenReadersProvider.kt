package lexer

import token.Location
import token.Token
import token.TokenType

class TokenReadersProvider {

    private val commentReader: TokenReader = Pair(
        { string, startIndex -> isThisString(string, startIndex, "//") },
        { string, location -> Token(TokenType.COMMENT, location, string, string.length) }
    )
    private val whiteSpaceReader: TokenReader = Pair(
        { string, startIndex -> isThisString(string, startIndex, " ") },
        { _, location -> Token(TokenType.WHITE_SPACE, location, "", 1) }
    )
    private val letKeyWordReader: TokenReader = Pair(
        { string, startIndex -> isThisString(string, startIndex, "let") },
        { _, location -> Token(TokenType.LET_KEYWORD, location, "", 3) }
    )
    private val numberKeyWordReader: TokenReader = Pair(
        { string, startIndex -> isThisString(string, startIndex, "number") },
        { _, location -> Token(TokenType.NUMBER_KEYWORD, location, "", 6) }
    )
    private val stringKeyWordReader: TokenReader = Pair(
        { string, startIndex -> isThisString(string, startIndex, "string") },
        { _, location -> Token(TokenType.STRING_KEYWORD, location, "", 6) }
    )
    private val doubleDotsReader: TokenReader = Pair(
        { string, startIndex -> isThisString(string, startIndex, ":") },
        { _, location -> Token(TokenType.DOUBLE_DOTS, location, "", 1) }
    )
    private val semiColonReader: TokenReader = Pair(
        { string, startIndex -> isThisString(string, startIndex, ";") },
        { _, location -> Token(TokenType.SEMICOLON, location, "", 1) }
    )
    private val plusReader: TokenReader = Pair(
        { string, startIndex -> isThisString(string, startIndex, "+") },
        { _, location -> Token(TokenType.OPERATOR_PLUS, location, "", 1) }
    )
    private val minusReader: TokenReader = Pair(
        { string, startIndex -> isThisString(string, startIndex, "-") },
        { _, location -> Token(TokenType.OPERATOR_MINUS, location, "", 1) }
    )
    private val divisionReader: TokenReader = Pair(
        { string, startIndex -> isThisString(string, startIndex, "/") },
        { _, location -> Token(TokenType.OPERATOR_DIVIDE, location, "", 1) }
    )
    private val timesReader: TokenReader = Pair(
        { string, startIndex -> isThisString(string, startIndex, "*") },
        { _, location -> Token(TokenType.OPERATOR_TIMES, location, "", 1) }
    )
    private val leftParenthesisReader: TokenReader = Pair(
        { string, startIndex -> isThisString(string, startIndex, "(") },
        { _, location -> Token(TokenType.LEFT_PARENTHESIS, location, "", 1) }
    )
    private val rightParenthesisReader: TokenReader = Pair(
        { string, startIndex -> isThisString(string, startIndex, ")") },
        { _, location -> Token(TokenType.RIGHT_PARENTHESIS, location, "", 1) }
    )
    private val equalsReader: TokenReader = Pair(
        { string, startIndex -> isThisString(string, startIndex, "=") },
        { _, location -> Token(TokenType.ASIGNATION_EQUALS, location, "", 1) }
    )
    private val numberLiteralReader: TokenReader = Pair(
        { string, startIndex -> string[startIndex].isDigit() },
        { string, location -> Token(TokenType.NUMBER_LITERAL, location, cutNumberFromLine(string, location), cutNumberFromLine(string, location).length) }
    )
    private val identifierReader: TokenReader = Pair(
        { string, startIndex -> string[startIndex].isLetter() },
        { string, location -> Token(TokenType.IDENTIFIER, location, cutIdentifierFromLine(string, location), cutIdentifierFromLine(string, location).length) }
    )
    private val quotationMarksStringReader: TokenReader = Pair(
        { string, startIndex -> string[startIndex] == '"' },
        { string, location -> createStringLitPair(string, location, '"') }
    )
    private val apostropheStringReader: TokenReader = Pair(
        { string, startIndex -> string[startIndex] == '\'' },
        { string, location -> createStringLitPair(string, location, '\'') }
    )

    private fun createStringLitPair(string: String, location: Location, stringChar: Char): Token {
        return createToken(location, string, stringChar)
    }

    private fun createToken(location: Location, string: String, stringChar: Char) = try {
        Token(TokenType.STRING_LITERAL, location, curStringLitFromLine(string, location, stringChar), curStringLitFromLine(string, location, stringChar).length)
    } catch (error: MalformedStringException) {
        Token(TokenType.ERROR, location, string.substring(location.column, string.length), string.length - location.column)
    }

    private val printScriptTokenList: MutableList<Pair<TokenVerifierFunc, StringToTokenFunc>> = mutableListOf(
        commentReader, whiteSpaceReader, letKeyWordReader, numberKeyWordReader, stringKeyWordReader,
        doubleDotsReader, semiColonReader, plusReader, minusReader, divisionReader, timesReader,
        leftParenthesisReader, rightParenthesisReader, equalsReader, numberLiteralReader, identifierReader,
        quotationMarksStringReader, apostropheStringReader
    )

    private val tokenListMap: MutableMap<String, List<Pair<TokenVerifierFunc, StringToTokenFunc>>> = mutableMapOf(
        Pair("PrintScript", printScriptTokenList)
    )

    fun getTokenMap(languageName: String): List<Pair<TokenVerifierFunc, StringToTokenFunc>>? {
        return tokenListMap[languageName]
    }

    private fun isAplhanumeric(c: Char): Boolean {
        return c.isLetterOrDigit()
    }

    private fun isSameChar(c: Char, c1: Char): Boolean {
        return c == c1
    }

    private fun calculateEndOfIdentifier(line: String, startIndex: Int): Int {
        for (i in startIndex + 1 until line.length) {
            if (isAplhanumeric(line[i]) || line[i] == '_') {
                continue
            } else {
                return i
            }
        }
        return line.length
    }

    private fun calculateEndOfString(line: String, startIndex: Int, c: Char, location: Location): Int {
        for (i in startIndex + 1 until line.length) {
            if (!isSameChar(c, line[i])) {
                continue
            } else {
                return i + 1
            }
        }
        throw MalformedStringException("String was not closed properly at ", location)
    }

    private fun curStringLitFromLine(string: String, location: Location, stringStarter: Char) =
        string.substring(location.column, calculateEndOfString(string, location.column, stringStarter, location))

    private fun cutIdentifierFromLine(string: String, location: Location) =
        string.substring(location.column, calculateEndOfIdentifier(string, location.column))

    private fun cutNumberFromLine(string: String, location: Location) =
        string.substring(location.column, calculateEndOfNumber(string, location.column))

    private fun calculateEndOfNumber(line: String, startIndex: Int): Int {
        var passedADot = false
        for (i in startIndex + 1 until line.length) {
            if ((line[i]).isDigit() || (line[i] == '.' && !passedADot)) {
                if (line[i] == '.') passedADot = true
                continue
            } else {
                return i
            }
        }
        return line.length
    }

    private fun isThisString(string: String, startIndex: Int, target: String) =
        (checkIfStringEvaluatedFits(string, startIndex, target.length) && string.substring(startIndex, startIndex + target.length) == target)

    private fun checkIfStringEvaluatedFits(string: String, index: Int, stringEvaluatedLength: Int) = string.length >= index + stringEvaluatedLength
}

typealias TokenVerifierFunc = (line: String, startIndex: Int) -> Boolean
typealias StringToTokenFunc = (line: String, location: Location) -> Token
typealias TokenReader = Pair<TokenVerifierFunc, StringToTokenFunc>
