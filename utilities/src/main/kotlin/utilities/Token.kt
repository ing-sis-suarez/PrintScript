package utilities

data class Token(val id: Int, val type: TokenType, val location: Location, val originalValue: String){
    fun actualValue() = if (type == TokenType.STRING_LITERAL) originalValue.trim(originalValue[0]) else originalValue
    fun length(): Int{
        return when (type){
            TokenType.STRING_LITERAL, TokenType.NUMBER_LITERAL,
            TokenType.IDENTIFIER, TokenType.COMMENT,
            TokenType.ERROR, TokenType.UNKNOWN -> originalValue.length
            TokenType.NUMBER_KEYWORD -> 6
            TokenType.STRING_KEYWORD -> 6
            TokenType.LET_KEYWORD -> 3
            TokenType.OPERATOR_PLUS, TokenType.OPERATOR_MINUS,
            TokenType.OPERATOR_TIMES, TokenType.OPERATOR_DIVIDE,
            TokenType.DOUBLE_DOTS, TokenType.SEMI_COLON,
            TokenType.ASIGNATION_EQUALS, TokenType.LEFT_PARENTHESIS,
            TokenType.RIGHT_PARENTHESIS, TokenType.WHITE_SPACE, -> 1
        }
    }
}


data class Location(val row: Int, val column: Int)

enum class TokenType {
    NUMBER_KEYWORD,
    STRING_KEYWORD,
    NUMBER_LITERAL,
    STRING_LITERAL,
    IDENTIFIER,
    LET_KEYWORD,
    OPERATOR_PLUS,
    OPERATOR_MINUS,
    OPERATOR_TIMES,
    OPERATOR_DIVIDE,
    DOUBLE_DOTS,
    SEMI_COLON,
    ASIGNATION_EQUALS,
    LEFT_PARENTHESIS,
    RIGHT_PARENTHESIS,
    WHITE_SPACE,
    COMMENT,
    ERROR,
    UNKNOWN
}
