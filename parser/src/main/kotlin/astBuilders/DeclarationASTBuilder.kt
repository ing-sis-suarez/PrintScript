package astBuilders

import ast.node.Declaration
import astBuilders.ASTBuilder.Companion.checkLength
import astBuilders.ASTBuilder.Companion.checkTokenType
import token.Token
import token.TokenType

class DeclarationASTBuilder : ASTBuilder<Declaration> {
    override fun isApplicable(statement: List<Token>): Boolean {
        return statement[0].type == TokenType.LET_KEYWORD || statement[2].type == TokenType.DOUBLE_DOTS
    }

    override fun buildAST(statement: List<Token>): Declaration {
        checkDeclaration(statement)
        return Declaration(statement[1], statement[3])
    }

    private fun checkDeclaration(statement: List<Token>) {
        checkLength(statement, 4, "declaration")
        checkTokenType(statement[0], "Let", listOf(TokenType.LET_KEYWORD))
        checkTokenType(statement[1], "Identifier", listOf(TokenType.IDENTIFIER))
        checkTokenType(statement[2], "Double dots", listOf(TokenType.DOUBLE_DOTS))
        checkTokenType(statement[3], "Type", listOf(TokenType.NUMBER_KEYWORD, TokenType.STRING_KEYWORD))
    }
}
