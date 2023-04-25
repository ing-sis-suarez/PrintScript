import interpreter.Interpret
import interpreter.Interpreter
import interpreter.InterpreterSuccessResponse
import lexer.RegularLexer
import lexer.TokenReadersProvider
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import parser.RegularParser
import provider.ASTNodeProvider
import provider.ASTNodeProviderImpl
import java.io.File

class InterpreterTest {
    @Test
    fun declarationinitializationTest() {
        val interpret: Interpreter = Interpret(setup(Files.getResourceAsFile("mock_text_declaration_initialization.txt")!!))
        var result = interpret.interpret()
        while (result is InterpreterSuccessResponse && result.message == null) {
            result = interpret.interpret()
        }

        Assertions.assertEquals(result, InterpreterSuccessResponse(Files.getResourceAsText("mock_text_declaration_initialization_result.txt").toString()))
    }

    @Test
    fun declarationinitializationOperationOperationTest() {
        val interpret: Interpreter = Interpret(setup(Files.getResourceAsFile("mock_text_declaration_initialization_operation.txt")!!))
        var result = interpret.interpret()
        while (result is InterpreterSuccessResponse && result.message == null) {
            result = interpret.interpret()
        }

        Assertions.assertEquals(result, InterpreterSuccessResponse(Files.getResourceAsText("mock_text_declaration_initialization_operation_result.txt").toString()))
    }

    private fun setup(src: File): ASTNodeProvider {
        val tokenMap = TokenReadersProvider().getTokenMap("1.0")
        val tokenProvider = FileTokenProvider(src, RegularLexer(tokenMap!!))
        return ASTNodeProviderImpl(tokenProvider, RegularParser.createDefaultParser())
    }
}
