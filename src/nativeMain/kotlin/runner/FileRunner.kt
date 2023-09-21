package runner

import io.FileReader
import parser.AstParser
import kotlin.time.measureTime

object FileRunner {
    /**
     * Run from AST file.
     * @param fileSource The file source.
     */
    fun runFromAstFile(fileSource: String) {
        // Parse the AST file from Json a Kotlin models
        val astModal = AstParser.parseAst(fileSource).onFailure {
            println("e: ${it.message}")
        }.getOrNull() ?: return

        // Create a new Runner to run the expressions
        runCatching {
            ExpressionRunner()
                .run(astModal.expressions)
        }.onFailure {
            println("e: ${it.message}")
        }
    }

    /**
     * Run from Rinha file.
     * @param fileSource The file source.
     */
    fun runFromRinhaFile(fileSource: String) {
        // Parse file
        val fileContent = FileReader.readFile(fileSource) ?: return

        // Create a new Runner to run the expressions
        runCatching {
            ExpressionRunner().run {
                runFromSource(fileContent)
            }
        }.onFailure {
            println("e: ${it.message}")
        }
    }
}
