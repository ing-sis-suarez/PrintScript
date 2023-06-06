package node

import token.Token

interface ASTNode

data class BinaryTokenNode(val token: Token, val right: BinaryTokenNode?, val left: BinaryTokenNode?)
data class Declaration(val identifier: Token, val type: Token) : ASTNode
data class Value(val tree: BinaryTokenNode, val tokenList: List<Token>) : ASTNode
data class DeclarationInitialization(val declaration: Declaration, val value: Value, val isConst: Boolean) : ASTNode
data class MethodCall(val identifier: Token, val arguments: Value) : ASTNode
data class Assignation(val identifier: Token, val value: Value) : ASTNode
data class Condition(val condition: Value?, val ifAction: MutableList<ASTNode>) : ASTNode
