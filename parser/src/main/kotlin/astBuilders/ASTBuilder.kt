package astBuilders

import ast_node.ASTNode
import exceptions.MalformedStructureException
import exceptions.UnexpectedTokenException
import token.Token
import token.TokenType

interface ASTBuilder<out R: ASTNode> {

    fun isApplicable(statement: List<Token>): Boolean

    fun buildAST(statement: List<Token>): R

    companion object{
        fun checkLength(statement: List<Token>, correctSize: Int, structureName: String) {
            if (statement.size < correctSize) throw MalformedStructureException("Malformed $structureName at ${statement[0].location.row} ")
            if (statement.size > correctSize) throw UnexpectedTokenException("Unexpected token at: ${statement[4].location.row}, ${statement[4].location.column}")
        }

        fun checkLetKeyword(token: Token) {
            if (token.type != TokenType.LET_KEYWORD)
                throw UnexpectedTokenException("Let expected at: ${token.location.row}, ${token.location.column}")
        }
        fun checkIdentifier(token: Token) {
            if (token.type != TokenType.IDENTIFIER)
                throw UnexpectedTokenException("Identifier expected at: ${token.location.row}, ${token.location.column}")
        }

        fun checkDoubleDots(token: Token) {
            if (token.type != TokenType.DOUBLE_DOTS)
                throw UnexpectedTokenException("Double dots expected at: ${token.location.row}, ${token.location.column}")
        }

        fun checkType(token: Token) {
            if (token.type != TokenType.NUMBER_KEYWORD && token.type != TokenType.STRING_KEYWORD)
                throw UnexpectedTokenException("Type expected at: ${token.location.row}, ${token.location.column}")
        }

        fun checkEquals(token: Token) {
            if (token.type != TokenType.ASIGNATION_EQUALS)
                throw UnexpectedTokenException("'=' expected at: ${token.location.row}, ${token.location.column}")
        }

        fun checkMinLength(statement: List<Token>) {
            if (statement.size < 4) throw MalformedStructureException("Missing arguments at line: ${statement[0].location.row}")
        }

        fun checkRightParenthesis(token: Token) {
            if (token.type != TokenType.RIGHT_PARENTHESIS)
                throw UnexpectedTokenException("')' expected at: ${token.location.row}, ${token.location.column}")
        }

        fun checkLeftParenthesis(token: Token) {
            if (token.type != TokenType.LEFT_PARENTHESIS)
                throw UnexpectedTokenException("'(' expected at: ${token.location.row}, ${token.location.column}")
        }
    }
}