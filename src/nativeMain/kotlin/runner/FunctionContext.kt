package runner

/**
 * Context of a function call.
 * @property scope The variables map that the function can access.
 * @property root The name of the root function that is being called.
 * @property recursiveCall The recursive depth of the function call.
 * @property canTailCallOptimize If the function can be tail call optimized.
 */
data class FunctionContext(
    val scope: MutableMap<String, Any?>,
    val root: String?,
    val recursiveCall: Int = 1,
    val canTailCallOptimize: Boolean = false,
)
