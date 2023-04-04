import ast_node.*

class StaticCodeAnalyzer {
    val analizersList: ArrayList<Analyzer> = ArrayList()
    fun analyze(execution: List<ASTNode>){
        for (ast in execution){
            for (analyzer in analizersList){
                analyzer.analyze(ast)
            }
        }

    }

}