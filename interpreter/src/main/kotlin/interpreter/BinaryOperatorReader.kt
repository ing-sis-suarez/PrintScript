package interpreter

import node.BinaryOperator
import node.BinaryTokenNode
import node.BooleanOperator
import node.IdentifierOperator
import node.MethodCall
import node.NumericOperator
import node.StringOperator
import token.Location
import token.Token
import token.TokenType

class BinaryOperatorReader(private val variables: VariableManager) {
    var inputResults: MutableMap<Location, Variable> = HashMap()
    var inputLocation: Location? = null
    var inputType: ValueType? = null
    var msgInput: String? = null

    private fun readImput(methodCall: MethodCall): Variable {
        if (inputResults.containsKey(methodCall.identifier.location) && inputResults[methodCall.identifier.location]!!.getValue() != null) {
            if (inputResults[methodCall.identifier.location]!!.getValue() != null) {
                val result = inputResults[methodCall.identifier.location]!!
                return result
            } else { throw Exception("Invalid null value in line ${methodCall.identifier.location.row} ${methodCall.identifier.location.column}") }
        } else {
            inputLocation = methodCall.identifier.location
            return Variable(evaluate(methodCall.arguments.tree).getValue(), ValueType.INPUT)
        }
    }
    private fun identifier(token: Token): Variable {
        return if (variables.contains(token.actualValue()) && variables.get(token.actualValue()).getValue() != null) {
            variables.get(token.actualValue())
        } else {
            throw Exception("Variable not initialized in line ${token.location.row} ${token.location.column}")
        }
    }
    fun evaluate(binary: BinaryTokenNode): Variable {
        return when (binary) {
            is NumericOperator -> Variable(binary.value.actualValue(), ValueType.NUMBER)
            is StringOperator -> Variable(binary.value.actualValue(), ValueType.STRING)
            is BooleanOperator -> Variable(binary.value.actualValue(), ValueType.BOOLEAN)
            is IdentifierOperator -> identifier(binary.value)
            is BinaryOperator -> calculateOperation(binary)
            is MethodCall -> readImput(binary)
        }
    }
    private fun calculateOperation(binaryOperator: BinaryOperator): Variable {
        val leftValue = evaluate(binaryOperator.left)
        if (leftValue.getType() == ValueType.INPUT) return leftValue
        val rightValue = evaluate(binaryOperator.right)
        if (rightValue.getType() == ValueType.INPUT) return rightValue
        return when {
            leftValue.getType() == ValueType.STRING || rightValue.getType() == ValueType.STRING -> operationString(leftValue.getValue()!!, rightValue.getValue()!!, binaryOperator.op)
            leftValue.getType() == ValueType.NUMBER && rightValue.getType() == ValueType.NUMBER -> operation(leftValue.getValue()!!.toDouble(), rightValue.getValue()!!.toDouble(), binaryOperator.op)
            else -> throw Exception("Unexpected expression")
        }
    }
    private fun operation(left: Double, right: Double, op: Token): Variable {
        return when (op.type) {
            TokenType.OPERATOR_PLUS -> Variable((left + right).toString(), ValueType.NUMBER)
            TokenType.OPERATOR_MINUS -> Variable((left - right).toString(), ValueType.NUMBER)
            TokenType.OPERATOR_TIMES -> Variable((left * right).toString(), ValueType.NUMBER)
            TokenType.OPERATOR_DIVIDE -> Variable((left / right).toString(), ValueType.NUMBER)
            else -> throw Exception("${op.type} is invalid with Number operations in line ${op.location.row} ${op.location.column}")
        }
    }
    private fun operationString(left: String, right: String, op: Token): Variable {
        return when (op.type) {
            TokenType.OPERATOR_PLUS -> Variable((left + right), ValueType.STRING)
            else -> throw Exception("${op.type} is invalid with Number operations in line ${op.location.row} ${op.location.column}")
        }
    }
    fun setType(type: ValueType) {
        inputType = type
    }
    fun setMsg(msgSend: String) {
        msgInput = msgSend
        if (inputType != null && inputLocation != null) {
            inputResults[inputLocation!!] = Variable(msgInput!!, inputType!!)
            inputLocation = null
            inputType = null
            msgInput = null
        }
    }
}
