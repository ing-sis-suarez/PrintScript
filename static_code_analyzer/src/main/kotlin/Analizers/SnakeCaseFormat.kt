package Analizers

import Analizer
import ast_node.ASTNode
import ast_node.Declaration
import ast_node.DeclarationInitialization

class SnakeCaseFormat: Analizer {
    override fun analize(astNode: ASTNode) {
        when(astNode){
            is Declaration -> analize(astNode.identifier.actualValue())
            is DeclarationInitialization -> analize(astNode.declaration.identifier.actualValue())
        }
    }

    private fun analize(str: String){
        if (!str.matches("^[a-z]+(_[a-z]+)*$".toRegex())) {
            return
        }else{
            // hacer excepcion
        }
    }
}