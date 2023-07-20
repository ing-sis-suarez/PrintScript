import consumer.ASTNodeConsumerInterpreter
import consumer.ConsumerResponseError
import consumer.ConsumerResponseSuccess
import interpreter.Interpret
import lexer.RegularLexer
import lexer.TokenReadersProvider
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import parser.RegularParser
import provider.ASTNodeProvider
import provider.ASTNodeProviderImpl
import java.io.File

class InterpreterTest {
    @Test
    fun declarationInitializationTest() {
        val interpret: ASTNodeConsumerInterpreter = Interpret(setup(Files.getResourceAsFile("mock_text_declaration_initialization.txt")!!))
        var result = interpret.consume()
        while (result is ConsumerResponseSuccess && result.msg == null) {
            result = interpret.consume()
        }

        assertEquals(ConsumerResponseSuccess(Files.getResourceAsText("mock_text_declaration_initialization_result.txt").toString()), result)
    }

    @Test()
    fun declarationInitializationTestError() {
        val interpret: ASTNodeConsumerInterpreter = Interpret(setup(Files.getResourceAsFile("mock_text_declaration_initialization_error.txt")!!))
        var result = interpret.consume()
        while (result is ConsumerResponseSuccess && result.msg == null) {
            result = interpret.consume()
        }

        assertEquals(ConsumerResponseError(Files.getResourceAsText("mock_text_declaration_initialization_error_result.txt").toString()), result)
    }

    @Test
    fun declarationInitializationOperationTest() {
        val interpret: ASTNodeConsumerInterpreter = Interpret(setup(Files.getResourceAsFile("mock_text_declaration_initialization_operation.txt")!!))
        var result = interpret.consume()
        while (result is ConsumerResponseSuccess && result.msg == null) {
            result = interpret.consume()
        }

        assertEquals(result, ConsumerResponseSuccess(Files.getResourceAsText("mock_text_declaration_initialization_operation_result.txt").toString()))
    }

    @Test
    fun declarationInitializationOperationStringTest() {
        val interpret: ASTNodeConsumerInterpreter = Interpret(setup(Files.getResourceAsFile("mock_text_declaration_initialization_operation_string.txt")!!))
        var result = interpret.consume()
        while (result is ConsumerResponseSuccess && result.msg == null) {
            result = interpret.consume()
        }

        assertEquals(result, ConsumerResponseSuccess(Files.getResourceAsText("mock_text_declaration_initialization_operation_string_result.txt").toString()))
    }

    @Test
    fun declarationInitializationOperationOperationVariableTest() {
        val interpret: ASTNodeConsumerInterpreter = Interpret(setup(Files.getResourceAsFile("mock_text_declaration_initialization_operation_variable.txt")!!))
        var result = interpret.consume()
        while (result is ConsumerResponseSuccess && result.msg == null) {
            result = interpret.consume()
        }

        assertEquals(ConsumerResponseSuccess(Files.getResourceAsText("mock_text_declaration_initialization_operation_variable_result.txt").toString()), result)
    }

    @Test
    fun declarationInAssignationTest() {
        val interpret: ASTNodeConsumerInterpreter = Interpret(setup(Files.getResourceAsFile("mock_text_declaration_assignation.txt")!!))
        var result = interpret.consume()
        while (result is ConsumerResponseSuccess && result.msg == null) {
            result = interpret.consume()
        }

        assertEquals(ConsumerResponseSuccess(Files.getResourceAsText("mock_text_declaration_assignation_result.txt").toString()), result)
    }

    @Test
    fun assignationErrorTest() {
        val interpret: ASTNodeConsumerInterpreter = Interpret(setup(Files.getResourceAsFile("mock_text_assignation_error.txt")!!))
        var result = interpret.consume()
        while (result is ConsumerResponseSuccess && result.msg == null) {
            result = interpret.consume()
        }

        assertEquals(ConsumerResponseError(Files.getResourceAsText("mock_text_assignation_error_result.txt").toString()), result)
    }

    @Test
    fun assignationWithoutVariableErrorTest() {
        val interpret: ASTNodeConsumerInterpreter = Interpret(setup(Files.getResourceAsFile("mock_text_assignation_without_variable_error.txt")!!))
        var result = interpret.consume()
        while (result is ConsumerResponseSuccess && result.msg == null) {
            result = interpret.consume()
        }

        assertEquals(ConsumerResponseError(Files.getResourceAsText("mock_text_assignation_without_variable_error_result.txt").toString()), result)
    }

    @Test
    fun ifConditionTest() {
        val interpret: ASTNodeConsumerInterpreter = Interpret(setup(Files.getResourceAsFile("mock_text_if_condition.txt")!!))
        var result = interpret.consume()
        while (result is ConsumerResponseSuccess && result.msg == null) {
            result = interpret.consume()
        }

        assertEquals(ConsumerResponseSuccess(Files.getResourceAsText("mock_text_if_condition_result.txt").toString()), result)
    }

    @Test
    fun ifConditionAssignationErrorTest() {
        val interpret: ASTNodeConsumerInterpreter = Interpret(setup(Files.getResourceAsFile("mock_text_if_condition_assignation_error.txt")!!))
        var result = interpret.consume()
        while (result is ConsumerResponseSuccess && result.msg == null) {
            result = interpret.consume()
        }

        assertEquals(ConsumerResponseError(Files.getResourceAsText("mock_text_if_condition_assignation_error_result.txt").toString()), result)
    }

    @Test
    fun elseConditionTest() {
        val interpret: ASTNodeConsumerInterpreter = Interpret(setup(Files.getResourceAsFile("mock_text_else_condition.txt")!!))
        var result = interpret.consume()
        while (result is ConsumerResponseSuccess && result.msg == null) {
            result = interpret.consume()
        }

        assertEquals(ConsumerResponseSuccess(Files.getResourceAsText("mock_text_else_condition_result.txt").toString()), result)
    }

    @Test
    fun invalidElseConditionErrorTest() {
        val interpret: ASTNodeConsumerInterpreter = Interpret(setup(Files.getResourceAsFile("mock_text_invalid_else_condition_error.txt")!!))
        var result = interpret.consume()
        while (result is ConsumerResponseSuccess && result.msg == null) {
            result = interpret.consume()
        }

        assertEquals(ConsumerResponseError(Files.getResourceAsText("mock_text_invalid_else_condition_error_result.txt").toString()), result)
    }

    @Test
    fun ifConditionError() {
        val interpret: ASTNodeConsumerInterpreter = Interpret(setup(Files.getResourceAsFile("mock_text_if_condition_error.txt")!!))
        var result = interpret.consume()
        while (result is ConsumerResponseSuccess && result.msg == null) {
            result = interpret.consume()
        }

        assertEquals(ConsumerResponseError(Files.getResourceAsText("mock_text_if_condition_error_result.txt").toString()), result)
    }

    @Test
    fun constTest() {
        val interpret: ASTNodeConsumerInterpreter = Interpret(setup(Files.getResourceAsFile("mock_text_const.txt")!!))
        var result = interpret.consume()
        while (result is ConsumerResponseSuccess && result.msg == null) {
            result = interpret.consume()
        }

        assertEquals(ConsumerResponseSuccess(Files.getResourceAsText("mock_text_const_result.txt").toString()), result)
    }

    @Test
    fun constTestError() {
        val interpret: ASTNodeConsumerInterpreter = Interpret(setup(Files.getResourceAsFile("mock_text_const_error.txt")!!))
        var result = interpret.consume()
        while (result is ConsumerResponseSuccess && result.msg == null) {
            result = interpret.consume()
        }

        assertEquals(ConsumerResponseError(Files.getResourceAsText("mock_text_const_error_result.txt").toString()), result)
    }

    @Test
    fun methodCall() {
        val interpret: ASTNodeConsumerInterpreter = Interpret(setup(Files.getResourceAsFile("mock_text_method_call.txt")!!))
        var result = interpret.consume()
        while (result is ConsumerResponseSuccess && result.msg == null) {
            result = interpret.consume()
        }

        assertEquals(ConsumerResponseSuccess(Files.getResourceAsText("mock_text_method_call_result.txt").toString()), result)
    }

    private fun setup(src: File): ASTNodeProvider {
        val tokenMap = TokenReadersProvider().getTokenMap("1.1")
        val tokenProvider = FileTokenProvider(src, RegularLexer(tokenMap!!))
        return ASTNodeProviderImpl(tokenProvider, RegularParser.createDefaultParser())
    }
}
