package Analyzers

import Analyzer
import InvalidFormatException
import ast_node.ASTNode
import ast_node.Declaration
import ast_node.DeclarationInitialization
import token.Location

class SnakeCaseFormat: Analyzer {
    override fun analyze(astNode: ASTNode) {
        when(astNode){
            is Declaration -> analize(astNode.identifier.actualValue(), astNode.identifier.location)
            is DeclarationInitialization -> analize(astNode.declaration.identifier.actualValue(), astNode.declaration.identifier.location)
        }
    }

    private fun analize(str: String, location: Location){
        if (!str.matches("^[a-z]+(_[a-z]+)*$".toRegex())) {
            return
        }else{
            throw InvalidFormatException("Invalid typing format in row ${location.row} ${location.column}")
        }
    }
}