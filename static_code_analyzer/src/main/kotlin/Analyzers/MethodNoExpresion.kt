package Analyzers

import Analyzer
import ast_node.ASTNode
import ast_node.MethodCall
import ast_node.Value

class MethodNoExpresion: Analyzer {
    override fun analyze(astNode: ASTNode) {
        when{
            astNode is MethodCall -> analyze(astNode)
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