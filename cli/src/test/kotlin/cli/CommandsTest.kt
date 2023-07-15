package cli
import Files
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CommandsTest {
    @Test
    fun testInterpret() {
        val pathToFile = Files.getResourceAsFile("mock_text_declaration_initialization.txt")!!
        val pathToprint = Files.getResourceAsFile("mock_text_declaration_initialization_file.txt")!!
        val pathToInput = Files.getResourceAsFile("mock_text_declaration_initialization_input_interpreter.txt")!!
        main(arrayOf("run", pathToFile.absolutePath, "1.1", pathToprint.absolutePath, pathToInput.absolutePath))
        assertEquals(
            Files.getResourceAsText("mock_text_declaration_initialization_result_interpreter.txt"),
            Files.getResourceAsText("mock_text_declaration_initialization_file.txt")
        )
    }

    @Test
    fun testFormater() {
        val pathToFile = Files.getResourceAsFile("mock_text_declaration_initialization.txt")!!
        val pathToprint = Files.getResourceAsFile("mock_text_declaration_initialization_file.txt")!!
        main(arrayOf("format", pathToFile.absolutePath, pathToprint.absolutePath, "1.0"))
        assertEquals(
            Files.getResourceAsText("mock_text_declaration_initialization_result_Formater.txt"),
            Files.getResourceAsText("mock_text_declaration_initialization_file.txt")
        )
    }

    @Test
    fun testSCA() {
        val pathToFile = Files.getResourceAsFile("mock_text_declaration_initialization.txt")!!
        val pathToprint = Files.getResourceAsFile("mock_text_declaration_initialization_file.txt")!!
        main(arrayOf("analyze", pathToFile.absolutePath, "1.0", pathToprint.absolutePath))
        assertEquals(
            Files.getResourceAsText("mock_text_declaration_initialization_result_SCA.txt"),
            Files.getResourceAsText("mock_text_declaration_initialization_file.txt")
        )
    }
}
