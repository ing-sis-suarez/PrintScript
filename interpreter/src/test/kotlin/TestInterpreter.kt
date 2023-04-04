import ast_node.ASTNode
import ast_node.Declaration
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import ASTs.*
import token.Location
import token.Token
import token.TokenType

class TestInterpreter {
    val asts: ASTs = ASTs()
    val variables: Variables = Variables()
    @Test
    fun case1(){
        val evaluator: Evaluator = Evaluator()
        evaluator.executionReader(asts.ast1())
        Assertions.assertEquals(evaluator.variables, variables.variable1())

    }

    @Test
    fun case2(){
        val evaluator: Evaluator = Evaluator()
        evaluator.executionReader(asts.ast2())
        Assertions.assertEquals(evaluator.variables, variables.variable2())

    }

    @Test
    fun case3(){
        val evaluator = Evaluator()
        evaluator.executionReader(asts.ast3())
        Assertions.assertEquals(evaluator.variables, variables.variable3())

    }

    @Test
    fun case4(){
        val evaluator = Evaluator()
        evaluator.executionReader(asts.ast4())
        Assertions.assertEquals(evaluator.variables, variables.variable3())

    }

}