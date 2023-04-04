package Analyzers

import Analyzer
import InvalidFormatException
import ast_node.ASTNode
import ast_node.Declaration
import ast_node.DeclarationInitialization
import token.Location

class CamelCaseFormat(): Analyzer{
    override fun analyze(astNode: ASTNode) {
         when(astNode){
             is Declaration -> analyze(astNode.identifier.actualValue(), astNode.identifier.location)
             is DeclarationInitialization -> analyze(astNode.declaration.identifier.actualValue(), astNode.declaration.identifier.location)
         }
    }

    private fun analyze(str: String, location: Location){
        if (!str.matches("^[a-zA-Z]+(([A-Z][a-z])|[a-z0-9]+)*$".toRegex())) {
            return
        }else{
            throw InvalidFormatException("Invalid typing format in row ${location.row} ${location.column}")
        }
    }
}