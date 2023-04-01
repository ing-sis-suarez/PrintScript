package astBuilders

import astBuilders.ASTBuilder.Companion.checkEquals
import ast_node.Assignation
import astBuilders.ASTBuilder.Companion.checkIdentifier
import token.Token
import token.TokenType

class AssignationASTBuilder: ASTBuilder<Assignation> {

    private val valueBuilder = ValueASTBuilder()
    override fun isApplicable(statement: List<Token>): Boolean {
        if (statement.size < 2) return false
        return statement[1].type == TokenType.ASIGNATION_EQUALS
    }

    override fun buildAST(statement: List<Token>): Assignation {
        checkAssignation(statement)
        val value = valueBuilder.buildAST(statement.subList(2, statement.size))
        return Assignation(statement[0], value)
    }

    private fun checkAssignation(statement: List<Token>) {
        checkIdentifier(statement[0])
        checkEquals(statement[1])
    }
}