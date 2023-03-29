package lexer

import token.Location
import token.Token
import token.TokenType


class TokenReadersProvider {

    private val commentReader: TokenReader = Pair(
        {string, startIndex -> isThisString(string, startIndex, "//")},
        {id, string, location -> Token(id, TokenType.COMMENT, location, string)})
    private val whiteSpaceReader: TokenReader = Pair(
        {string, startIndex -> isThisString(string, startIndex, " ")},
        { id, _, location -> Token(id, TokenType.WHITE_SPACE, location, "")})
    private val letKeyWordReader: TokenReader = Pair(
        {string, startIndex -> isThisString(string, startIndex, "let") },
        { id, _, location -> Token(id, TokenType.LET_KEYWORD, location, "")})
    private val numberKeyWordReader: TokenReader = Pair(
        {string, startIndex -> isThisString(string, startIndex, "number") },
        { id, _, location -> Token(id, TokenType.NUMBER_KEYWORD, location, "")})
    private val stringKeyWordReader: TokenReader = Pair(
        {string, startIndex -> isThisString(string, startIndex, "string") },
        { id, _, location -> Token(id, TokenType.STRING_KEYWORD, location, "")})
    private val doubleDotsReader: TokenReader = Pair(
        { string, startIndex -> isThisString(string, startIndex, ":")},
        { id, _, location -> Token(id, TokenType.DOUBLE_DOTS, location, "")})
    private val semiColonReader: TokenReader = Pair(
        { string, startIndex -> isThisString(string, startIndex, ";")},
        { id, _, location -> Token(id, TokenType.SEMI_COLON, location, "")})
    private val plusReader: TokenReader = Pair(
        { string, startIndex -> isThisString(string, startIndex, "+")},
        { id, _, location -> Token(id, TokenType.OPERATOR_PLUS, location, "")})
    private val minusReader: TokenReader = Pair(
        { string, startIndex -> isThisString(string, startIndex, "-")},
        { id, _, location -> Token(id, TokenType.OPERATOR_MINUS, location, "")})
    private val divisionReader: TokenReader = Pair(
        { string, startIndex -> isThisString(string, startIndex, "/")},
        { id, _, location -> Token(id, TokenType.OPERATOR_DIVIDE, location, "")})
    private val timesReader: TokenReader = Pair(
        { string, startIndex -> isThisString(string, startIndex, "*")},
        { id, _, location -> Token(id, TokenType.OPERATOR_TIMES, location, "")})
    private val leftParenthesisReader: TokenReader = Pair(
        { string, startIndex -> isThisString(string, startIndex, "(")},
        { id, _, location -> Token(id, TokenType.LEFT_PARENTHESIS, location, "")})
    private val rightParenthesisReader: TokenReader = Pair(
        { string, startIndex -> isThisString(string, startIndex, ")")},
        { id, _, location -> Token(id, TokenType.RIGHT_PARENTHESIS, location, "")})
    private val equalsReader: TokenReader = Pair(
        { string, startIndex -> isThisString(string, startIndex, "=")},
        { id, _, location -> Token(id, TokenType.ASIGNATION_EQUALS, location, "")})
    private val numberLiteralReader: TokenReader = Pair(
        { string, startIndex -> string[startIndex].isDigit() },
        { id, string, location -> Token(id, TokenType.NUMBER_LITERAL, location, cutNumberFromLine(string, location))})
    private val identifierReader: TokenReader = Pair(
        { string, startIndex -> string[startIndex].isLetter() },
        { id, string, location -> Token(id, TokenType.IDENTIFIER, location, cutIdentifierFromLine(string, location))})
    private val quotationMarksStringReader: TokenReader = Pair(
        { string, startIndex -> string[startIndex] == '"'},
        { id, string, location -> createStringLitPair(id, string, location, '"')})
    private val apostropheStringReader: TokenReader = Pair(
        { string, startIndex -> string[startIndex] == '\''},
        { id, string, location -> createStringLitPair(id, string, location, '\'')})

    private fun createStringLitPair(id: Int, string: String, location: Location, stringChar: Char): Token{
        return createToken(id, location, string, stringChar)
    }

    private fun createToken(id: Int, location: Location, string: String, stringChar: Char) = try {
        Token(id, TokenType.STRING_LITERAL, location, curStringLitFromLine(string, location, stringChar))
    } catch (error: MalformedStringException) {
        Token(id, TokenType.ERROR, location, string.substring(location.column, string.length))
    }


    private val printScriptTokenList: MutableList<Pair<TokenVerifierFunc, StringToTokenFunc>> = mutableListOf(
        commentReader, whiteSpaceReader,letKeyWordReader, numberKeyWordReader, stringKeyWordReader,
        doubleDotsReader, semiColonReader, plusReader, minusReader, divisionReader, timesReader,
        leftParenthesisReader, rightParenthesisReader, equalsReader, numberLiteralReader,identifierReader,
        quotationMarksStringReader, apostropheStringReader
    )


    private val tokenListMap: MutableMap<String, List<Pair<TokenVerifierFunc, StringToTokenFunc>>> = mutableMapOf(
        Pair("PrintScript", printScriptTokenList)
    )

    fun getTokenMap(languageName: String): List<Pair<TokenVerifierFunc, StringToTokenFunc>>?{
        return tokenListMap[languageName]
    }

    private fun isAplhanumeric(c: Char): Boolean {
        return c.isLetterOrDigit()
    }

    private fun isSameChar(c: Char, c1: Char): Boolean {
        return c == c1
    }

    private fun calculateEndOfIdentifier(line: String, startIndex: Int): Int {
        for (i in startIndex + 1 until line.length){
            if (isAplhanumeric(line[i])) continue
            else return i
        }
        return line.length
    }

    private fun calculateEndOfString(line: String, startIndex: Int, c: Char, location: Location): Int {
        for (i in startIndex + 1 until line.length){
            if (!isSameChar(c, line[i])) continue
            else return i + 1
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
        for (i in startIndex + 1 until line.length){
            if ((line[i]).isDigit() || (line[i] == '.' && !passedADot)){
                if (line[i] == '.') passedADot = true
                continue
            }
            else return i
        }
        return line.length
    }

    private fun isThisString(string: String, startIndex: Int, target: String) =
        (checkIfStringEvaluatedFits(string, startIndex, target.length) && string.substring(startIndex, startIndex + target.length) == target)

    private fun checkIfStringEvaluatedFits(string: String, index: Int, stringEvaluatedLength: Int) = string.length >= index + stringEvaluatedLength
}

typealias TokenVerifierFunc = (line: String, startIndex: Int) -> Boolean
typealias StringToTokenFunc = (id: Int, line: String, location: Location) -> Token
typealias TokenReader = Pair<TokenVerifierFunc, StringToTokenFunc>