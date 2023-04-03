package Analizers

import Analizer
import ast_node.ASTNode
import ast_node.Declaration
import ast_node.DeclarationInitialization
import ast_node.MethodCall

class CamelCaseFormat(): Analizer{
    override fun analize(astNode: ASTNode) {
         when(astNode){
             is Declaration -> analize(astNode.identifier.actualValue())
             is DeclarationInitialization -> analize(astNode.declaration.identifier.actualValue())
         }
    }

    private fun analize(str: String){
        if (!str.matches("^[a-zA-Z]+(([A-Z][a-z])|[a-z0-9]+)*$".toRegex())) {
            return
        }else{
            // hacer excepcion
        }
    }
}