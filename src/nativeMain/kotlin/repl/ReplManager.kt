package repl

import runner.ExpressionRunner
import runner.RunTimeContext

/**
 * Manages the REPL.
 */
object ReplManager {
    fun run() {
        println("Kotlin Rinha Interpreter")
        println("------------------------")
        println("  Type '!q' to exit.")
        println("  Type '!s' to see the variable scope.")
        println()

        val runner = ExpressionRunner(
            context = RunTimeContext(),
        )

        while (true) {
            runner.clearOutput()

            print("❯ ")

            val input = readlnOrNull()?.trim() ?: break

            when {
                input.isEmpty() -> {
                    continue
                }
                SCOPE_PARAMS.contains(input) -> {
                    runner.printGlobalScope()
                    continue
                }
                QUIT_PARAMS.contains(input) -> {
                    // Bye bye!
                    break
                }
                else -> {
                    runner.runFromSource(input)
                }
            }

            runner.dumpOutput()
        }
    }

    private val QUIT_PARAMS = listOf("!q", "!quit")
    private val SCOPE_PARAMS = listOf("!s", "!scope")
}
