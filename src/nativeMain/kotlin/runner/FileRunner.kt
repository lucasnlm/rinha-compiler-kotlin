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
        measureTime {
            // Parse the AST file from Json a Kotlin models
            val astModal = AstParser.parseAst(fileSource).onFailure {
                println("Error: ${it.message}")
            }.getOrNull() ?: return

            // Create a new Runner to run the expressions
            runCatching {
                ExpressionRunner()
                    .run(astModal.expressions)
            }.onFailure {
                println("e: ${it.message}")
            }
        }.let {
            println("Time: ${it.inWholeMilliseconds}ms")
        }
    }

    /**
     * Run from Rinha file.
     * @param fileSource The file source.
     */
    fun runFromRinhaFile(fileSource: String) {
        measureTime {
            // Parse file
            val fileContent = FileReader.readFile(fileSource) ?: return

            // Create a new Runner to run the expressions
            runCatching {
                ExpressionRunner().run {
                    runFromSource(fileContent)
                    dumpOutput()
                }
            }.onFailure {
                println("e: ${it.message}")
            }
        }.let {
            println("Time: ${it.inWholeMilliseconds}ms")
        }
    }
}
