package printscript

import things.PrintEmitter

class Emitter : PrintEmitter {
    override fun print(message: String?) {
        println(message)
    }
}
