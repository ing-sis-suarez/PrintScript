package parser

import astBuilders.*
import exceptions.MalformedStructureException
import ast_node.*
import token.Token
import token.TokenType

class RegularParser(private val astBuilderList: List<ASTBuilder<ASTNode>>): Parser {

    override fun parse(tokens: List<Token>): List<ASTNode> {
        val cleanedTokens = takeWhiteSpacesAndComments(tokens)
        val statements = breakIntoStatements(cleanedTokens, TokenType.SEMI_COLON)
        return generateChildren(statements)
    }

    private fun generateChildren(statements: List<List<Token>>): List<ASTNode> {
        return statements.map {statement -> createChild(statement)}
    }

    private fun createChild(
        statement: List<Token>,
    ): ASTNode {
        for (builder in astBuilderList) {
            if (builder.isApplicable(statement)) {
                return builder.buildAST(statement)
            }
        }
        throw MalformedStructureException("Could not recognize syntax")
    }

    private fun takeWhiteSpacesAndComments(tokens: List<Token>): List<Token> {
        return tokens.filter { token -> !(token.type == TokenType.WHITE_SPACE || token.type == TokenType.COMMENT) }
    }

    private fun breakIntoStatements(tokens: List<Token>, separator: TokenType): List<List<Token>>{
        var lastIndex = 0
        val statements: MutableList<List<Token>> = mutableListOf()
        for (i in tokens.indices){
            if (tokens[i].type == separator){
                statements.add(tokens.subList(lastIndex, i))
                lastIndex = i + 1
            }
        }
        return statements
    }

    companion object{
        fun createDefaultParser(): RegularParser {
            return RegularParser(listOf(
                DeclarationInitializationASTBuilder(),
                MethodCallASTBuilder(),
                AssignationASTBuilder(),
                DeclarationASTBuilder()
            ))
        }
    }
}