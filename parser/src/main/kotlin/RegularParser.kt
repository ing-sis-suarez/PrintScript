import ast_node.*
import token.Token
import token.TokenType

class RegularParser(private val readerList: List<Pair<SyntaxVerifier, ASTTreeConstructor>>): Parser {

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
        for (reader in readerList) {
            if (reader.first.invoke(statement)) {
                return reader.second.invoke(statement)
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
}