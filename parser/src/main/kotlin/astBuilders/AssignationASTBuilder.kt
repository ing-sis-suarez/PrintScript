package astBuilders

import ast.node.Assignation
import astBuilders.ASTBuilder.Companion.checkTokenType
import astBuilders.ASTBuilder.Companion.takeWhiteSpacesCommentsAndSemiColon
import token.Token
import token.TokenType

class AssignationASTBuilder : ASTBuilder<Assignation> {

    private val valueBuilder = ValueASTBuilder()
    override fun isApplicable(statement: List<Token>): Boolean {
        val parsedStatements = takeWhiteSpacesCommentsAndSemiColon(statement)
        if (parsedStatements.size < 2) return false
        return parsedStatements[1].type == TokenType.ASIGNATION_EQUALS
    }

    override fun buildAST(statement: List<Token>): Assignation {
        val parsedStatements = takeWhiteSpacesCommentsAndSemiColon(statement)
        checkAssignation(parsedStatements)
        val value = valueBuilder.buildAST(parsedStatements.subList(2, parsedStatements.size))
        return Assignation(parsedStatements[0], value)
    }

    private fun checkAssignation(statement: List<Token>) {
        checkTokenType(statement[0], "Identifier", listOf(TokenType.IDENTIFIER))
        checkTokenType(statement[1], "=", listOf(TokenType.ASIGNATION_EQUALS))
    }
}
