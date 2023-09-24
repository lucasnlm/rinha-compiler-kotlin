package runner

import io.JsonFileReader
import io.RinhaFileReader
import okio.FileSystem
import parser.AstParser

object FileRunner {
    /**
     * Run from AST file.
     * @param fileSource The file source.
     * @param fileSystem The [FileSystem] to use. Default is [FileSystem.SYSTEM].
     */
    fun runFromAstFile(
        fileSource: String,
        fileSystem: FileSystem = FileSystem.SYSTEM,
    ) {
        runCatching {
            // Read the JSON file content
            val jsonContent = JsonFileReader.readFile(
                filePath = fileSource,
                fileSystem = fileSystem,
            ).getOrThrow()

            // Parse the AST file from Json a Kotlin models
            val expressions = AstParser.parseAstFile(
                fileContent = jsonContent,
            ).getOrThrow()

            // Create a new Runner to run the expressions
            ExpressionRunner().runFromExpressions(expressions)
        }.onFailure {
            println("e: ${it.message}")
        }
    }

    /**
     * Run from Rinha file.
     * @param fileSource The file source.
     * @param fileSystem The [FileSystem] to use. Default is [FileSystem.SYSTEM].
     */
    fun runFromRinhaFile(
        fileSource: String,
        fileSystem: FileSystem = FileSystem.SYSTEM,
    ) {
        runCatching {
            // Read file content
            val fileContent = RinhaFileReader.readFile(fileSource, fileSystem).getOrThrow()

            // Create a new Runner to run the expressions
            ExpressionRunner().run {
                runFromSource(fileContent)
            }
        }.onFailure {
            println("e: ${it.message}")
        }
    }
}
