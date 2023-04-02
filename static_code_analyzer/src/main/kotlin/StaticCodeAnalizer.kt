import ast_node.*

class StaticCodeAnalizer {
    val declarationList: ArrayList<Analizer> = ArrayList()
    val declarationInitializationList: ArrayList<Analizer> = ArrayList()
    val valueList: ArrayList<Analizer> = ArrayList()
    val assignationList: ArrayList<Analizer> = ArrayList()
    val methodCallList: ArrayList<Analizer> = ArrayList()
    val variables: MutableMap<String, Pair<String, Boolean>> = HashMap()
    fun analize(execution: List<ASTNode>){
        for (ast in execution)
        when(ast){
            is Declaration ->{
                for (declaration in declarationList){
                    declaration.analize(ast, variables)
                }
            }
            is DeclarationInitialization ->{
                for (declarationInitialization in declarationInitializationList){
                    declarationInitialization.analize(ast, variables)
                }
            }
            is Value ->{
                for (value in valueList){
                    value.analize(ast, variables)
                }
            }
            is Assignation ->{
                for (assignation in assignationList){
                    assignation.analize(ast, variables)
                }
            }
            is MethodCall ->{
                for (methodcall in methodCallList){
                    methodcall.analize(ast, variables)
                }
            }
        }
    }

}