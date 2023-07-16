package cli

import java.io.File

interface InputHandler {
    fun Input(msg: String): String
}

data class Inputer(val path: String?) : InputHandler {
    private val handler: InputHandler = selectHandler()
    override fun Input(msg: String): String {
        return handler.Input(msg)
    }

    private fun selectHandler(): InputHandler {
        return if (path.equals("")) {
            ConsoleInput(path)
        } else {
            FileInput(path!!)
        }
    }
}

data class FileInput(val path: String) : InputHandler {
    val file = File(path)
    val reader = file.bufferedReader()
    override fun Input(msg: String): String {
        if (file.exists()) {
            val firstLine = reader.readLine()
            return firstLine ?: ""
        }
        return ""
    }
}

data class ConsoleInput(val path: String?) : InputHandler {
    override fun Input(msg: String): String {
        println(msg)
        val input: String = readLine() ?: ""
        return input
    }
}
