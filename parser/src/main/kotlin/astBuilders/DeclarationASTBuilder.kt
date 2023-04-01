package astBuilders

import ast_node.Declaration
import astBuilders.ASTBuilder.Companion.checkDoubleDots
import astBuilders.ASTBuilder.Companion.checkIdentifier
import astBuilders.ASTBuilder.Companion.checkLength
import astBuilders.ASTBuilder.Companion.checkLetKeyword
import astBuilders.ASTBuilder.Companion.checkType
import token.Token
import token.TokenType

class DeclarationASTBuilder: ASTBuilder<Declaration> {
    override fun isApplicable(statement: List<Token>): Boolean {
        return statement[0].type == TokenType.LET_KEYWORD || statement[2].type == TokenType.DOUBLE_DOTS
    }

    override fun buildAST(statement: List<Token>): Declaration {
        checkDeclaration(statement)
        return Declaration(statement[1], statement[3])
    }

    private fun checkDeclaration(statement: List<Token>) {
        checkLength(statement, 4, "declaration")
        checkLetKeyword(statement[0])
        checkIdentifier(statement[1])
        checkDoubleDots(statement[2])
        checkType(statement[3])
    }


}
