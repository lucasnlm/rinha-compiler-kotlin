package mocks

import expressions.Expression
import io.JsonFileReader
import okio.Path.Companion.toPath
import okio.fakefilesystem.FakeFileSystem
import parser.AstParser

object AstHelper {
    fun mockAst(
        fileName: String = "test.json",
        content: String? = null,
    ): Result<List<Expression>> {
        val userHome = "/Users/user".toPath()
        val testJson = userHome / "test.json"
        val fileSystem = FakeFileSystem().apply {
            createDirectories(userHome)
            content?.let {
                write(testJson) { writeUtf8(it) }
            }
        }

        return runCatching {
            val fileContent = JsonFileReader.readFile(
                filePath = "/Users/user/$fileName",
                fileSystem = fileSystem,
            ).getOrThrow()

            // Parse the AST file from Json a Kotlin models
            AstParser.parseAstFile(
                fileContent = fileContent,
            ).getOrThrow()
        }.onFailure {
            println("e: ${it.message}")
        }
    }
}
