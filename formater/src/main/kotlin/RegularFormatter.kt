import ast.node.*
import token.Token
import token.TokenType
import java.lang.Exception

class RegularFormatter() : Formatter {
    private val spacesBetweenTokens: Int = 1
    private val SEMI_COLON = ";"
    private val LET_KEYWORD = "let"
    private val EQUALS = "="
    private val DOUBLE_DOTS = ":"
    private val LEFT_PARENTHESIS = "("
    private val RIGHT_PARENTHESIS = ")"

    override fun format(node: ASTNode): String {
        return when (node) {
            is Declaration -> formatDeclaration(node) + SEMI_COLON + "\n"
            is DeclarationInitialization -> formatInitialization(node) + SEMI_COLON + "\n"
            is Assignation -> formatAssignation(node) + SEMI_COLON + "\n"
            is MethodCall -> formatMethodCall(node) + SEMI_COLON + "\n"
            else -> throw Exception("Not valid format")
        }
    }

    // let x:Number;
    private fun formatDeclaration(node: Declaration): String {
        return LET_KEYWORD + getBlankSpaces(spacesBetweenTokens) +
                getStringValue(node.identifier) + DOUBLE_DOTS + getStringValue(node.type)
    }

    //let x:Number = 4 + 5
    private fun formatInitialization(node: DeclarationInitialization): String {
        return formatDeclaration(node.declaration) + getBlankSpaces(spacesBetweenTokens) +
                EQUALS + getBlankSpaces(spacesBetweenTokens) + formatValue(node.value)
    }

    // x = 5;
    private fun formatAssignation(node: Assignation): String {
        return getStringValue(node.identifier) + getBlankSpaces(spacesBetweenTokens) +
                EQUALS + getBlankSpaces(spacesBetweenTokens) + formatValue(node.value)
    }

    //println("hello");
    private fun formatMethodCall(node: MethodCall): String {
        return getStringValue(node.identifier) + LEFT_PARENTHESIS + formatValue(node.arguments) + RIGHT_PARENTHESIS
    }

    private fun formatValue(node: Value): String {
        return if (node.tokenList.isEmpty()) return ""
        else node.tokenList.joinToString(getBlankSpaces(spacesBetweenTokens)) { getStringValue(it) }
    }

    private fun getStringValue(token: Token):String {
        return when(token.type){
            TokenType.NUMBER_KEYWORD -> "Number"
            TokenType.STRING_KEYWORD -> "String"
            TokenType.OPERATOR_PLUS -> "+"
            TokenType.OPERATOR_MINUS -> "-"
            TokenType.OPERATOR_TIMES -> "*"
            TokenType.OPERATOR_DIVIDE -> "/"
            TokenType.STRING_LITERAL -> token.originalValue
            TokenType.NUMBER_LITERAL -> token.originalValue
            TokenType.IDENTIFIER -> token.actualValue()
            TokenType.LEFT_PARENTHESIS -> "("
            TokenType.RIGHT_PARENTHESIS -> ")"
            else -> ""
        }
    }

    private fun getBlankSpaces(x: Int): String {
        return " ".repeat(x)
    }
}