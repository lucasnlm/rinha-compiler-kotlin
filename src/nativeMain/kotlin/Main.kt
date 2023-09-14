import arguments.ArgumentParser
import parser.AstParser
import runner.ExpressionRunner
import kotlin.time.measureTime

fun main(args: Array<String> = arrayOf()) {
    measureTime {
        // Validate the arguments
        val arguments = ArgumentParser.parse(args).onFailure {
            println("Error: ${it.message}")
        }.getOrNull() ?: return

        // Parse the AST file from Json a Kotlin models
        val astModal = AstParser.parseAst(arguments.inputAstFile).onFailure {
            println("Error: ${it.message}")
        }.getOrNull() ?: return

        // Create a new Runner to run the expressions
        runCatching {
            ExpressionRunner()
                .run(astModal.expressions)
        }.onFailure {
            println("Error: ${it.message}")
        }
    }.let {
        println("Time: ${it.inWholeMilliseconds}ms")
    }
}
