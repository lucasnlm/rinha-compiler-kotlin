package ast

import expressions.AstModel
import mocks.HardcodedScripts
import okio.Path.Companion.toPath
import okio.fakefilesystem.FakeFileSystem
import parser.AstParser
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AstParserTest {
    private fun mockAst(
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

    @Test
    fun `fails when file do not exist`() {
        val result = mockAst("non-existent.json")
        assertTrue(result.isFailure)
        assertEquals("Invalid AST file content from '/Users/user/non-existent.json'", result.exceptionOrNull()?.message)
    }

    @Test
    fun `fails when fails to parse file`() {
        val result = mockAst(content = "")
        assertTrue(result.isFailure)
        assertEquals("Fail to parse JSON from '/Users/user/test.json'", result.exceptionOrNull()?.message)
    }

    @Test
    fun `success to parse sum rinha`() {
        val result = mockAst(content = HardcodedScripts.sum)
        assertTrue(result.isSuccess)
    }
}
