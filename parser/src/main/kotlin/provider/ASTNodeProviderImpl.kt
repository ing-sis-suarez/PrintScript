package provider

import main.kotlin.token_provider.TokenProvider
import node.*
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
            if (token.type == TokenType.SEMICOLON)
                return ASTNProviderResponseSuccess(parser.parse(tokenList))
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