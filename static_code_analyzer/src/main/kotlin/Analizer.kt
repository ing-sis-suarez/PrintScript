import ast_node.ASTNode

interface Analizer {
    fun analize(astNode: ASTNode, variables: MutableMap<String, Pair<String, Boolean>>)
}