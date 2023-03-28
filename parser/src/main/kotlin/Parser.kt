import ast_node.ASTNode
import sun.tools.jstat.Token

interface Parser {

    fun parse(tokens: List<Token>): List<ASTNode>
}