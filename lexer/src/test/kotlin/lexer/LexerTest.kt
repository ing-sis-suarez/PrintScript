package lexer

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test


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
        val mockText = getResourceAsText("mock_text_with_unclosed_string.txt").toString()
        val tokenMap = TokenReadersProvider().getTokenMap("PrintScript") ?: return
        val lexer: Lexer = RegularLexer(tokenMap)
        Assertions.assertThrows(MalformedStringException:: class.java){ lexer.stringTokenizer(mockText)}
    }

    private fun printScriptEvaluateText(fileName: String): List<Token>{
        val mockText = getResourceAsText(fileName).toString()
        val tokenMap = TokenReadersProvider().getTokenMap("PrintScript") ?: return emptyList()
        val lexer: Lexer = RegularLexer(tokenMap)
        return lexer.stringTokenizer(mockText)
    }
    private fun lexerResultsToString(results: List<Token>): String{
        var toString = "["
        for (i in results.indices){
            if (i == results.size - 1) toString += "Token(${results[i].id}, ${results[i].type}, ${results[i].location}, ${results[i].originalValue})]"
            toString += "Token(${results[i].id}, ${results[i].type}, ${results[i].location}, ${results[i].originalValue}), "
        }
        return toString
    }

    private fun getResourceAsText(path: String): String? {
        return this::class.java.classLoader.getResource(path)?.readText()
    }

}