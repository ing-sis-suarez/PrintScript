package cli
import Files
import org.junit.jupiter.api.Test

class CommandsTest {

    @Test
    fun testInterpret() {
        val pathToFile = Files.getResourceAsFile("mock_text_declaration_initialization.txt")!!
        main(arrayOf("run", pathToFile.absolutePath, "1.0"))
    }
}
