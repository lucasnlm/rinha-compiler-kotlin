package expressions

/**
 * Represents an expression in the AST.
 * @property start The start location of the expression.
 * @property end The end location of the expression.
 * @property fileName The file name of the expression.
 */
data class ExpressionLocation(
    val start: Int,
    val end: Int,
    val fileName: String,
) {
    override fun toString(): String {
        return "($fileName:$start-$end)"
    }
}
