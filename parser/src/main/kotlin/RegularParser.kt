import utilities.ASTNode
import utilities.ASTNodeType
import utilities.Token
import utilities.TokenType

class RegularParser(private val readerList: List<Pair<SyntaxVerifier, ASTTreeConstructor>>): Parser {

    override fun parse(tokens: List<Token>): ASTNode {
        val cleanedTokens = takeWhiteSpacesandComments(tokens)
        val statements = breakIntoStatements(cleanedTokens, TokenType.SEMI_COLON)
        return createTree(statements)
    }

    private fun createTree(statements: List<List<Token>>): ASTNode {
        val children = generateChildren(statements)
        return ASTNode(children, null, ASTNodeType.EXECUTION)
    }

    private fun generateChildren(
        statements: List<List<Token>>,
    ): List<ASTNode> {
        val children = mutableListOf<ASTNode>()
        for (statement in statements) {
            addChild(statement, children)
        }
        return children
    }

    private fun addChild(
        statement: List<Token>,
        children: MutableList<ASTNode>
    ) {
        var match = false
        for (reader in readerList) {
            if (reader.first.invoke(statement)) {
                children.add(reader.second.invoke(statement))
                match = true
                break
            }
        }
        if (!match) throw MalformedStructureException("Could not recognize syntax")
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