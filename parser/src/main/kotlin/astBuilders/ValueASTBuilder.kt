package astBuilders

import astBuilders.ASTBuilder.Companion.takeWhiteSpacesCommentsAndSemiColon
import exceptions.UnexpectedTokenException
import node.BinaryOperator
import node.BinaryTokenNode
import node.BooleanOperator
import node.IdentifierOperator
import node.MethodCall
import node.NumericOperator
import node.StringOperator
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
        val methods: MutableMap<Location, MethodCall> = HashMap()
        checkValueNode(parsedStatements)
        val removedInput = findInput(parsedStatements, methods)
        val queue = processOperation(removedInput)
        return Value(createTree(queue, methods), statement)
        // 69 * 420 / ( 2 * 3 * 6 / 9 ) + 6
    }

    private fun checkValueNode(statement: List<Token>) {
        var state = SyntaxState.START
        checkcloseParenthesis(statement)

        for (token in statement) {
            state = when (state) {
                SyntaxState.START -> when (token.type) {
                    TokenType.IDENTIFIER -> if (token.actualValue() == "readInput") SyntaxState.METHOD else SyntaxState.OPERAND
                    TokenType.NUMBER_LITERAL, TokenType.STRING_LITERAL, TokenType.BOOLEAN_LITERAL -> SyntaxState.OPERAND
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
                    TokenType.IDENTIFIER -> if (token.actualValue() == "readInput") SyntaxState.METHOD else SyntaxState.OPERAND
                    TokenType.NUMBER_LITERAL, TokenType.STRING_LITERAL, TokenType.BOOLEAN_LITERAL -> SyntaxState.OPERAND
                    TokenType.LEFT_PARENTHESIS -> SyntaxState.LEFT_PARENTHESIS
                    TokenType.OPERATOR_MINUS -> SyntaxState.MINUS_OPERATOR_OR_OPERAND
                    else -> throw UnexpectedTokenException("Unexpected token at: ${token.location.row}, ${token.location.column}")
                }

                SyntaxState.MINUS_OPERATOR_OR_OPERAND -> when (token.type) {
                    TokenType.IDENTIFIER -> if (token.actualValue() == "readInput") SyntaxState.METHOD else SyntaxState.OPERAND
                    TokenType.NUMBER_LITERAL, TokenType.STRING_LITERAL -> SyntaxState.OPERAND
                    TokenType.LEFT_PARENTHESIS -> SyntaxState.LEFT_PARENTHESIS
                    else -> throw UnexpectedTokenException("Unexpected token at: ${token.location.row}, ${token.location.column}")
                }

                SyntaxState.OPERATOR -> when (token.type) {
                    TokenType.IDENTIFIER -> if (token.actualValue() == "readInput") SyntaxState.METHOD else SyntaxState.OPERAND
                    TokenType.NUMBER_LITERAL, TokenType.STRING_LITERAL -> SyntaxState.OPERAND
                    TokenType.LEFT_PARENTHESIS -> SyntaxState.LEFT_PARENTHESIS
                    else -> throw UnexpectedTokenException("Unexpected token at: ${token.location.row}, ${token.location.column}")
                }

                SyntaxState.RIGHT_PARENTHESIS -> when (token.type) {
                    TokenType.OPERATOR_PLUS, TokenType.OPERATOR_MINUS, TokenType.OPERATOR_TIMES, TokenType.OPERATOR_DIVIDE -> SyntaxState.OPERATOR
                    TokenType.RIGHT_PARENTHESIS -> SyntaxState.RIGHT_PARENTHESIS
                    else -> throw UnexpectedTokenException("Unexpected token at: ${token.location.row}, ${token.location.column}")
                }

                SyntaxState.METHOD -> when (token.type) {
                    TokenType.LEFT_PARENTHESIS -> SyntaxState.LEFT_PARENTHESIS
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
        RIGHT_PARENTHESIS,
        METHOD
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

    private fun processOperation(nodeList: List<Token>): Queue<Token> {
        val stack: Stack<Token> = Stack()
        val queue: Queue<Token> = LinkedList()
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

    private fun isValue(token: Token): Boolean {
        return token.type == TokenType.NUMBER_LITERAL || token.type == TokenType.STRING_LITERAL || token.type == TokenType.IDENTIFIER || token.type == TokenType.BOOLEAN_LITERAL
    }

    private fun isLeftParenthesis(token: Token): Boolean {
        return token.type == TokenType.LEFT_PARENTHESIS
    }

    private fun isOperator(token: Token): Boolean {
        return token.type == TokenType.OPERATOR_PLUS || token.type == TokenType.OPERATOR_DIVIDE ||
            token.type == TokenType.OPERATOR_MINUS || token.type == TokenType.OPERATOR_TIMES
    }

    private fun hasLessPrecedence(
        token: Token,
        otherToken: Token
    ): Boolean {
        val precedence = listOf(
            TokenType.OPERATOR_MINUS,
            TokenType.OPERATOR_PLUS,
            TokenType.OPERATOR_DIVIDE,
            TokenType.OPERATOR_TIMES
        )
        return precedence.indexOf(token.type) < precedence.indexOf(otherToken.type) &&
            !isLeftParenthesis(otherToken)
    }

    private fun createTree(queue: Queue<Token>, methods: MutableMap<Location, MethodCall>): BinaryTokenNode {
        val stack: Stack<BinaryTokenNode> = Stack()
        while (queue.size != 0) {
            val node = queue.remove()
            if (isValue(node)) {
                if (node.type == TokenType.IDENTIFIER && node.actualValue() == "readInput") {
                    if (methods.containsKey(node.location)) {
                        stack.push(methods[node.location])
                    }
                } else {
                    stack.push(createValue(node))
                }
            } else {
                stack.push(createOperationTree(node, stack))
            }
        }
        return stack.pop()
    }

    private fun createOperationTree(
        node: Token,
        stack: Stack<BinaryTokenNode>
    ): BinaryTokenNode {
        val right = stack.pop()
        return BinaryOperator(
            node,
            right,
            if (stack.empty()) createZeroNode(node.location) else stack.pop()
        )
    }

    private fun createZeroNode(location: Location): BinaryTokenNode {
        return NumericOperator(Token(TokenType.NUMBER_LITERAL, location, "0", 1))
    }

    private fun createValue(token: Token): BinaryTokenNode {
        return when (token.type) {
            TokenType.NUMBER_LITERAL -> NumericOperator(token)
            TokenType.STRING_LITERAL -> StringOperator(token)
            TokenType.BOOLEAN_LITERAL -> BooleanOperator(token)
            TokenType.IDENTIFIER -> IdentifierOperator(token)
            else -> throw UnexpectedTokenException("Unexpected token at: ${token.location.row}, ${token.location.column}")
        }
    }

    private fun findInput(tokens: List<Token>, methods: MutableMap<Location, MethodCall>): List<Token> {
        var parenthesis = 0
        var isMethod = false
        val methodTokens: MutableList<Token> = mutableListOf()
        val toRemove: MutableList<Token> = mutableListOf()
        val methodCall = MethodCallASTBuilder()
        for (token in tokens) {
            when (token.type) {
                TokenType.IDENTIFIER -> {
                    methodTokens.add(token)
                    isMethod = true
                }
                TokenType.LEFT_PARENTHESIS -> {
                    if (isMethod) {
                        methodTokens.add(token)
                        parenthesis++
                    }
                }
                TokenType.RIGHT_PARENTHESIS -> {
                    if (isMethod) {
                        if (parenthesis == 1) {
                            methodTokens.add(token)
                            methods[methodTokens[0].location] = methodCall.buildAST(methodTokens)
                            toRemove.addAll(ArrayList(methodTokens.subList(1, methodTokens.size)))
                            methodTokens.clear()
                            isMethod = false
                            parenthesis = 0
                        } else {
                            methodTokens.add(token)
                            parenthesis--
                        }
                    }
                }
                else -> {
                    if (isMethod) {
                        methodTokens.add(token)
                    }
                }
            }
        }
        return removeInputs(tokens.toMutableList(), toRemove)
    }

    private fun removeInputs(tokens: MutableList<Token>, toRemove: MutableList<Token>): List<Token> {
        return tokens.filter { elemento -> !toRemove.contains(elemento) }
    }
}
