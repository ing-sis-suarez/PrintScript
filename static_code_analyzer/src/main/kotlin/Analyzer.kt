import ast.node.ASTNode

interface Analyzer {
    fun analyze(astNode: ASTNode)
}
