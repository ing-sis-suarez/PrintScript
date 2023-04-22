import ast.node.ASTNode
import lexer.Lexer
import lexer.RegularLexer
import lexer.TokenReadersProvider
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import parser.Parser
import parser.RegularParser
import token.Token

class SCATest {

//    @Test
//    fun camelCaseTest() {
//        try {
//            val statement = readtxt("mock_text_declaration_Camel_Case.txt")
//            val tokenList = lex(statement)
//            val astList = parse(tokenList)
//            val sca = StaticCodeAnalyzer(readtxt("camelCase.json"))
//            sca.analyze(astList)
//            Assertions.assertNull(null)
//        } catch (e: Exception) {
//            Assertions.fail(e)
//        }
//    }
//
//    @Test
//    fun snakeCaseTest() {
//        try {
//            val statement = readtxt("mock_text_declaration_Snake_Case.txt")
//            val tokenList = lex(statement)
//            val astList = parse(tokenList)
//            val sca = StaticCodeAnalyzer(readtxt("snakeCase.json"))
//            sca.analyze(astList)
//            Assertions.assertNull(null)
//        } catch (e: Exception) {
//            Assertions.fail(e)
//        }
//    }
//
//    @Test
//    fun methodNoExpressionTest() {
//        try {
//            val statement = readtxt("mock_text_declaration_Method_No_Expresion.txt")
//            val tokenList = lex(statement)
//            val astList = parse(tokenList)
//            val sca = StaticCodeAnalyzer(readtxt("methodNoExpresion.json"))
//            sca.analyze(astList)
//            Assertions.assertNull(null)
//        } catch (e: Exception) {
//            Assertions.fail(e)
//        }
//    }
//
//    private fun parse(tokenList: List<Token>): List<ASTNode> {
//        val parser: Parser = RegularParser.createDefaultParser()
//        return parser.parse(tokenList)
//    }
//
//    private fun lex(statement: String): List<Token> {
//        val tokenMap = TokenReadersProvider().getTokenMap("PrintScript") ?: return emptyList()
//        val lexer: Lexer = RegularLexer(tokenMap)
//        return lexer.lex(statement)
//    }
//
//    private fun readtxt(fileName: String): String {
//        return Files.getResourceAsText(fileName).toString()
//    }
}
