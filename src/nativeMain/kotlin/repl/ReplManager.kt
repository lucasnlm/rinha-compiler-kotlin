package repl

import AppName
import Output
import runner.ExpressionRunner
import runner.RunTimeContext

/**
 * Manages the REPL.
 */
object ReplManager {
    fun run() {
        """
        ${AppName.name}
        ------------------------------
          '!q' to exit.
          '!s' to see the variable scope.
        """.trimIndent().let {
            Output.print(it)
        }

        val runner = ExpressionRunner(
            runtimeContext = RunTimeContext(),
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
                        Output.error(it.message)
                    }
                }
            }
        }
    }

    private val QUIT_PARAMS = listOf("!q", "!quit")
    private val SCOPE_PARAMS = listOf("!s", "!scope")
}
