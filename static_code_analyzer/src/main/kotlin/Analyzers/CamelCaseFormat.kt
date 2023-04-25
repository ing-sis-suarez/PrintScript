package Analyzers

import Analyzer
import InvalidFormatException
import node.ASTNode
import node.Declaration
import node.DeclarationInitialization
import token.Location

class CamelCaseFormat() : Analyzer {
    override fun analyze(astNode: ASTNode) {
        when (astNode) {
            is Declaration -> analyze(astNode.identifier.actualValue(), astNode.identifier.location)
            is DeclarationInitialization -> analyze(astNode.declaration.identifier.actualValue(), astNode.declaration.identifier.location)
        }
    }

    private fun analyze(str: String, location: Location) {
        if (!Regex("[a-z]+(?:[A-Z][a-z]*)*").matches(str)) {
            throw InvalidFormatException("Invalid typing format in row ${location.row} ${location.column}")
        }
    }
}
