package parser

import ast_node.ASTNode
import token.Token

interface Parser {

    fun parse(tokens: List<Token>): List<ASTNode>
}