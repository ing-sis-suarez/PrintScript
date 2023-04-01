package astBuilders

import ast_node.DeclarationInitialization
import token.Token
import token.TokenType

class DeclarationInitializationASTBuilder : ASTBuilder<DeclarationInitialization> {

    private val declarationBuilder = DeclarationASTBuilder()
    private val valueBuilder = ValueASTBuilder()
    override fun isApplicable(statement: List<Token>): Boolean {
        if (statement.size < 5) return false
        return statement[4].type == TokenType.ASIGNATION_EQUALS
    }

    override fun buildAST(statement: List<Token>): DeclarationInitialization {
        val declaration = declarationBuilder.buildAST(statement.subList(0, 4))
        val value = valueBuilder.buildAST(statement.subList(5, statement.size))
        return DeclarationInitialization(declaration, value)
    }
}