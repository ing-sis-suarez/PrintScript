import consumer.ASTNodeConsumerInterpreter
import consumer.ConsumerResponseError
import consumer.ConsumerResponseSuccess
import interpreter.Interpret
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
        val interpret: ASTNodeConsumerInterpreter = Interpret(setup(Files.getResourceAsFile("mock_text_declaration_initialization.txt")!!))
        var result = interpret.consume()
        while (result is ConsumerResponseSuccess && result.msg == null) {
            result = interpret.consume()
        }

        Assertions.assertEquals(result, ConsumerResponseSuccess(Files.getResourceAsText("mock_text_declaration_initialization_result.txt").toString()))
    }

    @Test
    fun declarationinitializationOperationOperationTest() {
        val interpret: ASTNodeConsumerInterpreter = Interpret(setup(Files.getResourceAsFile("mock_text_declaration_initialization_operation.txt")!!))
        var result = interpret.consume()
        while (result is ConsumerResponseSuccess && result.msg == null) {
            result = interpret.consume()
        }

        Assertions.assertEquals(result, ConsumerResponseSuccess(Files.getResourceAsText("mock_text_declaration_initialization_operation_result.txt").toString()))
    }

    @Test
    fun declarationinitializationOperationOperationStringTest() {
        val interpret: ASTNodeConsumerInterpreter = Interpret(setup(Files.getResourceAsFile("mock_text_declaration_initialization_operation.txt")!!))
        var result = interpret.consume()
        while (result is ConsumerResponseSuccess && result.msg == null) {
            result = interpret.consume()
        }

        Assertions.assertEquals(result, ConsumerResponseSuccess(Files.getResourceAsText("mock_text_declaration_initialization_operation_result.txt").toString()))
    }

    @Test
    fun declarationinAssignationTest() {
        val interpret: ASTNodeConsumerInterpreter = Interpret(setup(Files.getResourceAsFile("mock_text_declaration_assignation.txt")!!))
        var result = interpret.consume()
        while (result is ConsumerResponseSuccess && result.msg == null) {
            result = interpret.consume()
        }

        Assertions.assertEquals(result, ConsumerResponseSuccess(Files.getResourceAsText("mock_text_declaration_assignation_result.txt").toString()))
    }

    @Test
    fun ifConditionTest() {
        val interpret: ASTNodeConsumerInterpreter = Interpret(setup(Files.getResourceAsFile("mock_text_if_condition.txt")!!))
        var result = interpret.consume()
        while (result is ConsumerResponseSuccess && result.msg == null) {
            result = interpret.consume()
        }

        Assertions.assertEquals(result, ConsumerResponseSuccess(Files.getResourceAsText("mock_text_if_condition_result.txt").toString()))
    }

    @Test
    fun elseConditionTest() {
        val interpret: ASTNodeConsumerInterpreter = Interpret(setup(Files.getResourceAsFile("mock_text_if_condition.txt")!!))
        var result = interpret.consume()
        while (result is ConsumerResponseSuccess && result.msg == null) {
            result = interpret.consume()
        }

        Assertions.assertEquals(result, ConsumerResponseSuccess(Files.getResourceAsText("mock_text_if_condition_result.txt").toString()))
    }

    @Test
    fun ifConditionError() {
        val interpret: ASTNodeConsumerInterpreter = Interpret(setup(Files.getResourceAsFile("mock_text_if_condition_error.txt")!!))
        var result = interpret.consume()
        while (result is ConsumerResponseSuccess && result.msg == null) {
            result = interpret.consume()
        }

        Assertions.assertEquals(result, ConsumerResponseError(Files.getResourceAsText("mock_text_if_condition_error_result.txt").toString()))
    }

    @Test
    fun constTest() {
        val interpret: ASTNodeConsumerInterpreter = Interpret(setup(Files.getResourceAsFile("mock_text_const.txt")!!))
        var result = interpret.consume()
        while (result is ConsumerResponseSuccess && result.msg == null) {
            result = interpret.consume()
        }

        Assertions.assertEquals(result, ConsumerResponseSuccess(Files.getResourceAsText("mock_text_const_result.txt").toString()))
    }

    @Test
    fun constTestError() {
        val interpret: ASTNodeConsumerInterpreter = Interpret(setup(Files.getResourceAsFile("mock_text_const_error.txt")!!))
        var result = interpret.consume()
        while (result is ConsumerResponseSuccess && result.msg == null) {
            result = interpret.consume()
        }

        Assertions.assertEquals(result, ConsumerResponseError(Files.getResourceAsText("mock_text_const_error_result.txt").toString()))
    }

    private fun setup(src: File): ASTNodeProvider {
        val tokenMap = TokenReadersProvider().getTokenMap("1.1")
        val tokenProvider = FileTokenProvider(src, RegularLexer(tokenMap!!))
        return ASTNodeProviderImpl(tokenProvider, RegularParser.createDefaultParser())
    }
}
