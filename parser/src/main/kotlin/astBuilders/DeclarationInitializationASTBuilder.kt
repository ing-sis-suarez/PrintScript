package astBuilders

import ast.node.DeclarationInitialization
import astBuilders.ASTBuilder.Companion.takeWhiteSpacesCommentsAndSemiColon
import token.Token
import token.TokenType


class DeclarationInitializationASTBuilder : ASTBuilder<DeclarationInitialization> {

    private val declarationBuilder = DeclarationASTBuilder()
    private val valueBuilder = ValueASTBuilder()
    override fun isApplicable(statement: List<Token>): Boolean {
        val parsedStatements = takeWhiteSpacesCommentsAndSemiColon(statement)
        if (parsedStatements.size < 5) return false
        return parsedStatements[4].type == TokenType.ASIGNATION_EQUALS
    }

    override fun buildAST(statement: List<Token>): DeclarationInitialization {
        val parsedStatements = takeWhiteSpacesCommentsAndSemiColon(statement)
        val declaration = declarationBuilder.buildAST(parsedStatements.subList(0, 4))
        val value = valueBuilder.buildAST(parsedStatements.subList(5, parsedStatements.size))
        return DeclarationInitialization(declaration, value)
    }
}
