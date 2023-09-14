package mocks

import expressions.AstModel
import okio.Path.Companion.toPath
import okio.fakefilesystem.FakeFileSystem
import parser.AstParser

object AstHelper {
    fun mockAst(
        fileName: String = "test.json",
        content: String? = null,
    ): Result<AstModel> {
        val userHome = "/Users/user".toPath()
        val testJson = userHome / "test.json"
        val fileSystem = FakeFileSystem().apply {
            createDirectories(userHome)
            content?.let {
                write(testJson) { writeUtf8(it) }
            }
        }

        return AstParser.parseAst("/Users/user/$fileName", fileSystem)
    }
}
