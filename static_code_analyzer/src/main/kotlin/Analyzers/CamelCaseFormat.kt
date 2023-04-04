package Analyzers

import Analyzer
import ast_node.ASTNode
import ast_node.Declaration
import ast_node.DeclarationInitialization

class CamelCaseFormat(): Analyzer{
    override fun analyze(astNode: ASTNode) {
         when(astNode){
             is Declaration -> analyze(astNode.identifier.actualValue())
             is DeclarationInitialization -> analyze(astNode.declaration.identifier.actualValue())
         }
    }

    private fun analyze(str: String){
        if (!str.matches("^[a-zA-Z]+(([A-Z][a-z])|[a-z0-9]+)*$".toRegex())) {
            return
        }else{
            // hacer excepcion
        }
    }
}