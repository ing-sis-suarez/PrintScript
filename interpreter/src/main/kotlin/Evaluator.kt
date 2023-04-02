import ast_node.*
import token.*
import java.util.HashMap

class Evaluator {
    val variables: MutableMap<String, Pair<String, String?>> = HashMap()
    val binaryOperatorReader: BinaryOperatorReader = BinaryOperatorReader(variables)


    fun evaluateDeclaration(declarator: Declaration) {
        variables[declarator.identifier.actualValue()] = Pair(declarator.type.actualValue(), null)
    }

    fun evaluateDeclarationInitalization(declarationInitalization: DeclarationInitialization) {
        variables[declarationInitalization.declaration.identifier.actualValue()] = Pair(
            declarationInitalization.declaration.type.actualValue(),
            binaryOperatorReader.evaluate(declarationInitalization.value.tree).actualValue())
    }

    fun evaluateAssignation(assignation: Assignation) {
        if (variables.containsKey(assignation.identifier.actualValue())){
            variables.replace(assignation.identifier.actualValue(), Pair(
                variables.get(assignation.identifier.actualValue())!!.first,
                binaryOperatorReader.evaluate(assignation.value.tree).actualValue()))
        }
    }

    fun evaluateMethodCall(methodCall: MethodCall){
        println(binaryOperatorReader.evaluate(methodCall.arguments.tree).actualValue())
    }

    fun executionReader(execution: Execution){
        for (ast in execution.trees){
            evaluate(ast)
        }
    }

    private fun evaluate(ast: ASTNode) {
        when(ast){
            is Declaration -> evaluateDeclaration(ast)
            is DeclarationInitialization -> evaluateDeclarationInitalization(ast)
            is Assignation -> evaluateAssignation(ast)
            is MethodCall -> evaluateMethodCall(ast)
        }
    }


}