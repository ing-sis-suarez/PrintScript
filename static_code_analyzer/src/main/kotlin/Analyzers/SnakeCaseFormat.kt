import ast.node.ASTNode
import ast.node.Declaration
import ast.node.DeclarationInitialization
import token.Location

class SnakeCaseFormat : Analyzer {
    override fun analyze(astNode: ASTNode) {
        when (astNode) {
            is Declaration -> analize(astNode.identifier.actualValue(), astNode.identifier.location)
            is DeclarationInitialization -> analize(astNode.declaration.identifier.actualValue(), astNode.declaration.identifier.location)
        }
    }

    private fun analize(str: String, location: Location) {
        if (!str.matches("^[a-z]+(_[a-z]+)*$".toRegex())) {
            return
        } else {
            throw InvalidFormatException("Invalid typing format in row ${location.row} ${location.column}")
        }
    }
}
