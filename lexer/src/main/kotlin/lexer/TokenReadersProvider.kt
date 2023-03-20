package lexer

import utilities.Location
import utilities.Token
import utilities.TokenType

class TokenReadersProvider {

    private val commentReader: TokenReader = Pair(
        {string, startIndex -> isThisString(string, startIndex, "//")},
        {id, string, location -> Pair(Token(id, TokenType.COMMENT, location, string), string.length)})
    private val whiteSpaceReader: TokenReader = Pair(
        {string, startIndex -> isThisString(string, startIndex, " ")},
        { id, _, location -> Pair(Token(id, TokenType.WHITE_SPACE, location, ""), location.column + 1)})
    private val letKeyWordReader: TokenReader = Pair(
        {string, startIndex -> isThisString(string, startIndex, "let") },
        { id, _, location -> Pair(Token(id, TokenType.LET_KEYWORD, location, ""), location.column + 3)})
    private val numberKeyWordReader: TokenReader = Pair(
        {string, startIndex -> isThisString(string, startIndex, "number") },
        { id, _, location -> Pair(Token(id, TokenType.NUMBER_KEYWORD, location, ""), location.column + 6)})
    private val stringKeyWordReader: TokenReader = Pair(
        {string, startIndex -> isThisString(string, startIndex, "string") },
        { id, _, location -> Pair(Token(id, TokenType.STRING_KEYWORD, location, ""), location.column + 6)})
    private val doubleDotsReader: TokenReader = Pair(
        { string, startIndex -> isThisString(string, startIndex, ":")},
        { id, _, location -> Pair(Token(id, TokenType.DOUBLE_DOTS, location, ""), location.column + 1)})
    private val semiColonReader: TokenReader = Pair(
        { string, startIndex -> isThisString(string, startIndex, ";")},
        { id, _, location -> Pair(Token(id, TokenType.SEMI_COLON, location, ""), location.column + 1)})
    private val plusReader: TokenReader = Pair(
        { string, startIndex -> isThisString(string, startIndex, "+")},
        { id, _, location -> Pair(Token(id, TokenType.OPERATOR_PLUS, location, ""), location.column + 1)})
    private val minusReader: TokenReader = Pair(
        { string, startIndex -> isThisString(string, startIndex, "-")},
        { id, _, location -> Pair(Token(id, TokenType.OPERATOR_MINUS, location, ""), location.column + 1)})
    private val divisionReader: TokenReader = Pair(
        { string, startIndex -> isThisString(string, startIndex, "/")},
        { id, _, location -> Pair(Token(id, TokenType.OPERATOR_DIVIDE, location, ""), location.column + 1)})
    private val timesReader: TokenReader = Pair(
        { string, startIndex -> isThisString(string, startIndex, "*")},
        { id, _, location -> Pair(Token(id, TokenType.OPERATOR_TIMES, location, ""), location.column + 1)})
    private val leftParenthesisReader: TokenReader = Pair(
        { string, startIndex -> isThisString(string, startIndex, "(")},
        { id, _, location -> Pair(Token(id, TokenType.LEFT_PARENTHESIS, location, ""), location.column + 1)})
    private val rightParenthesisReader: TokenReader = Pair(
        { string, startIndex -> isThisString(string, startIndex, ")")},
        { id, _, location -> Pair(Token(id, TokenType.RIGHT_PARENTHESIS, location, ""), location.column + 1)})
    private val equalsReader: TokenReader = Pair(
        { string, startIndex -> isThisString(string, startIndex, "=")},
        { id, _, location -> Pair(Token(id, TokenType.ASIGNATION_EQUALS, location, ""), location.column + 1)})
    private val numberLiteralReader: TokenReader = Pair(
        { string, startIndex -> string[startIndex].isDigit() },
        { id, string, location -> Pair(Token(id, TokenType.NUMBER_LITERAL, location, cutNumberFromLine(string, location)), calculateEndOfNumber(string, location.column))})
    private val identifierReader: TokenReader = Pair(
        { string, startIndex -> string[startIndex].isLetter() },
        { id, string, location -> Pair(Token(id, TokenType.IDENTIFIER, location, cutIdentifierFromLine(string, location)), calculateEndOfIdentifier(string, location.column))})
    private val quotationMarksStringReader: TokenReader = Pair(
        { string, startIndex -> string[startIndex] == '"'},
        { id, string, location -> Pair(Token(id, TokenType.STRING_LITERAL, location, curStringLitFromLine(string, location, '"')), calculateEndOfString(string, location.column, '"', location))})
    private val apostropheStringReader: TokenReader = Pair(
        { string, startIndex -> string[startIndex] == '\''},
        { id, string, location -> Pair(Token(id, TokenType.STRING_LITERAL, location, curStringLitFromLine(string, location, '\'')), calculateEndOfString(string, location.column, '\'', location))})



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
typealias StringToTokenFunc = (id: Int, line: String, location: Location) -> Pair<Token, Int>
typealias TokenReader = Pair<TokenVerifierFunc, StringToTokenFunc>