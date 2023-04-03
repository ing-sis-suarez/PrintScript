package Analizers

import Analizer
import ast_node.ASTNode
import ast_node.MethodCall
import ast_node.Value

class MethodNoExpresion: Analizer {
    override fun analize(astNode: ASTNode) {
        when{
            astNode is MethodCall -> analize(astNode)
        }
    }

    private fun analize(value: Value){
        if (value.tree.right == null && value.tree.left == null){
            return
        }else{
            // tirar excepcion
        }
    }

}