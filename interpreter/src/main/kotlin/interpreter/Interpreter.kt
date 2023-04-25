package interpreter

interface Interpreter {
    fun interpret(): InterpreterResponse
}
interface InterpreterResponse

data class InterpreterSuccessResponse(val message: String?) : InterpreterResponse
data class InterpreterFailResponse(val error: String) : InterpreterResponse
class InterpreterEndResponse : InterpreterResponse
