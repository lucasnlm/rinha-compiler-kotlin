package ast

import expressions.Expression
import io.JsonFileReader
import mocks.AstHelper
import mocks.HardcodedScripts
import parser.AstParser
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
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
        assertNotNull(result.getOrNull())
    }

    @Test
    fun `success to parse fib rinha`() {
        val result = AstHelper.mockAst(content = HardcodedScripts.fibAst)
        assertTrue(result.isSuccess)
        assertNotNull(result.getOrNull())
    }

    @Test
    fun `success to parse print rinha`() {
        val result = AstHelper.mockAst(content = HardcodedScripts.printAst)
        assertTrue(result.isSuccess)
        assertNotNull(result.getOrNull())
    }

    @Test
    fun `success to parse combination rinha`() {
        val result = AstHelper.mockAst(content = HardcodedScripts.combinationAst)
        assertTrue(result.isSuccess)
        assertNotNull(result.getOrNull())
    }

    @Test
    fun `test Let json parser`() {
        val content = JsonFileReader.parse(AstMocks.LET).getOrThrow()
        val expressions = AstParser.parseAstFile(content).getOrThrow()
        assertEquals(listOf(
            Expression.Let(
                name = "x",
                value = Expression.IntValue(10),
            ),
            Expression.Print(
                value = listOf(Expression.Var("x")),
            )
        ), expressions)
    }
}
