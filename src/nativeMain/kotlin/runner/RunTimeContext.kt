package runner

/**
 * The runtime scope for the interpreter.
 * @property variables The variables in the global scope.
 * @property output The output of the program.
 * @property maxOutputSize The maximum size of the output. Default is 100.
 * @property isTesting Whether the interpreter is running in test mode.
 */
data class RunTimeContext(
    val variables: MutableMap<String, Any?> = mutableMapOf(),
    val output: MutableList<String> = mutableListOf(),
    val maxOutputSize: Int = 100,
    val isTesting: Boolean = false,
)
