package node

import token.Token

sealed interface ASTNode

data class Declaration(val identifier: Token, val type: Token) : ASTNode
data class Value(val tree: BinaryTokenNode, val tokenList: List<Token>) : ASTNode
data class DeclarationInitialization(val declaration: Declaration, val value: Value, val isConst: Boolean) : ASTNode
data class MethodCall(val identifier: Token, val arguments: Value) : ASTNode, BinaryTokenNode
data class Assignation(val identifier: Token, val value: Value) : ASTNode
data class Condition(val condition: Value?, val ifAction: MutableList<ASTNode>) : ASTNode
sealed interface BinaryTokenNode
data class BinaryOperator(val op: Token, val right: BinaryTokenNode, val left: BinaryTokenNode) : BinaryTokenNode
data class NumericOperator(val value: Token) : BinaryTokenNode
data class StringOperator(val value: Token) : BinaryTokenNode
data class BooleanOperator(val value: Token) : BinaryTokenNode
data class IdentifierOperator(val value: Token) : BinaryTokenNode
