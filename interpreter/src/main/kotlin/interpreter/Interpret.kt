package interpreter

import consumer.ASTNodeConsumer
import consumer.ConsumerResponse
import consumer.ConsumerResponseEnd
import consumer.ConsumerResponseError
import consumer.ConsumerResponseSuccess
import node.Assignation
import node.Declaration
import node.DeclarationInitialization
import node.MethodCall
import provider.ASTNProviderResponseError
import provider.ASTNProviderResponseSuccess
import provider.ASTNodeProvider
import token.Token
import token.TokenType
import java.util.HashMap

class Interpret(private val astProvider: ASTNodeProvider) : ASTNodeConsumer {
    private val variables: MutableMap<String, Pair<String, String?>> = HashMap()
    private val binaryOperatorReader: BinaryOperatorReader = BinaryOperatorReader(variables)
    private fun evaluateDeclaration(declarator: Declaration): ConsumerResponse {
        variables[declarator.identifier.actualValue()] = Pair(getStringType(declarator.type), null)
        return ConsumerResponseSuccess(null)
    }
    private fun evaluateDeclarationInitalization(declarationInitalization: DeclarationInitialization): ConsumerResponse {
        val value = binaryOperatorReader.evaluate(declarationInitalization.value.tree)
        if (value.first == "Error") {
            return ConsumerResponseError(value.second)
        }
        return if (getStringType(declarationInitalization.declaration.type) != value.first) {
            ConsumerResponseError("Invalid Assignation in line ${declarationInitalization.declaration.identifier.location.row} ${declarationInitalization.declaration.identifier.location.column}")
        } else {
            variables[declarationInitalization.declaration.identifier.actualValue()] = value
            ConsumerResponseSuccess(null)
        }
    }
    private fun evaluateAssignation(assignation: Assignation): ConsumerResponse {
        if (variables.containsKey(assignation.identifier.actualValue())) {
            val value = binaryOperatorReader.evaluate(assignation.value.tree)
            if (value.first == "Error") {
                return ConsumerResponseError(value.second)
            }
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
        return if (value.first == "Error") {
            ConsumerResponseError(value.second)
        } else {
            ConsumerResponseSuccess(value.second.replace(".0", ""))
        }
    }
    private fun getStringType(token: Token): String {
        return when (token.type) {
            TokenType.NUMBER_KEYWORD -> "Number"
            TokenType.STRING_KEYWORD -> "String"
            TokenType.IDENTIFIER -> variables[token.actualValue()]!!.second!!
            else -> {
                return ""
            }
        }
    }

    override fun consume(): ConsumerResponse {
        val ast = astProvider.readASTNode()
        if (ast is ASTNProviderResponseSuccess) {
            return when (ast.astNode) {
                is Declaration -> evaluateDeclaration(ast.astNode as Declaration)
                is DeclarationInitialization -> evaluateDeclarationInitalization(ast.astNode as DeclarationInitialization)
                is Assignation -> evaluateAssignation(ast.astNode as Assignation)
                is MethodCall -> evaluateMethodCall(ast.astNode as MethodCall)
                else -> {
                    return ConsumerResponseError("Invalid AST")
                }
            }
        }
        return if (ast is ASTNProviderResponseError) {
            ConsumerResponseError(ast.error)
        } else {
            ConsumerResponseEnd()
        }
    }
}
