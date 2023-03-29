package parser

import Files
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import token.Location
import token.Token
import token.TokenType

class RegularParserTest {
    
    @Test
    fun declarationInitializationTest(){
        runCorrectResultTest("declarationInitialization")
    }

    @Test
    fun declarationTest(){
        runCorrectResultTest("declaration")
    }

    @Test
    fun methodCallTest(){
        runCorrectResultTest("methodCall")
    }

    @Test
    fun assignationTest(){
        runCorrectResultTest("assignation")
    }

    @Test
    fun operationTest(){
        runCorrectResultTest("operation")
    }

    private fun runCorrectResultTest(fileName: String) {
        val tokens = readTokens("correctStatements/$fileName.txt")
        val parser: Parser = RegularParser(StatementParserProvider().getParserList())
        val result = parser.parse(tokens)
        Assertions.assertEquals(
            Files.getResourceAsText("correctStatements/${fileName}_result.txt").toString(),
            result.toString(),
            fileName
        )
    }

    private fun readTokens(fileName: String): List<Token> {
        val rawText = Files.getResourceAsText(fileName)
        val tokenList = breakIntoLines(rawText.toString())
        return tokenList.map { tokenString -> toToken(tokenString) }
    }

    private fun toToken(tokenString: String): Token { // reads a token.toString() and returns a token
        val tokenRegex = """Token\(id=(\d+),\s*type=([A-Z_]+),\s*location=Location\(row=(\d+),\s*column=(\d+)\),\s*originalValue=(.*)\)""".toRegex()

        val matchResult = tokenRegex.matchEntire(tokenString)
        if (matchResult != null) {
            val (id, typeStr, row, column, originalValue) = matchResult.destructured
            val type = TokenType.valueOf(typeStr)
            val location = Location(row.toInt(), column.toInt())
            return Token(id.toInt(), type, location, originalValue.trim())
        }
        throw IllegalArgumentException("Invalid token string: $tokenString")
    }

    private fun breakIntoLines(rawText: String): List<String> {
        return rawText.trim().split(System.lineSeparator())
    }
}