package utilities

data class ASTNode(val children: List<ASTNode>, val token: Token?, val astNodeType: ASTNodeType)

enum class ASTNodeType {
    TOKEN,
    INITIALIZATION,
    DECLARATION,
    OPERATION,
    FUNCTION_CALL,
    ASSIGNATION,
    EXECUTION
}