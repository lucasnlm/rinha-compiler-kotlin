package arguments

/**
 * Parses the command line arguments.
 */
object ArgumentParser {
    /**
     * Parses the command line arguments.
     * @param args The command line arguments.
     * @return The parsed [Arguments].
     */
    fun parse(args: Array<String>): Arguments {
        return args.fold(Arguments()) { arguments, arg ->
            when {
                COMMON_HELP_COMMANDS.contains(arg) -> arguments.copy(showHelp = true)
                REPL_COMMANDS.contains(arg) -> arguments.copy(runRepl = true)
                isValidAstFilePath(arg) -> arguments.copy(astFilePath = arg)
                isValidSourceFilePath(arg) -> arguments.copy(sourceFilePath = arg)
                else -> arguments
            }
        }
    }

    private fun isValidAstFilePath(filePath: String): Boolean {
        return filePath.isNotBlank() && filePath.endsWith(AST_FILE_EXTENSION)
    }

    private fun isValidSourceFilePath(filePath: String): Boolean {
        return filePath.isNotBlank() && filePath.endsWith(SOURCE_FILE_EXTENSION)
    }

    private const val AST_FILE_EXTENSION = ".json"
    private const val SOURCE_FILE_EXTENSION = ".rinha"
    private val REPL_COMMANDS = listOf("repl", "r", "--repl", "-r")
    private val COMMON_HELP_COMMANDS = listOf("help", "--help", "-h")
}
