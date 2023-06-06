package astBuilders

import exceptions.MalformedStructureException
import node.ASTNode
import node.Condition
import parser.RegularParser
import token.Token
import token.TokenType

class ConditionASTBuilder : ASTBuilder<Condition> {

    private val valueBuilder = ValueASTBuilder()

    override fun isApplicable(statement: List<Token>): Boolean {
        val parsedStatements = ASTBuilder.takeWhiteSpacesCommentsAndSemiColon(statement)
        if (parsedStatements[0].type == TokenType.IF_KEYWORD) {
            return parsedStatements[1].type == TokenType.LEFT_PARENTHESIS &&
                parsedStatements[3].type == TokenType.RIGHT_PARENTHESIS &&
                parsedStatements[4].type == TokenType.LEFT_BRACE &&
                parsedStatements[parsedStatements.size - 1].type == TokenType.RIGHT_BRACE
        }
        if (parsedStatements[0].type == TokenType.ELSE_KEYWORD) {
            return parsedStatements[1].type == TokenType.LEFT_BRACE
        }
        return false
    }

    override fun buildAST(statement: List<Token>): Condition {
        val parsedList = takeWhiteSpacesComments(statement)
        if (parsedList[0].type == TokenType.IF_KEYWORD) {
            return Condition(valueBuilder.buildAST(parsedList.subList(2, 3)), astListBuilder(parsedList.subList(5, parsedList.size - 1)))
        }
        return Condition(null, astListBuilder(parsedList.subList(2, statement.size - 1)))
    }

    private fun astListBuilder(list: List<Token>): MutableList<ASTNode> {
        val astnList = mutableListOf<ASTNode>()
        val tokenList = mutableListOf<Token>()
        val parser = RegularParser.createDefaultParser()
        var braceCount = 0
        for (token in list) {
            if (token.type == TokenType.SEMICOLON && braceCount == 0) {
                astnList.add(parser.parse(tokenList))
                tokenList.clear()
                continue
            }
            if (token.type == TokenType.LEFT_BRACE) {
                braceCount += 1
            }
            if (token.type == TokenType.RIGHT_BRACE) {
                braceCount -= 1
                if (braceCount == 0) {
                    tokenList.add(token)
                    astnList.add(parser.parse(tokenList))
                    tokenList.clear()
                    continue
                }
            }
            tokenList.add(token)
        }
        if (tokenList.size != 0 && tokenList[0].type != TokenType.RIGHT_BRACE) {
            throw MalformedStructureException("Unexpected end of condition at: ${tokenList[tokenList.size - 1].location.row} ${tokenList[tokenList.size - 1].location.column}")
        } else {
            return astnList
        }
    }
    fun takeWhiteSpacesComments(tokens: List<Token>): List<Token> {
        return tokens.filter { token ->
            !(
                token.type == TokenType.WHITE_SPACE ||
                    token.type == TokenType.COMMENT
                )
        }
    }
}
