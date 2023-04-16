import ast.node.ASTNode

interface Formatter {
    fun format(node:ASTNode):String
}