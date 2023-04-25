package consumer

import node.ASTNode

interface ASTNodeConsumer {
    fun consume(node: ASTNode): ConsumerResponse
}

interface ConsumerResponse

data class ConsumerResponseSuccess(val msg: String) : ConsumerResponse
data class ConsumerResponseError(val error: String) : ConsumerResponse
class ConsumerResponseEnd : ConsumerResponse
