package parser

import FileTokenProvider
import exceptions.MalformedStructureException
import exceptions.UnexpectedTokenException
import lexer.RegularLexer
import lexer.TokenReadersProvider
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import provider.ASTNodeProviderImpl
import token.Location
import token.Token
import token.TokenType

class RegularParserTest {

    @Test
    fun declarationInitializationTest() {
        runCorrectResultTest("declarationInitialization")
    }

    @Test
    fun declarationTest() {
        runCorrectResultTest("declaration")
    }

    @Test
    fun declarationConstTest() {
        runCorrectResultTest("declaration_const")
    }

    @Test
    fun methodCallTest() {
        runCorrectResultTest("methodCall")
    }

    @Test
    fun assignationTest() {
        runCorrectResultTest("assignation")
    }

    @Test
    fun operationTest() {
        runCorrectResultTest("operation")
    }

    @Test
    fun conditionTest() {
        runCorrectResultTest("mock_text_if_condition")
    }

    @Test
    fun conditionElseTest() {
        runCorrectResultTest("mock_text_else_condition")
    }

    @Test
    fun declarationInput() {
        runCorrectResultTest("assignation_input")
    }

    @Test
    fun booleanOperation() {
        runCorrectResultTest("operation_boolean")
    }

    @Test
    fun inputTest() {
        runCorrectResultTest("mock_text_input")
    }

    @Test
    fun minusTest() {
        runCorrectResultTest("minus")
    }

    @Test
    fun twoOperatorsTogetherTest() {
        runIncorrectResultTest(
            "two_operator_together",
            UnexpectedTokenException::class.java,
            "Unexpected token at: 0, 24"
        )
    }

    @Test
    fun unclosedParenthesisTest() {
        runIncorrectResultTest(
            "unclosed_parenthesis",
            UnexpectedTokenException::class.java,
            "Unclosed parenthesis at: 0"
        )
        runIncorrectResultTest(
            "extra_right_parenthesis",
            UnexpectedTokenException::class.java,
            "Unexpected token at: 0, 22"
        )
    }

    @Test
    fun invalidDeclarationInitializationSyntaxTest() {
        runIncorrectResultTest(
            "invalid_declarationInitialization_syntax",
            UnexpectedTokenException::class.java,
            "Unexpected token at: 0, 10"
        )
    }

    @Test
    fun invalidDeclarationSyntaxTest() {
        runIncorrectResultTest(
            "invalid_declaration_syntax",
            UnexpectedTokenException::class.java,
            "Unexpected token at: 0, 10"
        )
    }

    @Test
    fun invalidAssignationSyntaxTest() {
        runIncorrectResultTest(
            "invalid_assignation_syntax",
            MalformedStructureException::class.java,
            "Could not recognize syntax"
        )
    }

    @Test
    fun invalidMethodCallSyntaxTest() {
        runIncorrectResultTest(
            "invalid_methodCall_syntax",
            MalformedStructureException::class.java,
            "Missing arguments at line: 0"
        )
    }

    @Test
    fun providerTest() {
        val tokenMap = TokenReadersProvider().getTokenMap("1.1")
        val lexer: RegularLexer? = tokenMap?.let { RegularLexer(it) }
        val ast = ASTNodeProviderImpl(FileTokenProvider(Files.getResourceAsFile("mock_text_assignation.txt")!!, lexer!!), RegularParser.createDefaultParser())
        val astNode1 = ast.readASTNode()
        assertNotNull(astNode1)
        val astNode2 = ast.readASTNode()
        assertNotNull(astNode2)
    }

    private fun <T : Exception> runIncorrectResultTest(
        fileName: String,
        exceptionType: Class<T>,
        errorMessage: String
    ) {
        val tokens = readTokens("incorrectStatements/$fileName.txt")
        val parser: Parser = RegularParser.createDefaultParser()
        val exception = Assertions.assertThrows(exceptionType) {
            parser.parse(tokens)
        }
        Assertions.assertEquals(errorMessage, exception.message)
    }

    private fun runCorrectResultTest(fileName: String) {
        val tokens = readTokens("correctStatements/$fileName.txt")
        val parser: Parser = RegularParser.createDefaultParser()
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
        val tokenRegex =
            """Token\(type=([A-Z_]+),\s*location=Location\(row=(\d+),\s*column=(\d+)\),\s*originalValue=(.*),\s*length=(\d+)\)""".toRegex()

        val matchResult = tokenRegex.matchEntire(tokenString)
        if (matchResult != null) {
            val (typeStr, row, column, originalValue, length) = matchResult.destructured
            val type = TokenType.valueOf(typeStr)
            val location = Location(row.toInt(), column.toInt())
            return Token(type, location, originalValue.trim(), length.toInt())
        }
        throw IllegalArgumentException("Invalid token string: $tokenString")
    }

    private fun breakIntoLines(rawText: String): List<String> {
        return rawText.trim().split(System.lineSeparator())
    }
}
