import ast_node.*
import token.*
import java.lang.IllegalArgumentException
import java.util.HashMap

class Evaluator {
    val variables: MutableMap<String, Pair<String, String?>> = HashMap()
    val binaryOperatorReader: BinaryOperatorReader = BinaryOperatorReader(variables)


    fun evaluateDeclaration(declarator: Declaration) {
        variables[declarator.identifier.actualValue()] = Pair(getStringType(declarator.type.type), null)
    }

    fun evaluateDeclarationInitalization(declarationInitalization: DeclarationInitialization) {
        if (!getStringType(declarationInitalization.declaration.type.type).equals(
                binaryOperatorReader.getValueType(declarationInitalization.value.tree))){
            throw IlligalTypeException("Invalid Assignation in line ${declarationInitalization.declaration.identifier.location.row} ${declarationInitalization.declaration.identifier.location.column}")
        }
        variables[declarationInitalization.declaration.identifier.actualValue()] = Pair(
            declarationInitalization.declaration.type.actualValue(),
            binaryOperatorReader.evaluate(declarationInitalization.value.tree).toString())
    }

    private fun evaluateAssignation(assignation: Assignation) {
        if (variables.containsKey(assignation.identifier.actualValue())){
            if (!variables.get(assignation.identifier.actualValue())!!.first.equals(
                    binaryOperatorReader.getValueType(assignation.value.tree))){
                throw IlligalTypeException("Invalid Assignation in line ${assignation.identifier.location.row} ${assignation.identifier.location.column}")
            }
            variables.replace(assignation.identifier.actualValue(), Pair(
                variables.get(assignation.identifier.actualValue())!!.first,
                binaryOperatorReader.evaluate(assignation.value.tree).toString()))
        }else{
            throw VariableDontExistException("Variable ${assignation.identifier.actualValue()} not found in line ${assignation.identifier.location.row} ${assignation.identifier.location.column}")
        }
    }

    private fun evaluateMethodCall(methodCall: MethodCall){
        println(binaryOperatorReader.evaluate(methodCall.arguments.tree).toString())
    }

    fun executionReader(execution: List<ASTNode>){
        for (ast in execution){
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

    private fun getStringType(tokenType: TokenType): String {
        return when{
            tokenType == TokenType.NUMBER_KEYWORD -> "Number"
            tokenType == TokenType.STRING_KEYWORD -> "String"
            else -> {
                return ""
            }
        }
    }

}