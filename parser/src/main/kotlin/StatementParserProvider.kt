import utilities.ASTNode
import utilities.ASTNodeType
import utilities.Token
import utilities.TokenType

class StatementParserProvider {





    val declarationParser: Pair<SyntaxVerifier, ASTTreeConstructor> = Pair(
        this::isDeclaration,
        this::createDeclarationNode
    )
    val initializationParser: Pair<SyntaxVerifier, ASTTreeConstructor> = Pair(
        this::isInitialization,
        this::createInitializationNode
    )
    val assignationParser: Pair<SyntaxVerifier, ASTTreeConstructor> = Pair(
        this::isAssignation,
        this::createAssignationNode
    )
    val methodCallParser: Pair<SyntaxVerifier, ASTTreeConstructor> = Pair(
        this::isMethodCall,
        this::createMethodNode
    )

    private fun createMethodNode(statement: List<Token>): ASTNode {
        val identifier = ASTNode(listOf(), statement[0], ASTNodeType.TOKEN)
        val parameterValue = createValueNode(statement.subList(2, statement.size))
        return ASTNode(listOf(identifier, parameterValue), null, ASTNodeType.FUNCTION_CALL)
    }

    private fun isMethodCall(statement: List<Token>): Boolean {
        return statement[0].type == TokenType.IDENTIFIER && statement[1].type == TokenType.LEFT_PARENTHESIS && statement[statement.size - 1].type == TokenType.RIGHT_PARENTHESIS
    }

    private fun createAssignationNode(statement: List<Token>): ASTNode {
        val identifier = ASTNode(listOf(), statement[0], ASTNodeType.TOKEN)
        val value = createValueNode(statement.subList(2, statement.size))
        return ASTNode(listOf(identifier, value), null, ASTNodeType.ASSIGNATION)
    }

    private fun isAssignation(statement: List<Token>): Boolean {
        return statement[1].type == TokenType.ASIGNATION_EQUALS
    }

    private fun createInitializationNode(statement: List<Token>): ASTNode {
        val declaration = createDeclarationNode(statement.subList(0, 4))
        val value = createValueNode(statement.subList(5, statement.size))
        return ASTNode(listOf(declaration, value), null, ASTNodeType.INITIALIZATION)
    }

    private fun createValueNode(statement: List<Token>): ASTNode {
        if (statement.size == 1) return ASTNode(listOf(), statement[0], ASTNodeType.TOKEN)
        else if ()
    }


    private fun isInitialization(statement: List<Token>): Boolean {
        return statement[4].type == TokenType.ASIGNATION_EQUALS
    }

    private fun createDeclarationNode(statement: List<Token>): ASTNode{
        val identifier = ASTNode(listOf(), statement[1], ASTNodeType.TOKEN)
        val type = ASTNode(listOf(), statement[3], ASTNodeType.TOKEN)
        return ASTNode(listOf(identifier, type), null, ASTNodeType.DECLARATION)
    }

    private fun isDeclaration(statement: List<Token>): Boolean{
        return statement.size == 4 ||
                statement[0].type == TokenType.LET_KEYWORD ||
                statement[1].type == TokenType.IDENTIFIER ||
                statement[2].type == TokenType.DOUBLE_DOTS ||
                isType(statement[3])
    }

    private fun isType(token: Token): Boolean {
        return token.type == TokenType.NUMBER_LITERAL || token.type == TokenType.STRING_LITERAL
    }
}

typealias SyntaxVerifier = (statement: List<Token>) -> Boolean
typealias ASTTreeConstructor = (statement: List<Token>) -> ASTNode