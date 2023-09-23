package ast

import mocks.AstHelper
import mocks.HardcodedScripts
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AstParserTest {
    @Test
    fun `fails when file do not exist`() {
        val result = AstHelper.mockAst("non-existent.json")
        assertTrue(result.isFailure)
        assertEquals("can't read file '/Users/user/non-existent.json'", result.exceptionOrNull()?.message)
    }

    @Test
    fun `fails when fails to parse file`() {
        val result = AstHelper.mockAst(content = "")
        assertTrue(result.isFailure)
        assertEquals("file '/Users/user/test.json' has no code, it's empty", result.exceptionOrNull()?.message)
    }

    @Test
    fun `success to parse sum rinha`() {
        val result = AstHelper.mockAst(content = HardcodedScripts.sumAst)
        assertTrue(result.isSuccess)
    }
}
