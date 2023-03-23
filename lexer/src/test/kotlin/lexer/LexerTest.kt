package lexer

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import utilities.Token


class LexerTest {

    @Test
    fun testAsignSyntax() {
        val evaluatedText = printScriptEvaluateText("mock_text_assignation.txt")
        Assertions.assertEquals(getResourceAsText("mock_text_assignation_result.txt"), lexerResultsToString(evaluatedText))
    }
    @Test
    fun testOperationSyntax() {
        val evaluatedText = printScriptEvaluateText("mock_text_operations.txt")
        Assertions.assertEquals(getResourceAsText("mock_text_operations_result.txt"), lexerResultsToString(evaluatedText))
    }

    @Test
    fun testPrintSyntax() {
        val evaluatedText = printScriptEvaluateText("mock_text_print.txt")
        Assertions.assertEquals(getResourceAsText("mock_text_print_result.txt"), lexerResultsToString(evaluatedText))
    }

    @Test
    fun testInvalidTokens(){
        val evaluatedText = printScriptEvaluateText("mock_text_with_invalid_char.txt")
        Assertions.assertEquals(getResourceAsText("mock_text_with_invalid_char_result.txt"), lexerResultsToString(evaluatedText))
    }
    @Test
    fun testUnclosedString(){
        val evaluatedText = printScriptEvaluateText("mock_text_with_unclosed_string.txt")
        Assertions.assertEquals(getResourceAsText("mock_text_with_unclosed_string_result.txt"), lexerResultsToString(evaluatedText))
    }

    private fun printScriptEvaluateText(fileName: String): List<Token>{
        val mockText = getResourceAsText(fileName).toString()
        val tokenMap = TokenReadersProvider().getTokenMap("PrintScript") ?: return emptyList()
        val lexer: Lexer = RegularLexer(tokenMap)
        return lexer.lex(mockText)
    }
    private fun lexerResultsToString(results: List<Token>): String{
        var toString = ""
        for (token in results){
            toString += token.toString() + "\r\n"
        }
        return toString
    }

    private fun getResourceAsText(path: String): String? {
        return this::class.java.classLoader.getResource(path)?.readText()
    }

}