import token.*
import ast_node.*

class BinaryOperatorReader(val variables: MutableMap<String, Pair<String, String?>>){

    fun isLeaf(binary: BinaryTokenNode): Boolean{
        return binary.left == null && binary.right == null
    }
    fun getValue(token: Token) : Any{
         return when{
            token.type == TokenType.NUMBER_LITERAL -> token.originalValue.toInt()
            token.type == TokenType.STRING_LITERAL -> token.originalValue
            token.type == TokenType.IDENTIFIER ->{
                if (variables.containsKey(token.actualValue()) && variables.get(token.actualValue())!!.second != null){
                    return if (variables.get(token.actualValue())!!.first.equals("String")){
                        variables.get(token.actualValue())!!.second!!
                    } else {
                        variables.get(token.actualValue())!!.second!!.toInt()
                    }
                }else{
                    throw java.lang.IllegalArgumentException("")
                }
            }
            else -> {throw IllegalArgumentException("Error")}
        }
    }
    public fun evaluate(binary: BinaryTokenNode): Any{
        if (isLeaf(binary)){
            return getValue(binary.token)
        }
        val leftValue = evaluate(binary.left!!)
        val rightValue = evaluate(binary.right!!)
        return when {
            leftValue is String || rightValue is String -> operationString(leftValue.toString(), rightValue.toString(), binary.token.type, binary.token.location)
            leftValue is Int && rightValue is Int -> operation(leftValue, rightValue, binary.token.type, binary.token.location)
            else -> throw IllegalArgumentException("Tipos incompatibles: $leftValue, $rightValue")
        }
    }

    fun operation(left: Int, right: Int, op: TokenType, location: Location) : Int{
        return when{
            op == TokenType.OPERATOR_PLUS -> left + right
            op == TokenType.OPERATOR_MINUS -> left - right
            op == TokenType.OPERATOR_TIMES -> left * right
            op == TokenType.OPERATOR_DIVIDE -> left / right

            else -> throw InvalidTypeException("${op} is invalid with Number operations in line ${location.row} ${location.column}")
        }
    }

    fun operationString(left: String, right: String, op: TokenType, location: Location) : String{
        return when{
            op == TokenType.OPERATOR_PLUS -> left + right
            else -> throw InvalidTypeException("${op} is invalid with Number operations in line ${location.row} ${location.column}")
        }
    }

    fun getValueType(value: BinaryTokenNode): String{
        var valueType: String = "Number"
        fun dfs(value: BinaryTokenNode){
            if (isLeaf(value)){
                if (value.token.type != TokenType.NUMBER_LITERAL){
                    valueType = "String"
                    return
                }
            }else{
                dfs(value.left!!)
                dfs(value.right!!)
            }
        }
        dfs(value)
        return  valueType
    }

}