package printscript

import things.ErrorHandler

class ErrorHandle: ErrorHandler {
    override fun reportError(message: String?) {
        println("\u001B[31m$message\u001B[0m")
    }
}