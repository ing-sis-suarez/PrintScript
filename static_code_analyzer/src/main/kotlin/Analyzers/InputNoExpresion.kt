package Analyzers

import Analyzer
import consumer.ConsumerResponse
import consumer.ConsumerResponseError
import consumer.ConsumerResponseSuccess
import node.ASTNode
import node.Assignation
import node.BinaryOperator
import node.BinaryTokenNode
import node.DeclarationInitialization
import node.MethodCall

class InputNoExpresion() : Analyzer {
    override fun analyze(astNode: ASTNode): ConsumerResponse {
        return when (astNode) {
            is DeclarationInitialization -> analyzeValue(astNode.value.tree)
            is Assignation -> analyzeValue(astNode.value.tree)
            is MethodCall -> analyzeValue(astNode.arguments.tree)
            else -> return ConsumerResponseSuccess(null)
        }
    }

    private fun analyzeValue(value: BinaryTokenNode): ConsumerResponse {
        if (value is MethodCall) {
            return if (value.arguments.tree !is BinaryOperator) {
                ConsumerResponseSuccess(null)
            } else { ConsumerResponseError("Invalid expression in row ${value.identifier.location.row} ${value.identifier.location.column}") }
        }
        if (value is BinaryOperator) {
            return analyzeBinaryOperator(value)
        } else {
            return ConsumerResponseSuccess(null)
        }
    }

    private fun analyzeBinaryOperator(binaryOperator: BinaryOperator): ConsumerResponse {
        val left = analyzeValue(binaryOperator.left)
        if (left is ConsumerResponseError) {
            return left
        }
        val right = analyzeValue(binaryOperator.right)
        if (right is ConsumerResponseError) {
            return right
        } else {
            return ConsumerResponseSuccess(null)
        }
    }
}
