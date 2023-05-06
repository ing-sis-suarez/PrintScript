package interpreter

import node.BinaryTokenNode
import token.Token
import token.TokenType

class BinaryOperatorReader(private val variables: MutableMap<String, Pair<String, String?>>) {
    private fun isLeaf(binary: BinaryTokenNode): Boolean {
        return binary.left == null && binary.right == null
    }
    private fun getValue(token: Token): Pair<String, String> {
        return when (token.type) {
            TokenType.NUMBER_LITERAL -> Pair("Number", token.actualValue())
            TokenType.STRING_LITERAL -> Pair("String", token.actualValue())
            TokenType.IDENTIFIER -> {
                if (variables.containsKey(token.actualValue()) && variables[token.actualValue()]!!.second != null) {
                    return if (variables[token.actualValue()]!!.first == "String") {
                        Pair("String", variables[token.actualValue()]!!.second!!)
                    } else {
                        Pair("Number", variables[token.actualValue()]!!.second!!)
                    }
                } else {
                    return Pair("Error", "Variable not initialized")
                }
            }
            else -> { return Pair("Error", "Unexpected exception") }
        }
    }
    fun evaluate(binary: BinaryTokenNode): Pair<String, String> {
        if (isLeaf(binary)) {
            return getValue(binary.token)
        }
        val leftValue = evaluate(binary.left!!)
        val rightValue = evaluate(binary.right!!)
        return when {
            leftValue.first == "Error" -> {
                return leftValue
            }
            rightValue.first == "Error" -> {
                return rightValue
            }
            leftValue.first == "String" || rightValue.first == "String" -> {
                if (binary.token.type == TokenType.OPERATOR_PLUS) {
                    operationString(leftValue.second, rightValue.second, binary.token.type)
                } else {
                    Pair("Error", "${binary.token.type} is invalid with Number operations in line ${binary.token.location.row} ${binary.token.location.column}")
                }
            }
            leftValue.first == "Number" && rightValue.first == "Number" -> {
                if (binary.token.type == TokenType.OPERATOR_PLUS || binary.token.type == TokenType.OPERATOR_MINUS || binary.token.type == TokenType.OPERATOR_TIMES || binary.token.type == TokenType.OPERATOR_DIVIDE) {
                    operation(leftValue.second.toDouble(), rightValue.second.toDouble(), binary.token.type)
                } else {
                    Pair("Error", "${binary.token.type} is invalid with Number operations in line ${binary.token.location.row} ${binary.token.location.column}")
                }
            }
            else -> {
                Pair("Error", "Unexpected expression")
            }
        }
    }
    private fun operation(left: Double, right: Double, op: TokenType): Pair<String, String> {
        return when (op) {
            TokenType.OPERATOR_PLUS -> Pair("Number", (left + right).toString())
            TokenType.OPERATOR_MINUS -> Pair("Number", (left - right).toString())
            TokenType.OPERATOR_TIMES -> Pair("Number", (left * right).toString())
            TokenType.OPERATOR_DIVIDE -> Pair("Number", (left / right).toString())
            else -> throw Exception("unexpected")
        }
    }
    private fun operationString(left: String, right: String, op: TokenType): Pair<String, String> {
        return when (op) {
            TokenType.OPERATOR_PLUS -> Pair("String", (left + right))
            else -> throw Exception("unexpected")
        }
    }
}
