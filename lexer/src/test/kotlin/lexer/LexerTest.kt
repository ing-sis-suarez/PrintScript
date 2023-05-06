package lexer

import FileTokenProvider
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import token.Token
import java.io.File

class LexerTest {

    @Test
    fun testAsignSyntax() {
        val evaluatedText = printScriptEvaluateText(Files.getResourceAsFile("mock_text_assignation.txt")!!)
        Assertions.assertEquals(Files.getResourceAsText("mock_text_assignation_result.txt"), lexerResultsToString(evaluatedText))
    }

    @Test
    fun testOperationSyntax() {
        val evaluatedText = printScriptEvaluateText(Files.getResourceAsFile("mock_text_operations.txt")!!)
        Assertions.assertEquals(Files.getResourceAsText("mock_text_operations_result.txt"), lexerResultsToString(evaluatedText))
    }

    @Test
    fun testPrintSyntax() {
        val evaluatedText = printScriptEvaluateText(Files.getResourceAsFile("mock_text_print.txt")!!)
        Assertions.assertEquals(Files.getResourceAsText("mock_text_print_result.txt"), lexerResultsToString(evaluatedText))
    }

    @Test
    fun testInvalidTokens() {
        val evaluatedText = printScriptEvaluateText(Files.getResourceAsFile("mock_text_with_invalid_char.txt")!!)
        Assertions.assertEquals(Files.getResourceAsText("mock_text_with_invalid_char_result.txt"), lexerResultsToString(evaluatedText))
    }

    @Test
    fun testUnclosedString() {
        val evaluatedText = printScriptEvaluateText(Files.getResourceAsFile("mock_text_with_unclosed_string.txt")!!)
        Assertions.assertEquals(Files.getResourceAsText("mock_text_with_unclosed_string_result.txt"), lexerResultsToString(evaluatedText))
    }
}

private fun printScriptEvaluateText(file: File): List<Token> {
    val lexer: Lexer = RegularLexer(TokenReadersProvider().getTokenMap("1.0")!!)
    val tokenProvider = FileTokenProvider(file, lexer)
    val tokenList = mutableListOf<Token>()
    while (true) {
        val token = tokenProvider.readToken() ?: break
        tokenList.add(token)
    }
    return tokenList
}

private fun lexerResultsToString(results: List<Token>): String {
    var toString = ""
    for (token in results) {
        toString += token.toString() + System.lineSeparator()
    }
    return toString
}
