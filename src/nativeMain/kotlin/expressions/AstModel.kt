package expressions

/**
 * This is the AST model.
 * @property filePath the file path of the AST file.
 * @property expressions the list of expressions.
 */
data class AstModel(
    val filePath: String,
    val expressions: List<Expression>,
)
