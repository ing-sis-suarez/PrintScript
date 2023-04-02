import token.*
import ast_node.*

class BinaryOperatorReader(val variables: MutableMap<String, Pair<String, String?>>){

    private fun isLeaf(binary: BinaryTokenNode): Boolean{
        return binary.left == null && binary.right == null
    }
    private fun getValue(token: Token) : Any{
        return when{
            token.type == TokenType.NUMBER_KEYWORD -> token.originalValue.toInt()
            token.type == TokenType.STRING_KEYWORD -> token.originalValue
            else -> {throw IllegalArgumentException("Invalid type at: ${token.location.row}, ${token.location.column}\"")}
        }
    }
    public fun evaluate(binary: BinaryTokenNode): Token{
        if (isLeaf(binary)) return binary.token
        val leftNode = evaluate(binary.left!!)
        val rightNode = evaluate(binary.right!!)
        return operation(leftNode, rightNode, binary.token)
    }

    private fun operation(left: Token, right: Token, op: Token) : Token{
        return when{
            op.type == TokenType.OPERATOR_PLUS -> {
                if (areBothInts(left, right)) operateFunc(left, right, op, ::sum)
                else stringSum(left, right, op.location)
            }
            op.type == TokenType.OPERATOR_MINUS -> operateFunc(left, right, op, ::substract)
            op.type == TokenType.OPERATOR_TIMES -> operateFunc(left, right, op, ::multiply)
            op.type == TokenType.OPERATOR_DIVIDE -> operateFunc(left, right, op, ::divide)

            else -> throw IllegalArgumentException("Unknown operatiom at: ${op.location.row}, ${op.location.column}")
        }
    }

    private fun operateFunc(left: Token, right: Token, op: Token, opFunc: (left: Token, right: Token) -> String) =
        if (areBothInts(left, right)) Token(
            TokenType.NUMBER_LITERAL,
            op.location,
            opFunc(left, right),
            opFunc(left, right).length
        )
        else throw IllegalArgumentException("Invalid type at: ${op.location.row}, ${op.location.column}\"")


    private fun stringSum(left: Token, right: Token, location: Location): Token{
        return Token(
            TokenType.STRING_LITERAL,
            location,
            left.actualValue() + right.actualValue(),
            (left.actualValue() + right.actualValue()).length
        )
    }
    private fun areBothInts(left: Token, right: Token): Boolean{
        return left.type == TokenType.NUMBER_LITERAL && right.type == TokenType.NUMBER_LITERAL
    }

    private fun substract(left: Token, right: Token): String{
        return (left.actualValue().toInt() - right.actualValue().toInt()).toString()
    }
    private fun sum(left: Token, right: Token): String{
        return (left.actualValue().toInt() + right.actualValue().toInt()).toString()
    }

    private fun multiply(left: Token, right: Token): String{
        return (left.actualValue().toInt() * right.actualValue().toInt()).toString()
    }

    private fun divide(left: Token, right: Token): String{
        return (left.actualValue().toInt() / right.actualValue().toInt()).toString()
    }

    private fun operationString(left: String, right: String, op: TokenType) : String{
        return when{
            op == TokenType.OPERATOR_PLUS -> left + right
            else -> throw IllegalArgumentException("Error")
        }
    }

}