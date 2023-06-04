package astBuilders

import astBuilders.ASTBuilder.Companion.checkLength
import astBuilders.ASTBuilder.Companion.checkTokenType
import astBuilders.ASTBuilder.Companion.takeWhiteSpacesCommentsAndSemiColon
import node.Declaration
import token.Token
import token.TokenType

class DeclarationASTBuilder : ASTBuilder<Declaration> {
    override fun isApplicable(statement: List<Token>): Boolean {
        val parsedStatements = takeWhiteSpacesCommentsAndSemiColon(statement)
        return parsedStatements[0].type == TokenType.LET_KEYWORD || parsedStatements[2].type == TokenType.DOUBLE_DOTS
    }

    override fun buildAST(statement: List<Token>): Declaration {
        val parsedStatements = takeWhiteSpacesCommentsAndSemiColon(statement)
        checkDeclaration(parsedStatements)
        return Declaration(parsedStatements[1], parsedStatements[3])
    }

    private fun checkDeclaration(statement: List<Token>) {
        checkLength(statement, 4, "declaration")
        checkTokenType(statement[0], "Let or const", listOf(TokenType.LET_KEYWORD, TokenType.CONST_KEYWORD))
        checkTokenType(statement[1], "Identifier", listOf(TokenType.IDENTIFIER))
        checkTokenType(statement[2], "Double dots", listOf(TokenType.DOUBLE_DOTS))
        checkTokenType(statement[3], "Type", listOf(TokenType.NUMBER_KEYWORD, TokenType.STRING_KEYWORD, TokenType.BOOLEAN_KEYWORD))
    }
}
