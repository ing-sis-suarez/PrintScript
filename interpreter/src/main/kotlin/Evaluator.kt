import utilities.*
import java.util.HashMap

class Evaluator {
    val variables: MutableMap<String, Pair<String, String?>> = HashMap()
    val binaryOperatorReader: BinaryOperatorReader = BinaryOperatorReader(variables)


    fun evaluate(declarator: Declaration) {
        variables[declarator.identifier.actualValue()] = Pair(declarator.type.actualValue(), null)
    }

    fun evaluate(declarationInitalization: DeclarationInitalization) {
        variables[declarationInitalization.declaration.identifier.actualValue()] = Pair(
            declarationInitalization.declaration.type.actualValue(),
            binaryOperatorReader.evaluate(declarationInitalization.value.tree).toString())
    }

    fun evaluate(assignation: Assignation) {
        if (variables.containsKey(assignation.identifier.actualValue())){
            variables.replace(assignation.identifier.actualValue(), Pair(
                variables.get(assignation.identifier.actualValue())!!.first,
                binaryOperatorReader.evaluate(assignation.value.tree).toString()))
        }
    }

    fun evaluate(methodCall: MethodCall){
        println(binaryOperatorReader.evaluate(methodCall.arguments.tree))
    }

    fun executionReader(execution: Execution){
        for (ast in execution.trees){
            evaluate(ast)
        }
    }

    private fun evaluate(ast: ASTNode) {
        when(ast){
            is Declaration -> evaluate(ast)
            is DeclarationInitalization -> evaluate(ast)
            is Value -> evaluate(ast)
            is Assignation -> evaluate(ast)
            is MethodCall -> evaluate(ast)
            is Declaration -> evaluate(ast)
        }
    }


}