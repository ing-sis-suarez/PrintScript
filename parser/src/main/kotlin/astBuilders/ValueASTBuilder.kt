package astBuilders

import astBuilders.ASTBuilder.Companion.takeWhiteSpacesCommentsAndSemiColon
import exceptions.UnexpectedTokenException
import node.BinaryTokenNode
import node.Value
import token.Location
import token.Token
import token.TokenType
import java.util.LinkedList
import java.util.Queue
import java.util.Stack

class ValueASTBuilder : ASTBuilder<Value> {
    override fun isApplicable(statement: List<Token>): Boolean {
        val parsedStatements = takeWhiteSpacesCommentsAndSemiColon(statement)
        return parsedStatements.isNotEmpty()
    }

    override fun buildAST(statement: List<Token>): Value {
        val parsedStatements = takeWhiteSpacesCommentsAndSemiColon(statement)
        checkValueNode(parsedStatements)
        val nodeList = parsedStatements.map { token -> BinaryTokenNode(token, null, null) }
        val queue = processOperation(nodeList)
        return Value(createTree(queue), statement)
        // 69 * 420 / ( 2 * 3 * 6 / 9 ) + 6
    }

    private fun checkValueNode(statement: List<Token>) {
        var state = SyntaxState.START
        checkcloseParenthesis(statement)

        for (token in statement) {
            state = when (state) {
                SyntaxState.START -> when (token.type) {
                    TokenType.NUMBER_LITERAL, TokenType.IDENTIFIER, TokenType.STRING_LITERAL -> SyntaxState.OPERAND
                    TokenType.LEFT_PARENTHESIS -> SyntaxState.LEFT_PARENTHESIS
                    TokenType.OPERATOR_MINUS, TokenType.OPERATOR_PLUS -> SyntaxState.MINUS_OPERATOR_OR_OPERAND
                    else -> throw UnexpectedTokenException("Unexpected token at: ${token.location.row}, ${token.location.column}")
                }

                SyntaxState.OPERAND -> when (token.type) {
                    TokenType.OPERATOR_PLUS, TokenType.OPERATOR_MINUS, TokenType.OPERATOR_TIMES, TokenType.OPERATOR_DIVIDE -> SyntaxState.OPERATOR
                    TokenType.RIGHT_PARENTHESIS -> SyntaxState.RIGHT_PARENTHESIS
                    else -> throw UnexpectedTokenException("Unexpected token at: ${token.location.row}, ${token.location.column}")
                }

                SyntaxState.LEFT_PARENTHESIS -> when (token.type) {
                    TokenType.NUMBER_LITERAL, TokenType.IDENTIFIER, TokenType.STRING_LITERAL -> SyntaxState.OPERAND
                    TokenType.LEFT_PARENTHESIS -> SyntaxState.LEFT_PARENTHESIS
                    TokenType.OPERATOR_MINUS -> SyntaxState.MINUS_OPERATOR_OR_OPERAND
                    else -> throw UnexpectedTokenException("Unexpected token at: ${token.location.row}, ${token.location.column}")
                }

                SyntaxState.MINUS_OPERATOR_OR_OPERAND -> when (token.type) {
                    TokenType.NUMBER_LITERAL, TokenType.IDENTIFIER, TokenType.STRING_LITERAL -> SyntaxState.OPERAND
                    TokenType.LEFT_PARENTHESIS -> SyntaxState.LEFT_PARENTHESIS
                    else -> throw UnexpectedTokenException("Unexpected token at: ${token.location.row}, ${token.location.column}")
                }

                SyntaxState.OPERATOR -> when (token.type) {
                    TokenType.NUMBER_LITERAL, TokenType.IDENTIFIER, TokenType.STRING_LITERAL -> SyntaxState.OPERAND
                    TokenType.LEFT_PARENTHESIS -> SyntaxState.LEFT_PARENTHESIS
                    else -> throw UnexpectedTokenException("Unexpected token at: ${token.location.row}, ${token.location.column}")
                }

                SyntaxState.RIGHT_PARENTHESIS -> when (token.type) {
                    TokenType.OPERATOR_PLUS, TokenType.OPERATOR_MINUS, TokenType.OPERATOR_TIMES, TokenType.OPERATOR_DIVIDE -> SyntaxState.OPERATOR
                    TokenType.RIGHT_PARENTHESIS -> SyntaxState.RIGHT_PARENTHESIS
                    else -> throw UnexpectedTokenException("Unexpected token at: ${token.location.row}, ${token.location.column}")
                }
            }
        }

        if (!(state == SyntaxState.OPERAND || state == SyntaxState.RIGHT_PARENTHESIS)) throw UnexpectedTokenException("Unexpected token at: ${statement[statement.size - 1].location.row}, ${statement[statement.size - 1].location.column}")
    }

    private enum class SyntaxState {
        START,
        MINUS_OPERATOR_OR_OPERAND,
        OPERATOR,
        LEFT_PARENTHESIS,
        OPERAND,
        RIGHT_PARENTHESIS
    }

    private fun checkcloseParenthesis(statement: List<Token>) {
        var count = 0
        for (token in statement) {
            if (token.type == TokenType.LEFT_PARENTHESIS) count++
            if (token.type == TokenType.RIGHT_PARENTHESIS) count--
            if (count < 0) throw UnexpectedTokenException("Unexpected token at: ${token.location.row}, ${token.location.column}")
        }
        if (count != 0) throw UnexpectedTokenException("Unclosed parenthesis at: ${statement[statement.size - 1].location.row}")
    }

    private fun processOperation(nodeList: List<BinaryTokenNode>): Queue<BinaryTokenNode> {
        val stack: Stack<BinaryTokenNode> = Stack()
        val queue: Queue<BinaryTokenNode> = LinkedList()
        for (node in nodeList) {
            if (isValue(node)) {
                queue.add(node)
            } else if (isLeftParenthesis(node)) {
                stack.push(node)
            } else if (isOperator(node)) {
                while (!stack.empty() && hasLessPrecedence(node, stack.peek())) {
                    queue.add(stack.pop())
                }
                stack.push(node)
            } else {
                while (!isLeftParenthesis(stack.peek())) {
                    queue.add(stack.pop())
                }
                stack.pop()
            }
        }
        while (!stack.empty()) {
            queue.add(stack.pop())
        }
        return queue
    }

    private fun isValue(node: BinaryTokenNode): Boolean {
        return node.token.type == TokenType.NUMBER_LITERAL || node.token.type == TokenType.STRING_LITERAL || node.token.type == TokenType.IDENTIFIER
    }

    private fun isLeftParenthesis(node: BinaryTokenNode): Boolean {
        return node.token.type == TokenType.LEFT_PARENTHESIS
    }

    private fun isOperator(node: BinaryTokenNode): Boolean {
        return node.token.type == TokenType.OPERATOR_PLUS || node.token.type == TokenType.OPERATOR_DIVIDE ||
            node.token.type == TokenType.OPERATOR_MINUS || node.token.type == TokenType.OPERATOR_TIMES
    }

    private fun hasLessPrecedence(
        node: BinaryTokenNode,
        otherNode: BinaryTokenNode
    ): Boolean {
        val precedence = listOf(
            TokenType.OPERATOR_MINUS,
            TokenType.OPERATOR_PLUS,
            TokenType.OPERATOR_DIVIDE,
            TokenType.OPERATOR_TIMES
        )
        return precedence.indexOf(node.token.type) < precedence.indexOf(otherNode.token.type) &&
            !isLeftParenthesis(otherNode)
    }

    private fun createTree(queue: Queue<BinaryTokenNode>): BinaryTokenNode {
        val stack: Stack<BinaryTokenNode> = Stack()
        while (queue.size != 0) {
            val node = queue.remove()
            if (isValue(node)) {
                stack.push(node)
            } else {
                stack.push(createOperationTree(node, stack))
            }
        }
        return stack.pop()
    }

    private fun createOperationTree(
        node: BinaryTokenNode,
        stack: Stack<BinaryTokenNode>
    ): BinaryTokenNode {
        val right = stack.pop()
        return BinaryTokenNode(
            node.token,
            right,
            if (stack.empty()) createZeroNode(right.token.location) else stack.pop()
        )
    }

    private fun createZeroNode(location: Location): BinaryTokenNode {
        return BinaryTokenNode(Token(TokenType.NUMBER_LITERAL, location, "0", 1), null, null)
    }
}
