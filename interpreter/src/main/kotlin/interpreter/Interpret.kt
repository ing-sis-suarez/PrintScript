package interpreter

import consumer.ASTNodeConsumerInterpreter
import consumer.ConsumerResponse
import consumer.ConsumerResponseEnd
import consumer.ConsumerResponseError
import consumer.ConsumerResponseImput
import consumer.ConsumerResponseSuccess
import node.ASTNode
import node.Assignation
import node.Condition
import node.Declaration
import node.DeclarationInitialization
import node.MethodCall
import provider.ASTNProviderResponseError
import provider.ASTNProviderResponseSuccess
import provider.ASTNodeProvider
import token.Token
import token.TokenType
import java.util.*
import kotlin.collections.HashMap

class Interpret(private val astProvider: ASTNodeProvider) : ASTNodeConsumerInterpreter {
    private val variables: MutableMap<String, Pair<String, String?>> = HashMap()
    private val mutable: MutableMap<String, Boolean> = HashMap()
    private var onHold: ASTNode? = null
    private val conditionASTN: Stack<ASTNode> = Stack()
    private var nextElse: Boolean? = null
    private val binaryOperatorReader: BinaryOperatorReader = BinaryOperatorReader(variables)
    private fun evaluateDeclaration(declarator: Declaration): ConsumerResponse {
        variables[declarator.identifier.actualValue()] = Pair(getStringType(declarator.type), null)
        mutable[declarator.identifier.actualValue()] = true
        return ConsumerResponseSuccess(null)
    }
    private fun evaluateDeclarationInitalization(declarationInitalization: DeclarationInitialization): ConsumerResponse {
        val value = binaryOperatorReader.evaluate(declarationInitalization.value.tree)
        if (value.first == "Error") return ConsumerResponseError(value.second)
        if (value.first == "Imput") return imputOperation(declarationInitalization, value.second)
        return if (getStringType(declarationInitalization.declaration.type) != value.first) {
            ConsumerResponseError("Invalid Assignation in line ${declarationInitalization.declaration.identifier.location.row} ${declarationInitalization.declaration.identifier.location.column}")
        } else {
            variables[declarationInitalization.declaration.identifier.actualValue()] = value
            if (declarationInitalization.isConst) {
                mutable[declarationInitalization.declaration.identifier.actualValue()] = false
            } else {
                mutable[declarationInitalization.declaration.identifier.actualValue()] = false
            }
            ConsumerResponseSuccess(null)
        }
    }
    private fun evaluateAssignation(assignation: Assignation): ConsumerResponse {
        if (variables.containsKey(assignation.identifier.actualValue())) {
            if (!mutable[assignation.identifier.actualValue()]!!) return ConsumerResponseError("Invalid Assignation in line ${assignation.identifier.location.row} ${assignation.identifier.location.column}")
            val value = binaryOperatorReader.evaluate(assignation.value.tree)
            if (value.first == "Error") return ConsumerResponseError(value.second)
            if (value.first == "Imput") return imputOperation(assignation, value.second)
            return if (variables[assignation.identifier.actualValue()]!!.first != value.first) {
                ConsumerResponseError("Invalid Assignation in line ${assignation.identifier.location.row} ${assignation.identifier.location.column}")
            } else {
                variables.replace(assignation.identifier.actualValue(), value)
                ConsumerResponseSuccess(null)
            }
        } else {
            return ConsumerResponseError("Variable ${assignation.identifier.actualValue()} not found in line ${assignation.identifier.location.row} ${assignation.identifier.location.column}")
        }
    }
    private fun evaluateMethodCall(methodCall: MethodCall): ConsumerResponse {
        val value = binaryOperatorReader.evaluate(methodCall.arguments.tree)
        if (value.first == "Error") return ConsumerResponseError(value.second)
        if (value.first == "Imput") return imputOperation(methodCall, value.second)
        return ConsumerResponseSuccess(value.second.replace(".0", ""))
    }
    private fun evaluateCondition(condition: Condition): ConsumerResponse {
        if (condition.condition != null) {
            val value = binaryOperatorReader.evaluate(condition.condition!!.tree)
            if (value.first == "Error") return ConsumerResponseError(value.second)
            if (value.first == "Imput") return imputOperation(condition, value.second)
            return if (value.first != "Boolean") {
                ConsumerResponseError("Invalid Assignation in line ${condition.condition!!.tree.token.location.row} ${condition.condition!!.tree.token.location.row}")
            } else {
                when (value.second) {
                    "true" -> {
                        conditionASTN.addAll(condition.ifAction.reversed())
                        nextElse = false
                        val first = conditionASTN.peek()
                        if (first is Condition && first.condition == null) return ConsumerResponseError("Invalid else Operation")
                        ConsumerResponseSuccess(null)
                    }
                    "false" -> {
                        nextElse = true
                        ConsumerResponseSuccess(null)
                    }
                    else -> { ConsumerResponseError("Unexpected exception") }
                }
            }
        } else {
            return when (nextElse) {
                true -> {
                    conditionASTN.addAll(condition.ifAction)
                    nextElse = null
                    ConsumerResponseSuccess(null)
                }
                false -> {
                    nextElse = null
                    ConsumerResponseSuccess(null)
                }
                null -> { ConsumerResponseError("Invalid else Operation") }
            }
        }
    }
    private fun getStringType(token: Token): String {
        return when (token.type) {
            TokenType.NUMBER_KEYWORD -> "Number"
            TokenType.STRING_KEYWORD -> "String"
            TokenType.BOOLEAN_KEYWORD -> "Boolean"
            TokenType.IDENTIFIER -> variables[token.actualValue()]!!.second!!
            else -> {
                return ""
            }
        }
    }

    private fun imputOperation(ast: ASTNode, msg: String): ConsumerResponseImput {
        onHold = ast
        return ConsumerResponseImput(msg)
    }

    override fun consume(): ConsumerResponse {
        if (!conditionASTN.isEmpty()) {
            return readASTN(conditionASTN.pop())
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
        if (ast !is Condition) {
            nextElse = null
        }
        return when (ast) {
            is Declaration -> evaluateDeclaration(ast)
            is DeclarationInitialization -> evaluateDeclarationInitalization(ast)
            is Assignation -> evaluateAssignation(ast)
            is MethodCall -> evaluateMethodCall(ast)
            is Condition -> evaluateCondition(ast)
            else -> return ConsumerResponseError("Invalid AST")
        }
    }

    override fun getValue(resp: String) {
    }
}
