import token.*
import ast_node.*

class BinaryOperatorReader(val variables: MutableMap<String, Pair<String, String?>>){

    fun isLeaf(binary: BinaryTokenNode): Boolean{
        return binary.left == null && binary.right == null
    }
    fun getValue(token: Token) : Any{
        return when{
            token.type == TokenType.NUMBER_KEYWORD -> token.originalValue.toInt()
            token.type == TokenType.STRING_KEYWORD -> token.originalValue
            else -> {throw IllegalArgumentException("Error")}
        }
    }
    public fun evaluate(binary: BinaryTokenNode): Any{
        if (isLeaf(binary)){
            return getValue(binary.token)
        }
        val leftValue = binary.left?.let { evaluate(it) }
        val rightValue = binary.right?.let { evaluate(it) }
        return when {
            leftValue is String && rightValue is String -> operationString(leftValue, rightValue, binary.token.type)
            leftValue is Int && rightValue is Int -> operation(leftValue, rightValue, binary.token.type)
            leftValue is String && rightValue is Int -> operationString(leftValue, rightValue.toString(), binary.token.type)
            leftValue is Int && rightValue is String -> operationString(leftValue.toString(), rightValue, binary.token.type)
            else -> throw IllegalArgumentException("Tipos incompatibles: $leftValue, $rightValue")
        }
    }

    fun operation(left: Int, right: Int, op: TokenType) : Int{
        return when{
            op == TokenType.OPERATOR_PLUS -> left + right
            op == TokenType.OPERATOR_MINUS -> left - right
            op == TokenType.OPERATOR_TIMES -> left * right
            op == TokenType.OPERATOR_DIVIDE -> left / right

            else -> throw IllegalArgumentException("Error")
        }
    }

    fun operationString(left: String, right: String, op: TokenType) : String{
        return when{
            op == TokenType.OPERATOR_PLUS -> left + right
            else -> throw IllegalArgumentException("Error")
        }
    }

}