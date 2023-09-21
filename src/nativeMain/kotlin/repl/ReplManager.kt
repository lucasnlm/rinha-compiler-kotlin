package repl

import runner.ExpressionRunner
import runner.RunTimeContext

/**
 * Manages the REPL.
 */
object ReplManager {
    fun run() {
        """
        Kotlin Rinha Interpreter
        ------------------------
          '!q' to exit.
          '!s' to see the variable scope.
        """.trimIndent().let(::println)

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
                    runCatching {
                        runner.run {
                            runFromSource(input)
                        }
                    }.onFailure {
                        println("e: ${it.message}")
                    }
                }
            }
        }
    }

    private val QUIT_PARAMS = listOf("!q", "!quit")
    private val SCOPE_PARAMS = listOf("!s", "!scope")
}
