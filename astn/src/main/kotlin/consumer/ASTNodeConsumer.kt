package consumer

interface ASTNodeConsumer {
    fun consume(): ConsumerResponse
}

interface ConsumerResponse

data class ConsumerResponseSuccess(val msg: String?) : ConsumerResponse
data class ConsumerResponseError(val error: String) : ConsumerResponse
class ConsumerResponseEnd : ConsumerResponse
