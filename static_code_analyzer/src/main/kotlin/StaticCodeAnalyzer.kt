import Analyzers.CamelCaseFormat
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

class StaticCodeAnalyzer(json: String, private val astProvider: ASTNodeProvider) : ASTNodeConsumer {
    val analizersList: ArrayList<Analyzer> = ArrayList()

    init {
        buildSCA(json)
    }

    private fun buildSCA(json: String) {
        if (json.isEmpty()) {
            analizersList.add(CamelCaseFormat())
            return
        }
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
