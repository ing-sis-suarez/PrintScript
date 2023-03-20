import utilities.ASTNode
import utilities.Token

interface Parser {

    fun parse(tokens: List<Token>): ASTNode
}