package formatter

import config.FormatterConfig
import consumer.ASTNodeConsumer
import consumer.ConsumerResponse
import consumer.ConsumerResponseEnd
import consumer.ConsumerResponseError
import consumer.ConsumerResponseSuccess
import node.ASTNode
import node.Assignation
import node.Condition
import node.Declaration
import node.DeclarationInitialization
import node.MethodCall
import node.Value
import provider.ASTNProviderResponse
import provider.ASTNProviderResponseEnd
import provider.ASTNProviderResponseError
import provider.ASTNProviderResponseSuccess
import provider.ASTNodeProvider
import token.Token
import token.TokenType
import kotlin.Exception

class RegularFormatter(private val astNodeProvider: ASTNodeProvider, private val config: FormatterConfig) : ASTNodeConsumer {

    override fun consume(): ConsumerResponse {
        return when (val astRes: ASTNProviderResponse = astNodeProvider.readASTNode()) {
            is ASTNProviderResponseSuccess -> { getResponse(astRes.astNode) }
            is ASTNProviderResponseEnd -> { ConsumerResponseEnd() }
            is ASTNProviderResponseError -> { ConsumerResponseError(astRes.error) }
            else -> { throw Exception("Unknown exception") }
        }
    }

    private fun getResponse(node: ASTNode): ConsumerResponse {
        return when (node) {
            is Declaration -> ConsumerResponseSuccess(formatDeclaration(node) + SEMI_COLON)
            is DeclarationInitialization -> ConsumerResponseSuccess(formatInitialization(node) + SEMI_COLON)
            is Assignation -> ConsumerResponseSuccess(formatAssignation(node) + SEMI_COLON)
            is MethodCall -> ConsumerResponseSuccess(formatMethodCall(node) + SEMI_COLON)
            is Condition -> ConsumerResponseSuccess(formatCondition(node))
            else -> ConsumerResponseError("Could not recognize syntax")
        }
    }

    // let x:number;
    private fun formatDeclaration(node: Declaration): String {
        return LET_KEYWORD + getBlankSpaces(config.spacesBetweenTokens) +
            getStringValue(node.identifier) + DOUBLE_DOTS + getBlankSpaces(config.spacesBetweenTokens) + getStringValue(node.type)
    }

    // let x:number = 4 + 5
    private fun formatInitialization(node: DeclarationInitialization): String {
        return formatDeclaration(node.declaration) + getBlankSpaces(config.spacesBetweenTokens) +
            EQUALS + getBlankSpaces(config.spacesBetweenTokens) + formatValue(node.value)
    }

    // x = 5;
    private fun formatAssignation(node: Assignation): String {
        return getStringValue(node.identifier) + getBlankSpaces(config.spacesBetweenTokens) +
            EQUALS + getBlankSpaces(config.spacesBetweenTokens) + formatValue(node.value)
    }

    /*
     * if(condition) {
     * }else {
     * }
     */
    private fun formatCondition(node: Condition): String {
        return if (node.condition == null) formatElse(node) else formatIf(node)
    }

    private fun formatIf(node: Condition): String {
        return IF_KEYWORD + LEFT_PARENTHESIS + formatValue(node.condition!!) + RIGHT_PARENTHESIS +
            getBlankSpaces(config.spacesBetweenTokens) + LEFT_BRACE + NEW_LINE + formatConditionBody(node) + RIGHT_BRACE
    }

    private fun formatElse(node: Condition): String {
        return ELSE_KEYWORD + getBlankSpaces(config.spacesBetweenTokens) + LEFT_BRACE + NEW_LINE +
            formatConditionBody(node) + RIGHT_BRACE
    }

    private fun formatConditionBody(node: Condition): String {
        val result = StringBuilder()
        node.ifAction.forEach {
            val response = getResponse(it) as ConsumerResponseSuccess
            result.append(getBlankSpaces(config.indentSize) + response.msg + NEW_LINE)
        }
        return result.toString()
    }

    // println("hello world");
    private fun formatMethodCall(node: MethodCall): String {
        return getStringValue(node.identifier) + LEFT_PARENTHESIS + formatValue(node.arguments) + RIGHT_PARENTHESIS
    }

    // (9 + 8) * 7
    private fun formatValue(node: Value): String {
        val result = StringBuilder()
        if (node.tokenList.isEmpty()) return result.toString()
        node.tokenList.forEachIndexed() { index, token ->
            result.append(
                if (isLeftParenthesisToken(token) || isNextToRightParenthesisToken(index, node.tokenList) || isLastToken(index, node.tokenList)) {
                    getStringValue(token)
                } else {
                    getStringValue(token) + getBlankSpaces(config.spacesBetweenTokens)
                }
            )
        }
        return result.toString()
    }

    private fun isLeftParenthesisToken(token: Token): Boolean {
        return token.type == TokenType.LEFT_PARENTHESIS
    }

    private fun isNextToRightParenthesisToken(index: Int, tokenList: List<Token>): Boolean {
        return index < tokenList.size - 1 && tokenList[index + 1].type == TokenType.RIGHT_PARENTHESIS
    }

    private fun isLastToken(index: Int, tokenList: List<Token>): Boolean {
        return index == tokenList.size - 1
    }

    private fun getStringValue(token: Token): String {
        return when (token.type) {
            TokenType.NUMBER_KEYWORD -> "number"
            TokenType.STRING_KEYWORD -> "string"
            TokenType.BOOLEAN_KEYWORD -> "boolean"
            TokenType.OPERATOR_PLUS -> "+"
            TokenType.OPERATOR_MINUS -> "-"
            TokenType.OPERATOR_TIMES -> "*"
            TokenType.OPERATOR_DIVIDE -> "/"
            TokenType.STRING_LITERAL -> token.originalValue
            TokenType.BOOLEAN_LITERAL -> token.originalValue
            TokenType.NUMBER_LITERAL -> token.originalValue
            TokenType.IDENTIFIER -> token.originalValue
            TokenType.LEFT_PARENTHESIS -> "("
            TokenType.RIGHT_PARENTHESIS -> ")"
            TokenType.COMMENT -> "//"
            else -> ""
        }
    }

    private fun getBlankSpaces(x: Int): String {
        return " ".repeat(x)
    }

    private companion object {
        private const val ELSE_KEYWORD = "else"
        private const val IF_KEYWORD = "if"
        private const val SEMI_COLON = ";"
        private const val LET_KEYWORD = "let"
        private const val EQUALS = "="
        private const val DOUBLE_DOTS = ":"
        private const val LEFT_PARENTHESIS = "("
        private const val RIGHT_PARENTHESIS = ")"
        private const val LEFT_BRACE = "{"
        private const val RIGHT_BRACE = "}"
        private const val NEW_LINE = "\n"
    }
}
