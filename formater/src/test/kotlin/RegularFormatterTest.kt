
import config.FormatterConfig
import consumer.ConsumerResponseSuccess
import formatter.RegularFormatter
import lexer.RegularLexer
import lexer.TokenReadersProvider
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import parser.RegularParser
import provider.ASTNodeProviderImpl
import java.io.File
import java.lang.StringBuilder

class RegularFormatterTest {

    @Test
    fun declarationTest() {
        val formatter = RegularFormatter(setup(Files.getResourceAsFile("mock_text_declaration.txt")!!), FormatterConfig())
        val result = formatter.consume() as ConsumerResponseSuccess

        assertEquals(Files.getResourceAsText("mock_text_declaration_result.txt"), result.msg)
    }

//    @Test
//    fun declarationAssignationTest() {
//        val formatter = RegularFormatter(setup(Files.getResourceAsFile("mock_text_declaration_assignation.txt")!!), FormatterConfig())
//        var response = formatter.consume()
//        val result = StringBuilder()
//        while (response is ConsumerResponseSuccess) {
//            result.append(response.msg)
//            response = formatter.consume()
//        }
//        assertEquals(Files.getResourceAsText("mock_text_declaration_assignation_result.txt"), result.toString())
//    }

    @Test
    fun declarationInitializationOperation() {
        val formatter = RegularFormatter(setup(Files.getResourceAsFile("mock_text_declaration_initialization_operation.txt")!!), FormatterConfig())
        val result = formatter.consume() as ConsumerResponseSuccess
        assertTrue(true, "Result is not ConsumerResponseSuccess")
        assertEquals(Files.getResourceAsText("mock_text_declaration_initialization_operation_result.txt"), result.msg)
    }

    @Test
    fun methodCallTest() {
        val formatter = RegularFormatter(setup(Files.getResourceAsFile("mock_text_print.txt")!!), FormatterConfig())
        val result = formatter.consume() as ConsumerResponseSuccess

        assertEquals(Files.getResourceAsText("mock_text_print_result.txt"), result.msg)
    }

    @Test
    fun conditionTest() {
        val formatter = RegularFormatter(setup(Files.getResourceAsFile("mock_text_condition.txt")!!), FormatterConfig())
        val result = StringBuilder()
        while (true) {
            val response = formatter.consume()
            if (response is ConsumerResponseSuccess) {
                result.append(response.msg)
            } else {
                break
            }
        }
        assertEquals(Files.getResourceAsText("mock_text_condition_result.txt"), result.toString())
    }

    @Test
    fun declarationTestUsingAnotherConfig() {
        val formatter = RegularFormatter(setup(Files.getResourceAsFile("mock_text_declaration.txt")!!), FormatterConfig.loadFromJsonFile(Files.getResourceAsFile("formatter_rules.json")!!))
        val result = formatter.consume() as ConsumerResponseSuccess

        assertEquals(Files.getResourceAsText("mock_text_declaration_result_using_another_config.txt"), result.msg)
    }

    private fun setup(src: File): ASTNodeProviderImpl {
        val tokenMap = TokenReadersProvider().getTokenMap("1.1")
        val tokenProvider = FileTokenProvider(src, RegularLexer(tokenMap!!))
        return ASTNodeProviderImpl(tokenProvider, RegularParser.createDefaultParser())
    }
}
