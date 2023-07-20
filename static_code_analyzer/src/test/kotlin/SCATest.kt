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
        val jsonReader = SCAJsonReader(File(""))
        val sca: ASTNodeConsumer = StaticCodeAnalyzer(
            jsonReader.readJson(),
            setup(Files.getResourceAsFile("mock_text_declaration_Snake_Case.txt")!!)
        )
        val result = sca.consume()
        Assertions.assertEquals(result, ConsumerResponseError("Invalid typing format in row 1 4"))
    }

    @Test
    fun camelCaseSuccesTest() {
        val jsonReader = SCAJsonReader(File(""))
        val sca: ASTNodeConsumer = StaticCodeAnalyzer(
            jsonReader.readJson(),
            setup(Files.getResourceAsFile("mock_text_declaration_Camel_Case.txt")!!)
        )
        val result = sca.consume()
        Assertions.assertEquals(result, ConsumerResponseSuccess(null))
    }

    @Test
    fun snakeCaseTest() {
        val jsonReader = SCAJsonReader(Files.getResourceAsFile("snakeCase.json")!!)
        val sca: ASTNodeConsumer = StaticCodeAnalyzer(
            jsonReader.readJson(),
            setup(Files.getResourceAsFile("mock_text_declaration_Camel_Case.txt")!!)
        )
        val result = sca.consume()
        Assertions.assertEquals(result, ConsumerResponseError("Invalid typing format in row 1 4"))
    }

    @Test
    fun snakeCaseSuccesTest() {
        val jsonReader = SCAJsonReader(Files.getResourceAsFile("snakeCase.json")!!)
        val sca: ASTNodeConsumer = StaticCodeAnalyzer(
            jsonReader.readJson(),
            setup(Files.getResourceAsFile("mock_text_declaration_Snake_Case.txt")!!)
        )
        val result = sca.consume()
        Assertions.assertEquals(result, ConsumerResponseSuccess(null))
    }

    @Test
    fun methodNoExpresionTest() {
        val jsonReader = SCAJsonReader(Files.getResourceAsFile("methodNoExpresion.json")!!)
        val sca: ASTNodeConsumer = StaticCodeAnalyzer(
            jsonReader.readJson(),
            setup(Files.getResourceAsFile("mock_text_declaration_Method_No_Expresion.txt")!!)
        )
        val result = sca.consume()
        Assertions.assertEquals(result, ConsumerResponseSuccess(null))
    }

    @Test
    fun methodNoExpresionErrorTest() {
        val jsonReader = SCAJsonReader(Files.getResourceAsFile("methodNoExpresion.json")!!)
        val sca: ASTNodeConsumer = StaticCodeAnalyzer(
            jsonReader.readJson(),
            setup(Files.getResourceAsFile("mock_text_declaration_Method_No_Expresion_Error.txt")!!)
        )
        val result = sca.consume()
        Assertions.assertEquals(result, ConsumerResponseError("Invalid expression in row 1 0"))
    }

    @Test
    fun inputNoExpresionTest() {
        val file = Files.getResourceAsFile("inputNoExpresion.json")!!
        val jsonReader = SCAJsonReader(file)
        val sca: ASTNodeConsumer = StaticCodeAnalyzer(
            jsonReader.readJson(),
            setup(Files.getResourceAsFile("mock_text_declaration_Input_No_Expresion.txt")!!)
        )
        val result = sca.consume()
        Assertions.assertEquals(result, ConsumerResponseError("Invalid expression in row 1 23"))
    }

    @Test
    fun inputNoExpresionMethodTest() {
        val file = Files.getResourceAsFile("inputNoExpresion.json")!!
        val jsonReader = SCAJsonReader(file)
        val sca: ASTNodeConsumer = StaticCodeAnalyzer(
            jsonReader.readJson(),
            setup(Files.getResourceAsFile("mock_text_declaration_Input_No_Expresion_Method.txt")!!)
        )
        val result = sca.consume()
        Assertions.assertEquals(result, ConsumerResponseSuccess(null))
    }

    private fun setup(src: File): ASTNodeProvider {
        val tokenMap = TokenReadersProvider().getTokenMap("1.0")
        val tokenProvider = FileTokenProvider(src, RegularLexer(tokenMap!!))
        return ASTNodeProviderImpl(tokenProvider, RegularParser.createDefaultParser())
    }
}
