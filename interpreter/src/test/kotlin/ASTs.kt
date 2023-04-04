import ast_node.*
import kotlinx.serialization.encodeToString
import token.*
import kotlinx.serialization.json.Json
import java.io.FileWriter

class ASTs {

    public fun ast1(): List<ASTNode>{
        val ast: ArrayList<ASTNode> = arrayListOf(Declaration(Token(1, TokenType.IDENTIFIER, Location(0,1), "val1"), Token(1, TokenType.NUMBER_KEYWORD, Location(0,2), "Number")))
        return ast
    }

    public fun ast2(): List<ASTNode>{
        val ast: ArrayList<ASTNode> = ArrayList()
        ast.add(DeclarationInitialization(
            Declaration(Token(1, TokenType.IDENTIFIER, Location(0,1), "val1"),
                Token(1, TokenType.NUMBER_KEYWORD, Location(0,2), "Number")),
            Value(BinaryTokenNode(Token(1, TokenType.NUMBER_LITERAL, Location(0,3), "10"), null, null))))
            return ast
    }

    fun ast3(): List<ASTNode>{
        val ast: ArrayList<ASTNode> = arrayListOf(DeclarationInitialization(
            Declaration(Token(1, TokenType.IDENTIFIER, Location(0,1), "val1"),
                Token(1, TokenType.STRING_KEYWORD, Location(0,2), "String")),
            Value(BinaryTokenNode(
                Token(1, TokenType.OPERATOR_PLUS, Location(0,4), "+"),
                BinaryTokenNode(Token(1, TokenType.STRING_LITERAL, Location(0,3), "10"), null, null),
                BinaryTokenNode(Token(1, TokenType.STRING_LITERAL, Location(0,5), "10"), null, null)))))
        return ast
    }

    fun ast4(): List<ASTNode>{
        val ast: ArrayList<ASTNode> = arrayListOf(Declaration(Token(1, TokenType.IDENTIFIER, Location(0,1), "val1"),
                Token(1, TokenType.STRING_KEYWORD, Location(0,2), "String")),
                Assignation(Token(1, TokenType.IDENTIFIER, Location(1,0), "val1"),
                Value(BinaryTokenNode(
                Token(1, TokenType.OPERATOR_PLUS, Location(0,4), "+"),
                BinaryTokenNode(Token(1, TokenType.STRING_LITERAL, Location(0,3), "10"), null, null),
                BinaryTokenNode(Token(1, TokenType.STRING_LITERAL, Location(0,5), "10"), null, null)))))
        return ast
    }





    fun operationValue(left: Token, riqht: Token, op: TokenType): Value{
        return Value(BinaryTokenNode(Token(1, op, Location(0,3), "+"),
            BinaryTokenNode(left, null, null), BinaryTokenNode(riqht,null,null)))
    }



}