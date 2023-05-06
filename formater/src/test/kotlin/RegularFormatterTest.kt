
import consumer.ConsumerResponseSuccess
import formatter.RegularFormatter
import lexer.RegularLexer
import lexer.TokenReadersProvider
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import parser.RegularParser
import provider.ASTNodeProviderImpl
import rules.FormatterConfig
import java.io.File

class RegularFormatterTest {

    @Test
    fun declarationTest() {
        val formatter = RegularFormatter(setup(Files.getResourceAsFile("mock_text_declaration.txt")!!), FormatterConfig())
        val result = formatter.consume() as ConsumerResponseSuccess

        assertEquals(Files.getResourceAsText("mock_text_declaration_result.txt"), result.msg)
    }

    @Test
    fun declarationInitializationTest() {
        val formatter = RegularFormatter(setup(Files.getResourceAsFile("mock_text_declaration_initialization.txt")!!), FormatterConfig())
        val result = formatter.consume() as ConsumerResponseSuccess

        assertEquals(Files.getResourceAsText("mock_text_declaration_initialization_result.txt"), result.msg)
    }

    @Test
    fun declarationInitializationOperation() {
        val formatter = RegularFormatter(setup(Files.getResourceAsFile("mock_text_declaration_initialization_operation.txt")!!), FormatterConfig())
        val result = formatter.consume()
        assertTrue(result is ConsumerResponseSuccess, "Result is not ConsumerResponseSuccess")

        assertEquals(Files.getResourceAsText("mock_text_declaration_initialization_operation_result.txt"), (result as ConsumerResponseSuccess).msg)
    }

    @Test
    fun methodCallTest() {
        val formatter = RegularFormatter(setup(Files.getResourceAsFile("mock_text_print.txt")!!), FormatterConfig())
        val result = formatter.consume() as ConsumerResponseSuccess

        assertEquals(Files.getResourceAsText("mock_text_print_result.txt"), result.msg)
    }

    private fun setup(src: File): ASTNodeProviderImpl {
        val tokenMap = TokenReadersProvider().getTokenMap("1.0")
        val tokenProvider = FileTokenProvider(src, RegularLexer(tokenMap!!))
        return ASTNodeProviderImpl(tokenProvider, RegularParser.createDefaultParser())
    }
}
