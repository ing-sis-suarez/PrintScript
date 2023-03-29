import org.junit.jupiter.api.Test
import token.Location
import token.Token
import token.TokenType

class RegularParserTest {
    
    @Test
    public fun declarationTest(){
        val tokens = readTokens("declaration.txt")
        val parser: Parser = RegularParser(StatementParserProvider().getParserList())
        parser.parse(tokens)
    }

    private fun readTokens(fileName: String): List<Token> {
        val rawText = Files.getResourceAsText(fileName)
        val tokenList = breakIntoLines(rawText.toString())
        return tokenList.map { tokenString -> toToken(tokenString) }
    }

    private fun toToken(tokenString: String): Token { // reads a token.toString() and returns a token
        val tokenRegex = """Token\(id=(\d+), type=(\w+), location=Location\(row=(\d+), column=(\d+)\), originalValue=(\S+)\)""".toRegex()

        val matchResult = tokenRegex.matchEntire(tokenString)
        if (matchResult != null) {
            val (id, typeStr, row, column, originalValue) = matchResult.destructured
            val type = TokenType.valueOf(typeStr)
            val location = Location(row.toInt(), column.toInt())
            return Token(id.toInt(), type, location, originalValue)
        }
        throw IllegalArgumentException("Invalid token string: $tokenString")
    }

    private fun breakIntoLines(rawText: String): List<String> {
        return rawText.trim().split(System.lineSeparator())
    }
}