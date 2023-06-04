package consumer

interface ASTNodeConsumer {
    fun consume(): ConsumerResponse
}

interface ASTNodeConsumerInterpreter {
    fun consume(): ConsumerResponse

    fun getValue(resp: String)
}

interface ConsumerResponse

data class ConsumerResponseSuccess(val msg: String?) : ConsumerResponse
data class ConsumerResponseError(val error: String) : ConsumerResponse
data class ConsumerResponseImput(val msg: String) : ConsumerResponse
class ConsumerResponseEnd : ConsumerResponse
