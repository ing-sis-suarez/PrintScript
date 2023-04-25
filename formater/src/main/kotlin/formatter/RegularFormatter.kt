package formatter

import consumer.ASTNodeConsumer
import consumer.ConsumerResponse
import consumer.ConsumerResponseError
import consumer.ConsumerResponseSuccess
import node.ASTNode
import node.Assignation
import node.Declaration
import node.DeclarationInitialization
import node.MethodCall
import node.Value
import provider.ASTNProviderResponse
import provider.ASTNProviderResponseError
import provider.ASTNProviderResponseSuccess
import provider.ASTNodeProvider
import rules.FormatterConfig
import token.Token
import token.TokenType
import kotlin.Exception

class RegularFormatter(private val astNodeProvider: ASTNodeProvider, private val config: FormatterConfig) : ASTNodeConsumer {
    private val SEMI_COLON = ";"
    private val LET_KEYWORD = "let"
    private val EQUALS = "="
    private val DOUBLE_DOTS = ": "
    private val LEFT_PARENTHESIS = "("
    private val RIGHT_PARENTHESIS = ")"

    override fun consume(node: ASTNode): ConsumerResponse {
        return when (node) {
            is Declaration -> ConsumerResponseSuccess(formatDeclaration(node) + SEMI_COLON)
            is DeclarationInitialization -> ConsumerResponseSuccess(formatInitialization(node) + SEMI_COLON)
            is Assignation -> ConsumerResponseSuccess(formatAssignation(node) + SEMI_COLON)
            is MethodCall -> ConsumerResponseSuccess(formatMethodCall(node) + SEMI_COLON)
            else -> ConsumerResponseError("Could not recognize syntax")
        }
    }

    fun readNode(): ASTNode {
        return when (val response: ASTNProviderResponse = astNodeProvider.readASTNode()) {
            is ASTNProviderResponseSuccess -> response.astNode
            is ASTNProviderResponseError -> throw Exception(response.error)
            else -> throw Exception("Success finish")
        }
    }

    // let x:number;
    private fun formatDeclaration(node: Declaration): String {
        return LET_KEYWORD + getBlankSpaces(config.spacesBetweenTokens) +
            getStringValue(node.identifier) + DOUBLE_DOTS + getStringValue(node.type)
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

    // println("hello world");
    private fun formatMethodCall(node: MethodCall): String {
        return getStringValue(node.identifier) + LEFT_PARENTHESIS + formatValue(node.arguments) + RIGHT_PARENTHESIS
    }

    // (9 + 8) * 7
    private fun formatValue(node: Value): String {
        return if (node.tokenList.isEmpty()) {
            return ""
        } else {
            node.tokenList.joinToString(getBlankSpaces(config.spacesBetweenTokens)) { getStringValue(it) }
        }
    }

    private fun getStringValue(token: Token): String {
        return when (token.type) {
            TokenType.NUMBER_KEYWORD -> "number"
            TokenType.STRING_KEYWORD -> "string"
            TokenType.OPERATOR_PLUS -> "+"
            TokenType.OPERATOR_MINUS -> "-"
            TokenType.OPERATOR_TIMES -> "*"
            TokenType.OPERATOR_DIVIDE -> "/"
            TokenType.STRING_LITERAL -> token.originalValue
            TokenType.NUMBER_LITERAL -> token.originalValue
            TokenType.IDENTIFIER -> token.originalValue
            TokenType.LEFT_PARENTHESIS -> "("
            TokenType.RIGHT_PARENTHESIS -> ")"
            else -> ""
        }
    }

    private fun getBlankSpaces(x: Int): String {
        return " ".repeat(x)
    }
}
