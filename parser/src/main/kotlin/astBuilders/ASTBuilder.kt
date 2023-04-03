package astBuilders

import ast.node.ASTNode
import exceptions.MalformedStructureException
import exceptions.UnexpectedTokenException
import token.Token
import token.TokenType

interface ASTBuilder<out T : ASTNode> {

    fun isApplicable(statement: List<Token>): Boolean

    fun buildAST(statement: List<Token>): T

    companion object {
        fun checkLength(statement: List<Token>, correctSize: Int, structureName: String) {
            if (statement.size < correctSize) throw MalformedStructureException("Malformed $structureName at ${statement[0].location.row} ")
            if (statement.size > correctSize) throw UnexpectedTokenException("Unexpected token at: ${statement[correctSize].location.row}, ${statement[correctSize].location.column}")
        }

        fun checkTokenType(token: Token, s: String, types: List<TokenType>) {
            if (!types.contains(token.type)) {
                throw UnexpectedTokenException("$s expected at: ${token.location.row}, ${token.location.column}")
            }
        }

        fun checkMinLength(statement: List<Token>) {
            if (statement.size < 4) throw MalformedStructureException("Missing arguments at line: ${statement[0].location.row}")
        }
    }
}
