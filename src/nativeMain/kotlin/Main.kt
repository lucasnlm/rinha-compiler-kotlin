import arguments.ArgumentParser
import repl.ReplManager
import runner.FileRunner

fun main(originalArguments: Array<String> = arrayOf()) {
    // Validate the arguments
    val arguments = ArgumentParser.parse(originalArguments)

    // Run the given argument
    when {
        arguments.showHelp -> showHelp()
        arguments.astFilePath?.isBlank() == false -> FileRunner.runFromAstFile(arguments.astFilePath)
        arguments.sourceFilePath?.isBlank() == false -> FileRunner.runFromRinhaFile(arguments.sourceFilePath)
        arguments.runRepl -> runRepl()
        else -> runRepl()
    }
}

fun showHelp() {
    val help = """
    Kotlin Rinha Interpreter
      Usage: rinhak [options] [source file]
      Options:
          help          Show this help message and exit.
          repl          Run the REPL.
          <file.json>   Run from AST file. E.g: rinhak test.json
          <file.rinha>  Run from Rinha file. E.g: rinhak test.rinha
    """.trimIndent()
    println(help)
}

fun runRepl() {
    ReplManager.run()
}
