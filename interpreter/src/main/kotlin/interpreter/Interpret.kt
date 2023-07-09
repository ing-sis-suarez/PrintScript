package interpreter

import consumer.ASTNodeConsumerInterpreter
import consumer.ConsumerResponse
import consumer.ConsumerResponseEnd
import consumer.ConsumerResponseError
import consumer.ConsumerResponseImput
import consumer.ConsumerResponseSuccess
import node.*
import provider.ASTNProviderResponseError
import provider.ASTNProviderResponseSuccess
import provider.ASTNodeProvider
import token.Token
import token.TokenType
import java.util.*

class Interpret(private val astProvider: ASTNodeProvider) : ASTNodeConsumerInterpreter {
    private val variableManager = VariableManager()
    private var onHold: ASTNode? = null
    private val conditionASTN: Stack<ASTNode> = Stack()
    private var nextElse: Boolean? = null
    private val binaryOperatorReader: BinaryOperatorReader = BinaryOperatorReader(variableManager)
    private fun evaluateDeclaration(declarator: Declaration): ConsumerResponse {
        variableManager.add(declarator.identifier.actualValue(), Variable(null, getStringType(declarator.type)), true)
        return ConsumerResponseSuccess(null)
    }
    private fun evaluateDeclarationInitalization(declarationInitalization: DeclarationInitialization): ConsumerResponse {
        val value = binaryOperatorReader.evaluate(declarationInitalization.value.tree)
        if (value.getType() == ValueType.INPUT) return imputOperation(declarationInitalization, value.getValue()!!, getStringType(declarationInitalization.declaration.type))
        return if (getStringType(declarationInitalization.declaration.type) != value.getType()) {
            throw Exception("Invalid Assignation in line ${declarationInitalization.declaration.identifier.location.row} ${declarationInitalization.declaration.identifier.location.column}")
        } else {
            variableManager.add(declarationInitalization.declaration.identifier.actualValue(), value, !declarationInitalization.isConst)
            ConsumerResponseSuccess(null)
        }
    }
    private fun evaluateAssignation(assignation: Assignation): ConsumerResponse {
        if (variableManager.contains(assignation.identifier.actualValue())) {
            if (!variableManager.isMutable(assignation.identifier.actualValue())) throw Exception("Invalid Assignation in line ${assignation.identifier.location.row} ${assignation.identifier.location.column}")
            val value = binaryOperatorReader.evaluate(assignation.value.tree)
            if (value.getType() == ValueType.INPUT) return imputOperation(assignation, value.getValue()!!, variableManager.get(assignation.identifier.actualValue()).getType())
            return if (variableManager.get(assignation.identifier.actualValue()).getType() != value.getType()) {
                throw Exception("Invalid Assignation in line ${assignation.identifier.location.row} ${assignation.identifier.location.column}")
            } else {
                variableManager.replace(assignation.identifier.actualValue(), value)
                ConsumerResponseSuccess(null)
            }
        } else {
            throw Exception("Variable ${assignation.identifier.actualValue()} not found in line ${assignation.identifier.location.row} ${assignation.identifier.location.column}")
        }
    }
    private fun evaluateMethodCall(methodCall: MethodCall): ConsumerResponse {
        val value = binaryOperatorReader.evaluate(methodCall.arguments.tree)
        if (value.getType() == ValueType.INPUT) return imputOperation(methodCall, value.getValue()!!, ValueType.STRING)
        return ConsumerResponseSuccess(value.getValue()!!.replace(".0", ""))
    }
    private fun evaluateCondition(condition: Condition): ConsumerResponse {
        if (condition.condition != null) {
            val value = binaryOperatorReader.evaluate(condition.condition!!.tree)
            if (value.getType() == ValueType.INPUT) return imputOperation(condition, value.getValue()!!, ValueType.BOOLEAN)
            return if (value.getType() != ValueType.BOOLEAN) {
                throw Exception("Invalid Assignation in condition in line")
            } else {
                when (value.getValue()) {
                    "true" -> {
                        conditionASTN.addAll(condition.ifAction.reversed())
                        variableManager.setInCondition(true)
                        nextElse = false
                        val first = conditionASTN.peek()
                        if (first is Condition && first.condition == null) throw Exception("Invalid else Operation")
                        ConsumerResponseSuccess(null)
                    }
                    "false" -> {
                        nextElse = true
                        ConsumerResponseSuccess(null)
                    }
                    else -> { throw Exception("Unexpected exception") }
                }
            }
        } else {
            return when (nextElse) {
                true -> {
                    conditionASTN.addAll(condition.ifAction)
                    variableManager.setInCondition(true)
                    nextElse = null
                    ConsumerResponseSuccess(null)
                }
                false -> {
                    nextElse = null
                    ConsumerResponseSuccess(null)
                }
                null -> { throw Exception("Invalid else Operation") }
            }
        }
    }
    private fun getStringType(token: Token): ValueType {
        return when (token.type) {
            TokenType.NUMBER_KEYWORD -> ValueType.NUMBER
            TokenType.STRING_KEYWORD -> ValueType.STRING
            TokenType.BOOLEAN_KEYWORD -> ValueType.BOOLEAN
            TokenType.IDENTIFIER -> variableManager.get(token.actualValue()).getType()
            else -> {
                return ValueType.ERROR
            }
        }
    }

    private fun imputOperation(ast: ASTNode, msg: String, type: ValueType): ConsumerResponseImput {
        onHold = ast
        binaryOperatorReader.setType(type)
        return ConsumerResponseImput(msg)
    }

    override fun consume(): ConsumerResponse {
        if (onHold != null) {
            val astNode = onHold
            onHold = null
            return readASTN(astNode!!)
        }
        if (!conditionASTN.isEmpty()) {
            return readASTN(conditionASTN.pop())
        } else {
            variableManager.setInCondition(false)
        }
        val ast = astProvider.readASTNode()
        if (ast is ASTNProviderResponseSuccess) {
            return readASTN(ast.astNode)
        }
        return if (ast is ASTNProviderResponseError) {
            ConsumerResponseError(ast.error)
        } else {
            ConsumerResponseEnd()
        }
    }

    private fun readASTN(ast: ASTNode): ConsumerResponse {
        return try {
            if (ast !is Condition) {
                nextElse = null
            }
            when (ast) {
                is Declaration -> evaluateDeclaration(ast)
                is DeclarationInitialization -> evaluateDeclarationInitalization(ast)
                is Assignation -> evaluateAssignation(ast)
                is MethodCall -> evaluateMethodCall(ast)
                is Condition -> evaluateCondition(ast)
                is Value -> throw Exception("Invalid AST")
            }
        } catch (e: Exception) {
            ConsumerResponseError(e.message!!)
        }
    }

    override fun getValue(resp: String) {
        binaryOperatorReader.setMsg(resp)
    }
}
