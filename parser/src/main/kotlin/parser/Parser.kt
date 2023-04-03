package parser

import ast.node.ASTNode
import token.Token

interface Parser {

    fun parse(tokens: List<Token>): List<ASTNode>
}
