package Evaluator

import ast.node.BinaryTokenNode
import token.Location
import token.Token
import token.TokenType

class BinaryOperatorReader(private val variables: MutableMap<String, Pair<String, String?>>) {
    private fun isLeaf(binary: BinaryTokenNode): Boolean {
        return binary.left == null && binary.right == null
    }
    fun getValue(token: Token): Any {
        return when (token.type) {
            TokenType.NUMBER_LITERAL -> token.actualValue().toDouble()
            TokenType.STRING_LITERAL -> token.actualValue()
            TokenType.IDENTIFIER -> {
                if (variables.containsKey(token.actualValue()) && variables[token.actualValue()]!!.second != null) {
                    return if (variables[token.actualValue()]!!.first == "String") {
                        variables[token.actualValue()]!!.second!!
                    } else {
                        variables[token.actualValue()]!!.second!!.toDouble()
                    }
                } else {
                    throw java.lang.IllegalArgumentException("Variable not initialized")
                }
            }
            else -> { throw IllegalArgumentException("Error") }
        }
    }
    fun evaluate(binary: BinaryTokenNode): Any {
        if (isLeaf(binary)) {
            return getValue(binary.token)
        }
        val leftValue = evaluate(binary.left!!)
        val rightValue = evaluate(binary.right!!)
        return when {
            leftValue is String || rightValue is String -> operationString(leftValue.toString(), rightValue.toString(), binary.token.type, binary.token.location)
            leftValue is Double && rightValue is Double -> operation(leftValue, rightValue, binary.token.type, binary.token.location)
            else -> throw IllegalArgumentException("Tipos incompatibles: $leftValue, $rightValue")
        }
    }
    private fun operation(left: Double, right: Double, op: TokenType, location: Location): Double {
        return when (op) {
            TokenType.OPERATOR_PLUS -> left + right
            TokenType.OPERATOR_MINUS -> left - right
            TokenType.OPERATOR_TIMES -> left * right
            TokenType.OPERATOR_DIVIDE -> left / right
            else -> throw InvalidTypeException("$op is invalid with Number operations in line ${location.row} ${location.column}")
        }
    }
    private fun operationString(left: String, right: String, op: TokenType, location: Location): String {
        return when (op) {
            TokenType.OPERATOR_PLUS -> left + right
            else -> throw InvalidTypeException("$op is invalid with Number operations in line ${location.row} ${location.column}")
        }
    }
    fun getValueType(value: BinaryTokenNode, variables: Map<String, Pair<String, String?>>): String {
        var valueType = "Number"
        fun dfs(value: BinaryTokenNode) {
            if (isLeaf(value)) {
                if (value.token.type == TokenType.IDENTIFIER && variables.containsKey(value.token.actualValue()) && variables[value.token.actualValue()]!!.second!! == "String") {
                    valueType = "String"
                    return
                }
                if (value.token.type == TokenType.STRING_LITERAL) {
                    valueType = "String"
                    return
                }
            } else {
                dfs(value.left!!)
                dfs(value.right!!)
            }
        }
        dfs(value)
        return valueType
    }
}
