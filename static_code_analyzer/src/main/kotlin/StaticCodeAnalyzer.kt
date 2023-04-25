import Analyzers.CamelCaseFormat
import Analyzers.MethodNoExpresion
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import node.ASTNode

class StaticCodeAnalyzer(json: String) {
    val analizersList: ArrayList<Analyzer> = ArrayList()

    init {
        buildSCA(json)
    }

    private fun buildSCA(json: String) {
        val objectBoolMap: Map<String, Boolean> = Gson().fromJson(json, object : TypeToken<Map<String, Boolean>>() {}.type)

        objectBoolMap.forEach { (nombre, valor) ->
            addAnalizer(nombre, valor)
        }
    }

    private fun addAnalizer(nombre: String, valor: Boolean) {
        if (valor) {
            when (nombre) {
                "CamelCaseFormat" -> analizersList.add(CamelCaseFormat())
                "SnakeCaseFormat" -> analizersList.add(SnakeCaseFormat())
                "MethodNoExpresion" -> analizersList.add(MethodNoExpresion())
                // Si hay más objetos que agregar, agregarlos aquí con sus correspondientes nombres
                else -> {}
            }
        }
    }
    fun analyze(execution: List<ASTNode>) {
        for (ast in execution) {
            for (analyzer in analizersList) {
                analyzer.analyze(ast)
            }
        }
    }
}
