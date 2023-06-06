package token

data class Token(val type: TokenType, val location: Location, val originalValue: String, val length: Int) {
    fun actualValue() =
        if (type == TokenType.STRING_LITERAL) originalValue.trim(originalValue[0]) else originalValue
}

data class Location(val row: Int, val column: Int)

enum class TokenType {
    NUMBER_KEYWORD,
    STRING_KEYWORD,
    BOOLEAN_KEYWORD,
    NUMBER_LITERAL,
    STRING_LITERAL,
    BOOLEAN_LITERAL,
    IDENTIFIER,
    LET_KEYWORD,
    CONST_KEYWORD,
    IF_KEYWORD,
    ELSE_KEYWORD,
    OPERATOR_PLUS,
    OPERATOR_MINUS,
    OPERATOR_TIMES,
    OPERATOR_DIVIDE,
    IMPUT,
    DOUBLE_DOTS,
    SEMICOLON,
    ASIGNATION_EQUALS,
    LEFT_PARENTHESIS,
    RIGHT_PARENTHESIS,
    LEFT_BRACE,
    RIGHT_BRACE,
    WHITE_SPACE,
    COMMENT,
    ERROR,
    UNKNOWN
}
