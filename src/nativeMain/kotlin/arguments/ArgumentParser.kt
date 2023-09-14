package arguments

/**
 * Parses the command line arguments.
 */
object ArgumentParser {
    private fun isValidAstFilePath(filePath: String): Boolean {
        return filePath.isNotBlank() && filePath.endsWith(".json")
    }

    /**
     * Parses the command line arguments.
     * @param args The command line arguments.
     * @return The parsed [Arguments].
     */
    fun parse(args: Array<String>): Result<Arguments> {
        return if (args.isEmpty()) {
            Result.failure(IllegalArgumentException("No arguments provided"))
        } else if (!isValidAstFilePath(args[0])) {
            Result.failure(IllegalArgumentException("Invalid AST file path"))
        } else {
            Result.success(
                Arguments(
                    inputAstFile = args[0],
                ),
            )
        }
    }
}
