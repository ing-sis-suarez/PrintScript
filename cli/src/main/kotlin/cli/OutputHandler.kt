package cli

import java.io.File
import java.io.FileWriter

interface OutputHandler {
    fun print(msg: String)
}

data class Printer(val path: String?) : OutputHandler {
    private val handler: OutputHandler = selectHandler()

    override fun print(msg: String) {
        handler.print(msg)
    }

    private fun selectHandler(): OutputHandler {
        return if (path.equals("")) {
            ConsoleOutputHandler(path)
        } else {
            FileOutputHandler(path!!)
        }
    }
}
data class FileOutputHandler(val path: String) : OutputHandler {
    init {
        clearFileContent()
    }
    override fun print(msg: String) {
        val toPrintFile = File(path)
        if (!toPrintFile.exists()) {
            toPrintFile.createNewFile()
        }
        val writer = FileWriter(toPrintFile, true)
        writer.append(msg + System.lineSeparator())
        writer.close()
    }

    private fun clearFileContent() {
        val toPrintFile = File(path)
        if (toPrintFile.exists()) {
            toPrintFile.writeText("") // Borra el contenido del archivo
        }
    }
}
data class ConsoleOutputHandler(val path: String?) : OutputHandler {
    override fun print(msg: String) {
        println(msg)
    }
}
