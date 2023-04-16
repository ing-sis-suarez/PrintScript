

class InterpreterTest {
//    @Test
//    fun declarationTest() {
//        val tokenList = lex(File(this::class.java.classLoader.getResource("mock_text_declaration.txt")!!.path))
//        val astList = parse(tokenList)
//        val evaluator = Evaluator()
//        evaluator.evaluate(astList)
//        Assertions.assertEquals(evaluator.variables.toString(), readtxt("mock_text_declaration_result.txt"))
//    }
//
//    @Test
//    fun declarationinitializationOperationTest() {
//        val statement = readtxt("mock_text_declaration_initialization.txt")
//        val tokenList = lex(statement)
//        val astList = parse(tokenList)
//        val evaluator = Evaluator()
//        evaluator.executionReader(astList)
//        Assertions.assertEquals(evaluator.variables.toString(), readtxt("mock_text_declaration_initialization_result.txt"))
//    }
//
//    @Test
//    fun declarationinitializationOperationOperationTest() {
//        val statement = readtxt("mock_text_declaration_initialization_operation.txt")
//        val tokenList = lex(statement)
//        val astList = parse(tokenList)
//        val evaluator = Evaluator()
//        evaluator.executionReader(astList)
//        Assertions.assertEquals(evaluator.variables.toString(), readtxt("mock_text_declaration_initialization_operation_result.txt"))
//    }
//
//    @Test
//    fun declarationinitializationOperationOperationStrtingTest() {
//        val statement = readtxt("mock_text_declaration_initialization_operation_string.txt")
//        val tokenList = lex(statement)
//        val astList = parse(tokenList)
//        val evaluator = Evaluator()
//        evaluator.executionReader(astList)
//        Assertions.assertEquals(evaluator.variables.toString(), readtxt("mock_text_declaration_initialization_operation_string_result.txt"))
//    }
//
//    @Test
//    fun declarationinitializationOperationOperationVariableTest() {
//        val statement = readtxt("mock_text_declaration_initialization_operation_variable.txt")
//        val tokenList = lex(statement)
//        val astList = parse(tokenList)
//        val evaluator = Evaluator()
//        evaluator.executionReader(astList)
//        Assertions.assertEquals(evaluator.variables.toString(), readtxt("mock_text_declaration_initialization_operation_variable_result.txt"))
//    }
//
//    @Test
//    fun assignationTest() {
//        val statement = readtxt("mock_text_declaration_initialization.txt")
//        val tokenList = lex(statement)
//        val astList = parse(tokenList)
//        val evaluator = Evaluator()
//        evaluator.executionReader(astList)
//        Assertions.assertEquals(evaluator.variables.toString(), readtxt("mock_text_declaration_initialization_result.txt"))
//    }
//
//    private fun parse(tokenList: List<Token>): List<ASTNode> {
//        val parser: Parser = RegularParser.createDefaultParser()
//        return parser.parse(tokenList)
//    }
//
//    private fun lex(src: File) {
//        val tokenMap = TokenReadersProvider().getTokenMap("1.0")!!
//        val lexer: Lexer = RegularLexer(tokenMap)
//        lexer.lex(src)
//    }
//
//    private fun readtxt(fileName: String): String {
//        return Files.getResourceAsText(fileName).toString()
//    }
}
