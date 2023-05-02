import consumer.ConsumerResponse
import node.ASTNode

interface Analyzer {
    fun analyze(astNode: ASTNode): ConsumerResponse
}
