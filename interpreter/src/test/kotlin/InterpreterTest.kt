import ast.node.ASTNode
import lexer.Lexer
import lexer.RegularLexer
import lexer.TokenReadersProvider
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import parser.Parser
import parser.RegularParser
import token.Token

class InterpreterTest {
    @Test
    fun declarationTest() {
        val statement = readtxt("mock_text_declaration.txt")
        val tokenList = lex(statement)
        val astList = parse(tokenList)
        println(astList.first().toString())
        val evaluator = Evaluator()
        evaluator.executionReader(astList)
        Assertions.assertEquals(evaluator.variables.toString(), readtxt("mock_text_declaration_result.txt"))
    }

    @Test
    fun declarationinitializationOperationTest() {
        val statement = readtxt("mock_text_declaration_initialization.txt")
        val tokenList = lex(statement)
        val astList = parse(tokenList)
        val evaluator = Evaluator()
        evaluator.executionReader(astList)
        Assertions.assertEquals(evaluator.variables.toString(), readtxt("mock_text_declaration_initialization_result.txt"))
    }

    @Test
    fun declarationinitializationOperationOperationTest() {
        val statement = readtxt("mock_text_declaration_initialization_operation.txt")
        val tokenList = lex(statement)
        val astList = parse(tokenList)
        val evaluator = Evaluator()
        evaluator.executionReader(astList)
        Assertions.assertEquals(evaluator.variables.toString(), readtxt("mock_text_declaration_initialization_operation_result.txt"))
    }

    @Test
    fun declarationinitializationOperationOperationStrtingTest() {
        val statement = readtxt("mock_text_declaration_initialization_operation_string.txt")
        val tokenList = lex(statement)
        val astList = parse(tokenList)
        val evaluator = Evaluator()
        evaluator.executionReader(astList)
        Assertions.assertEquals(evaluator.variables.toString(), readtxt("mock_text_declaration_initialization_operation_string_result.txt"))
    }

    @Test
    fun declarationinitializationOperationOperationVariableTest() {
        val statement = readtxt("mock_text_declaration_initialization_operation_variable.txt")
        val tokenList = lex(statement)
        val astList = parse(tokenList)
        val evaluator = Evaluator()
        evaluator.executionReader(astList)
        Assertions.assertEquals(evaluator.variables.toString(), readtxt("mock_text_declaration_initialization_operation_variable_result.txt"))
    }

    @Test
    fun assignationTest() {
        val statement = readtxt("mock_text_declaration_initialization.txt")
        val tokenList = lex(statement)
        val astList = parse(tokenList)
        val evaluator = Evaluator()
        evaluator.executionReader(astList)
        Assertions.assertEquals(evaluator.variables.toString(), readtxt("mock_text_declaration_initialization_result.txt"))
    }

    private fun parse(tokenList: List<Token>): List<ASTNode> {
        val parser: Parser = RegularParser.createDefaultParser()
        return parser.parse(tokenList)
    }

    private fun lex(statement: String): List<Token> {
        val tokenMap = TokenReadersProvider().getTokenMap("PrintScript") ?: return emptyList()
        val lexer: Lexer = RegularLexer(tokenMap)
        return lexer.lex(statement)
    }

    private fun readtxt(fileName: String): String {
        return Files.getResourceAsText(fileName).toString()
    }
}
