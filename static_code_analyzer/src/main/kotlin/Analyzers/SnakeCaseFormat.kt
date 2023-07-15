import consumer.ConsumerResponse
import consumer.ConsumerResponseError
import consumer.ConsumerResponseSuccess
import node.ASTNode
import node.Declaration
import node.DeclarationInitialization
import token.Location

class SnakeCaseFormat : Analyzer {
    override fun analyze(astNode: ASTNode): ConsumerResponse {
        return when (astNode) {
            is Declaration -> analyze(astNode.identifier.actualValue(), astNode.identifier.location)
            is DeclarationInitialization -> analyze(astNode.declaration.identifier.actualValue(), astNode.declaration.identifier.location)
            else -> { return ConsumerResponseSuccess(null) }
        }
    }

    private fun analyze(str: String, location: Location): ConsumerResponse {
        return if (!Regex("[a-z]+(?:_[a-z]+)*").matches(str)) {
            ConsumerResponseError("Invalid typing format in row ${location.row} ${location.column}")
        } else {
            ConsumerResponseSuccess(null)
        }
    }
}
