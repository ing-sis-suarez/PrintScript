package DeclarationInitializationAnalizer

import Analizer
import ValueTypeReader
import ast_node.ASTNode
import ast_node.DeclarationInitialization
import java.lang.IllegalArgumentException

class DiferentTypeAnalizer(): Analizer {
    val valueTypereader = ValueTypeReader()
     override fun analize(astNode: ASTNode, variables: MutableMap<String, Pair<String, Boolean>>) {
         val declarationInitialization: DeclarationInitialization = astNode as DeclarationInitialization
        if(declarationInitialization.declaration.type.equals(valueTypereader.getValueType(astNode.value.tree))){
            return
        }else{
            throw IllegalArgumentException("La variable y el valor no son compatibles")
        }
    }
}