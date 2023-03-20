import utilities.ASTNode
import utilities.Token
import utilities.TokenType

class RegularParser: Parser {

    override fun parse(tokens: List<Token>): ASTNode {
        val cleanedTokens = takeWhiteSpacesandComments(tokens)
        val statements = breakIntoStatements(cleanedTokens, TokenType.SEMI_COLON)
    }

    private fun takeWhiteSpacesandComments(tokens: List<Token>): List<Token> {
        val cleanedTokens = mutableListOf<Token>()
        tokens.forEach { token -> if (!(token.type == TokenType.WHITE_SPACE || token.type == TokenType.COMMENT)) cleanedTokens.add(token)}
        return cleanedTokens
    }

    fun breakIntoStatements(tokens: List<Token>, separator: TokenType): List<List<Token>>{
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
}