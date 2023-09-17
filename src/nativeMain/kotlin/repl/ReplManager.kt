package repl

import com.github.h0tk3y.betterParse.grammar.parseToEnd
import parser.RinhaGrammar

/**
 * Manages the REPL.
 */
object ReplManager {
    fun run() {
        println("Rinha Kotlin Interpreter")

        while (true) {
            print("❯ ")

            val input = readlnOrNull()?.trim() ?: break

            if (input == "quit") {
                break
            }

            RinhaGrammar.parseToEnd(input).also {
                println(it)
            }

//            RinhaParser.parseString(sanitizedInput).onSuccess {
//                println(it)
//            }.onFailure {
//                println("Error: ${it.message}")
//            }

        }
    }
}
