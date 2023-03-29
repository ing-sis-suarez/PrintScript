package lexer

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import token.Token


class LexerTest {

    @Test
    fun testAsignSyntax() {
        val evaluatedText = printScriptEvaluateText("mock_text_assignation.txt")
        Assertions.assertEquals(Files.getResourceAsText("mock_text_assignation_result.txt"), lexerResultsToString(evaluatedText))
    }
    @Test
    fun testOperationSyntax() {
        val evaluatedText = printScriptEvaluateText("mock_text_operations.txt")
        Assertions.assertEquals(Files.getResourceAsText("mock_text_operations_result.txt"), lexerResultsToString(evaluatedText))
    }

    @Test
    fun testPrintSyntax() {
        val evaluatedText = printScriptEvaluateText("mock_text_print.txt")
        Assertions.assertEquals(Files.getResourceAsText("mock_text_print_result.txt"), lexerResultsToString(evaluatedText))
    }

    @Test
    fun testInvalidTokens(){
        val evaluatedText = printScriptEvaluateText("mock_text_with_invalid_char.txt")
        Assertions.assertEquals(Files.getResourceAsText("mock_text_with_invalid_char_result.txt"), lexerResultsToString(evaluatedText))
    }
    @Test
    fun testUnclosedString(){
        val evaluatedText = printScriptEvaluateText("mock_text_with_unclosed_string.txt")
        Assertions.assertEquals(Files.getResourceAsText("mock_text_with_unclosed_string_result.txt"), lexerResultsToString(evaluatedText))
    }
    @Test
    fun xd(){
        val evaluatedText = printScriptEvaluateText("declaration.txt")
        java.io.File("C:\\Users\\Usuario\\IdeaProjects\\PrintScript\\lexer\\src\\test\\resources\\declaration.txt").writeText(lexerResultsToString(evaluatedText))
    }

    private fun printScriptEvaluateText(fileName: String): List<Token>{
        val mockText = Files.getResourceAsText(fileName).toString()
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
}