import arguments.ArgumentParser
import repl.ReplManager
import runner.FileRunner

fun main(originalArguments: Array<String> = arrayOf()) {
    // Validate the arguments
    val arguments = ArgumentParser.parse(originalArguments)

    // Run the given argument
    when {
        arguments.rinhaMode -> runRinhaFile()
        arguments.showHelp -> showHelp()
        arguments.astFilePath?.isBlank() == false -> FileRunner.runFromAstFile(arguments.astFilePath)
        arguments.sourceFilePath?.isBlank() == false -> FileRunner.runFromRinhaFile(arguments.sourceFilePath)
        arguments.runRepl -> runRepl()
        else -> runRepl()
    }
}

fun showHelp() {
    """
    ${AppName.name}
      Usage: rinhak [options] [source file]
      Options:
          help          Show this help message and exit.
          repl          Run the REPL.
          rinha         Run on Rinha mode.
          <file.json>   Run from AST file. E.g: rinhak test.json
          <file.rinha>  Run from Rinha file. E.g: rinhak test.rinha
    """.trimIndent().let {
        Output.print(it)
    }
}

fun runRepl() {
    ReplManager.run()
}

fun runRinhaFile() {
    val rinhaFilePath = "/var/rinha/source.rinha.json"
    FileRunner.runFromAstFile(rinhaFilePath)
}
