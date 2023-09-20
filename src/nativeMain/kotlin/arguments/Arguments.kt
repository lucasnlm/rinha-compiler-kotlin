package arguments

/**
 * The command line arguments.
 * @property astFilePath The path to the AST file.
 * @property sourceFilePath The path to the source file.
 * @property showHelp Whether to show the help.
 * @property runRepl Whether to run the REPL.
 */
data class Arguments(
    val astFilePath: String? = null,
    val sourceFilePath: String? = null,
    val showHelp: Boolean = false,
    val runRepl: Boolean = false,
)
