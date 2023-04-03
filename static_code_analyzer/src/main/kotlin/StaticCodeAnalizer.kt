import ast_node.*

class StaticCodeAnalizer {
    val analizersList: ArrayList<Analizer> = ArrayList()
    fun analize(execution: List<ASTNode>){
        for (ast in execution){
            for (analizer in analizersList){
                analizer.analize(ast)
            }
        }

    }

}