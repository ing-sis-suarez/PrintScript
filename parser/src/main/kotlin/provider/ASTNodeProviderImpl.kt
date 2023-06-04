package provider

import parser.Parser
import token.Token
import token.TokenType
class ASTNodeProviderImpl(
    private val tokenProvider: TokenProvider,
    private val parser: Parser
) : ASTNodeProvider {

    override fun readASTNode(): ASTNProviderResponse {
        val tokenList = mutableListOf<Token>()
        while (true) {
            val token = tokenProvider.readToken() ?: return handleNullToken(tokenList)
            if (token.type == TokenType.LEFT_BRACE) {
                tokenList.add(token)
                val blockResponse = readASTBlock(tokenList)
                if (blockResponse is ASTNProviderResponseEnd) return ASTNProviderResponseSuccess(parser.parse(tokenList))
                if (blockResponse is ASTNProviderResponseError) return ASTNProviderResponseError(blockResponse.error)
            }
            if (token.type == TokenType.SEMICOLON) {
                return ASTNProviderResponseSuccess(parser.parse(tokenList))
            }
            tokenList.add(token)
        }
    }
    private fun readASTBlock(tokenList: MutableList<Token>): ASTNProviderResponse {
        while (true) {
            val token = tokenProvider.readToken() ?: return ASTNProviderResponseError("Unexpected end of file")
            if (token.type == TokenType.RIGHT_BRACE) {
                tokenList.add(token)
                return ASTNProviderResponseEnd()
            }
            if (token.type == TokenType.LEFT_BRACE) {
                readASTBlock(tokenList)
            }
            tokenList.add(token)
        }
    }
    private fun handleNullToken(tokenList: MutableList<Token>): ASTNProviderResponse {
        if (tokenList.isEmpty()) return ASTNProviderResponseEnd()
        if (!sentenceIsFinished(tokenList)) return ASTNProviderResponseError("Missing semicolon at line ${tokenList[tokenList.size - 1].location.row}")
        return ASTNProviderResponseError("Unexpected end of file")
    }

    private fun sentenceIsFinished(tokenList: List<Token>): Boolean {
        return tokenList[tokenList.size - 1].type == TokenType.SEMICOLON
    }
}
