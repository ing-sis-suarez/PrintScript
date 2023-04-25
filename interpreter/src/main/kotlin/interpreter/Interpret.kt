package interpreter

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

class Interpret(private val astProvider: ASTNodeProvider) : Interpreter {
    private val variables: MutableMap<String, Pair<String, String?>> = HashMap()
    private val binaryOperatorReader: BinaryOperatorReader = BinaryOperatorReader(variables)
    private fun evaluateDeclaration(declarator: Declaration): InterpreterResponse {
        variables[declarator.identifier.actualValue()] = Pair(getStringType(declarator.type), null)
        return InterpreterSuccessResponse(null)
    }
    private fun evaluateDeclarationInitalization(declarationInitalization: DeclarationInitialization): InterpreterResponse {
        if (getStringType(declarationInitalization.declaration.type) != binaryOperatorReader.getValueType(declarationInitalization.value.tree, variables)) {
            return InterpreterFailResponse("Invalid Assignation in line ${declarationInitalization.declaration.identifier.location.row} ${declarationInitalization.declaration.identifier.location.column}")
        }
        val value = binaryOperatorReader.evaluate(declarationInitalization.value.tree)
        return if (value is InterpreterFailResponse) {
            value
        } else {
            variables[declarationInitalization.declaration.identifier.actualValue()] = Pair(
                getStringType(declarationInitalization.declaration.type),
                value.toString()
            )
            InterpreterSuccessResponse(null)
        }
    }
    private fun evaluateAssignation(assignation: Assignation): InterpreterResponse {
        if (variables.containsKey(assignation.identifier.actualValue())) {
            if (variables[assignation.identifier.actualValue()]!!.first != binaryOperatorReader.getValueType(assignation.value.tree, variables)) {
                return InterpreterFailResponse("Invalid Assignation in line ${assignation.identifier.location.row} ${assignation.identifier.location.column}")
            }
            val value = binaryOperatorReader.evaluate(assignation.value.tree)
            return if (value is InterpreterFailResponse) {
                value
            } else {
                variables.replace(
                    assignation.identifier.actualValue(),
                    Pair(
                        variables[assignation.identifier.actualValue()]!!.first,
                        value.toString()
                    )
                )
                InterpreterSuccessResponse(null)
            }
        } else {
            return InterpreterFailResponse("Variable ${assignation.identifier.actualValue()} not found in line ${assignation.identifier.location.row} ${assignation.identifier.location.column}")
        }
    }
    private fun evaluateMethodCall(methodCall: MethodCall): InterpreterResponse {
        val value = binaryOperatorReader.evaluate(methodCall.arguments.tree)
        return if (value is InterpreterFailResponse) {
            value
        } else {
            InterpreterSuccessResponse(value.toString().replace(".0", ""))
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

    override fun interpret(): InterpreterResponse {
        val ast = astProvider.readASTNode()
        if (ast is ASTNProviderResponseSuccess) {
            return when (ast.astNode) {
                is Declaration -> evaluateDeclaration(ast.astNode as Declaration)
                is DeclarationInitialization -> evaluateDeclarationInitalization(ast.astNode as DeclarationInitialization)
                is Assignation -> evaluateAssignation(ast.astNode as Assignation)
                is MethodCall -> evaluateMethodCall(ast.astNode as MethodCall)
                else -> {
                    return InterpreterFailResponse("Invalid AST")
                }
            }
        }
        return if (ast is ASTNProviderResponseError) {
            InterpreterFailResponse(ast.error)
        } else {
            InterpreterEndResponse()
        }
    }
}
