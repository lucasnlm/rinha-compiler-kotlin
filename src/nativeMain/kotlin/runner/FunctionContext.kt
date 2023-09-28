package runner

/**
 * Context of a function call.
 * @property scope The variables map that the function can access.
 * @property root The name of the root function that is being called.
 * @property recursiveCall The recursive depth of the function call.
 * @property tailCall The name of the tail call function.
 * @property runtimeOptimization If the function can be optimized at runtime.
 */
data class FunctionContext(
    val scope: MutableMap<String, Any?>,
    val root: String?,
    val recursiveCall: Int = 1,
    val tailCall: String? = null,
    val runtimeOptimization: Boolean = true,
)
