import Analyzers.CamelCaseFormat
import Analyzers.InputNoExpresion
import Analyzers.MethodNoExpresion
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import consumer.ASTNodeConsumer
import consumer.ConsumerResponse
import consumer.ConsumerResponseEnd
import consumer.ConsumerResponseError
import consumer.ConsumerResponseSuccess
import provider.ASTNProviderResponseError
import provider.ASTNProviderResponseSuccess
import provider.ASTNodeProvider

class StaticCodeAnalyzer(objectBoolMap: Map<String, Boolean>, private val astProvider: ASTNodeProvider) : ASTNodeConsumer {
    val analizersList: ArrayList<Analyzer> = ArrayList()

    init {
        buildSCA(objectBoolMap)
    }

    private fun buildSCA(objectBoolMap: Map<String, Boolean>) {
        if (objectBoolMap.isEmpty()) {
            analizersList.add(CamelCaseFormat())
            return
        }
        objectBoolMap.forEach { (nombre, valor) ->
            addAnalyzer(nombre, valor)
        }
    }

    private fun addAnalyzer(nombre: String, valor: Boolean) {
        if (valor) {
            when (nombre) {
                "CamelCaseFormat" -> analizersList.add(CamelCaseFormat())
                "SnakeCaseFormat" -> analizersList.add(SnakeCaseFormat())
                "MethodNoExpresion" -> analizersList.add(MethodNoExpresion())
                "InputNoExpresion" -> analizersList.add(InputNoExpresion())
                else -> {}
            }
        }
    }

    override fun consume(): ConsumerResponse {
        val ast = astProvider.readASTNode()
        if (ast is ASTNProviderResponseSuccess) {
            for (analyzer in analizersList) {
                val response = analyzer.analyze(ast.astNode)
                if (response is ConsumerResponseError) {
                    return ConsumerResponseError(response.error)
                }
            }
            return ConsumerResponseSuccess(null)
        }
        return if (ast is ASTNProviderResponseError) {
            ConsumerResponseError(ast.error)
        } else {
            ConsumerResponseEnd()
        }
    }
}
