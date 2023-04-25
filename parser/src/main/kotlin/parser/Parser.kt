package parser

import node.ASTNode
import token.Token

interface Parser {

    fun parse(tokens: List<Token>): ASTNode
}
