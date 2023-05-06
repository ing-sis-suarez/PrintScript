import consumer.ASTNodeConsumer
import consumer.ConsumerResponseError
import consumer.ConsumerResponseSuccess
import lexer.RegularLexer
import lexer.TokenReadersProvider
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import parser.RegularParser
import provider.ASTNodeProvider
import provider.ASTNodeProviderImpl
import java.io.File

class SCATest {

    @Test
    fun camelCaseTest() {
        val sca: ASTNodeConsumer = StaticCodeAnalyzer(readtxt("camelCase.json"), setup(Files.getResourceAsFile("mock_text_declaration_Snake_Case.txt")!!))
        val result = sca.consume()
        Assertions.assertEquals(result, ConsumerResponseError("Invalid typing format in row 1 4"))
    }

    @Test
    fun snakeCaseTest() {
        val sca: ASTNodeConsumer = StaticCodeAnalyzer(readtxt("snakeCase.json"), setup(Files.getResourceAsFile("mock_text_declaration_Camel_Case.txt")!!))
        val result = sca.consume()
        Assertions.assertEquals(result, ConsumerResponseError("Invalid typing format in row 1 4"))
    }

    @Test
    fun methodNoExpresionTest() {
        val sca: ASTNodeConsumer = StaticCodeAnalyzer(readtxt("methodNoExpresion.json"), setup(Files.getResourceAsFile("mock_text_declaration_Method_No_Expresion.txt")!!))
        val result = sca.consume()
        Assertions.assertEquals(result, ConsumerResponseSuccess(null))
    }

    private fun setup(src: File): ASTNodeProvider {
        val tokenMap = TokenReadersProvider().getTokenMap("1.0")
        val tokenProvider = FileTokenProvider(src, RegularLexer(tokenMap!!))
        return ASTNodeProviderImpl(tokenProvider, RegularParser.createDefaultParser())
    }

    private fun readtxt(fileName: String): String {
        return Files.getResourceAsText(fileName).toString()
    }
}
