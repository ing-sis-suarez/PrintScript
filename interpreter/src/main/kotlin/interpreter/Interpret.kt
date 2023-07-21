package interpreter

import consumer.ASTNodeConsumerInterpreter
import consumer.ConsumerResponse
import consumer.ConsumerResponseEnd
import consumer.ConsumerResponseError
import consumer.ConsumerResponseInput
import consumer.ConsumerResponseSuccess
import node.ASTNode
import node.Assignation
import node.Condition
import node.Declaration
import node.DeclarationInitialization
import node.MethodCall
import node.Value
import provider.ASTNProviderResponseError
import provider.ASTNProviderResponseSuccess
import provider.ASTNodeProvider
import token.Token
import token.TokenType
import java.util.Stack

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
    private fun evaluateDeclarationInitialization(declarationInitialization: DeclarationInitialization): ConsumerResponse {
        val value = binaryOperatorReader.evaluate(declarationInitialization.value.tree)
        if (value.getType() == ValueType.INPUT) return inputOperation(declarationInitialization, value.getValue()!!, getStringType(declarationInitialization.declaration.type))
        return if (getStringType(declarationInitialization.declaration.type) != value.getType()) {
            throw Exception("Invalid Assignation in line ${declarationInitialization.declaration.identifier.location.row} ${declarationInitialization.declaration.identifier.location.column}")
        } else {
            variableManager.add(declarationInitialization.declaration.identifier.actualValue(), value, !declarationInitialization.isConst)
            ConsumerResponseSuccess(null)
        }
    }
    private fun evaluateAssignation(assignation: Assignation): ConsumerResponse {
        if (variableManager.contains(assignation.identifier.actualValue())) {
            if (!variableManager.isMutable(assignation.identifier.actualValue())) throw Exception("Invalid Assignation in line ${assignation.identifier.location.row} ${assignation.identifier.location.column}")
            val value = binaryOperatorReader.evaluate(assignation.value.tree)
            if (value.getType() == ValueType.INPUT) return inputOperation(assignation, value.getValue()!!, variableManager.get(assignation.identifier.actualValue()).getType())
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
        if (value.getType() == ValueType.INPUT) return inputOperation(methodCall, value.getValue()!!, ValueType.STRING)
        return ConsumerResponseSuccess(value.getValue()!!.replace(".0", ""))
    }
    private fun evaluateCondition(condition: Condition): ConsumerResponse {
        if (condition.condition != null) {
            val value = binaryOperatorReader.evaluate(condition.condition!!.tree)
            if (value.getType() == ValueType.INPUT) return inputOperation(condition, value.getValue()!!, ValueType.BOOLEAN)
            if (value.getType() != ValueType.BOOLEAN) {
                throw Exception("Invalid Assignation in condition in line")
            } else {
                return when (value.getValue()) {
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

    private fun inputOperation(ast: ASTNode, msg: String, type: ValueType): ConsumerResponseInput {
        onHold = ast
        binaryOperatorReader.setType(type)
        return ConsumerResponseInput(msg)
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
            if (ast.astNode !is Condition) {
                nextElse = null
            }
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
            return when (ast) {
                is Declaration -> evaluateDeclaration(ast)
                is DeclarationInitialization -> evaluateDeclarationInitialization(ast)
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
