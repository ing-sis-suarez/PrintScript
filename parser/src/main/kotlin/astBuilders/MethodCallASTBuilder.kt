package astBuilders

import ast.node.MethodCall
import astBuilders.ASTBuilder.Companion.checkMinLength
import astBuilders.ASTBuilder.Companion.checkTokenType
import astBuilders.ASTBuilder.Companion.takeWhiteSpacesCommentsAndSemiColon
import token.Token
import token.TokenType

class MethodCallASTBuilder : ASTBuilder<MethodCall> {

    private val valueBuilder = ValueASTBuilder()
    override fun isApplicable(statement: List<Token>): Boolean {
        val parsedStatements = takeWhiteSpacesCommentsAndSemiColon(statement)
        return parsedStatements[0].type == TokenType.IDENTIFIER &&
                parsedStatements[1].type == TokenType.LEFT_PARENTHESIS &&
                parsedStatements[parsedStatements.size - 1].type == TokenType.RIGHT_PARENTHESIS
    }

    override fun buildAST(statement: List<Token>): MethodCall {
        val parsedStatements = takeWhiteSpacesCommentsAndSemiColon(statement)
        checkMethodCall(parsedStatements)
        val parameterValue = valueBuilder.buildAST(parsedStatements.subList(2, parsedStatements.size - 1))
        return MethodCall(parsedStatements[0], parameterValue)
    }

    private fun checkMethodCall(statement: List<Token>) {
        checkMinLength(statement)
        checkTokenType(statement[0], "Identifier", listOf(TokenType.IDENTIFIER))
        checkTokenType(statement[1], "Left parenthesis", listOf(TokenType.LEFT_PARENTHESIS))
        checkTokenType(statement[statement.size - 1], "Right parenthesis", listOf(TokenType.RIGHT_PARENTHESIS))
    }
}
