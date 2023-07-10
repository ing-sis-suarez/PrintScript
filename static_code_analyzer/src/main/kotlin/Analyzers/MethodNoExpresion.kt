package Analyzers

import Analyzer
import consumer.ConsumerResponse
import consumer.ConsumerResponseError
import consumer.ConsumerResponseSuccess
import node.ASTNode
import node.BinaryOperator
import node.MethodCall
import node.Value
import token.Location

class MethodNoExpresion : Analyzer {
    override fun analyze(astNode: ASTNode): ConsumerResponse {
        return when (astNode) {
            is MethodCall -> analyze(astNode.arguments, astNode.identifier.location)
            else -> { return ConsumerResponseSuccess(null) }
        }
    }

    private fun analyze(value: Value, location: Location): ConsumerResponse {
        return if (value.tree !is BinaryOperator) {
            ConsumerResponseSuccess(null)
        } else {
            ConsumerResponseError("Invalid expression in row ${location.row} ${location.column}")
        }
    }
}
