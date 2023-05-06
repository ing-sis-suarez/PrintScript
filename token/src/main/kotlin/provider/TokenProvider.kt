package provider

import token.Token
interface TokenProvider {

    fun readToken(): Token?
}
interface TPResponse
// data class TPSuccessResponse: TPResponse
