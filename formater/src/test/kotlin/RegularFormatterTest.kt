
import ast.node.ASTNode
import lexer.Lexer
import lexer.RegularLexer
import lexer.TokenReadersProvider
import org.junit.jupiter.api.Test
import parser.Parser
import parser.RegularParser
import token.Token

class RegularFormatterTest {

    @Test
    fun declarationTest() {
        val statement = readtxt("mock_text_declaration.txt")
        val tokenList = lex(statement)
        val astList = parse(tokenList)
        val formatter = RegularFormatter()
        val result = formatter.format(astList.first())
        println(result)
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
