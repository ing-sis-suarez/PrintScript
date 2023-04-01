package astBuilders

import astBuilders.ASTBuilder.Companion.checkIdentifier
import astBuilders.ASTBuilder.Companion.checkLeftParenthesis
import astBuilders.ASTBuilder.Companion.checkMinLength
import astBuilders.ASTBuilder.Companion.checkRightParenthesis
import ast_node.MethodCall
import token.Token
import token.TokenType

class MethodCallASTBuilder: ASTBuilder<MethodCall> {

    private val valueBuilder = ValueASTBuilder()
    override fun isApplicable(statement: List<Token>): Boolean {
        return statement[0].type == TokenType.IDENTIFIER &&
                statement[1].type == TokenType.LEFT_PARENTHESIS &&
                statement[statement.size - 1].type == TokenType.RIGHT_PARENTHESIS
    }

    override fun buildAST(statement: List<Token>): MethodCall {
        checkMethodCall(statement)
        val parameterValue = valueBuilder.buildAST(statement.subList(2, statement.size - 1))
        return MethodCall(statement[0], parameterValue)
    }

    private fun checkMethodCall(statement: List<Token>) {
        checkMinLength(statement)
        checkIdentifier(statement[0])
        checkLeftParenthesis(statement[1])
        checkRightParenthesis(statement[statement.size - 1])
    }
}