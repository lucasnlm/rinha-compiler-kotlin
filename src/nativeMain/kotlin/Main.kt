import arguments.ArgumentParser
import parser.AstParser
import repl.ReplManager
import runner.ExpressionRunner
import kotlin.time.measureTime

fun main(args: Array<String> = arrayOf()) {
    // Validate the arguments
    val arguments = ArgumentParser.parse(args).onFailure {
        println("Error: ${it.message}")
    }.getOrNull() ?: return

    if (arguments.inputAstFile.isEmpty()) {
        runRepl()
    } else {
        runFromSource(arguments.inputAstFile)
    }
}

fun runFromSource(fileSource: String) {
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
            println("Error: ${it.message}")
        }
    }.let {
        println("Time: ${it.inWholeMilliseconds}ms")
    }
}

fun runRepl() {
    ReplManager.run()
}