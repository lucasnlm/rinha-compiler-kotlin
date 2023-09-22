package runner

import com.github.h0tk3y.betterParse.grammar.parseToEnd
import expressions.Expression
import mocks.AstHelper
import mocks.HardcodedScripts
import parser.RinhaGrammar
import platform.posix.NAN
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ExpressionRunnerTest {
    @Test
    fun `test basic let script`() {
        testScript(
            ast = HardcodedScripts.testLetAst,
            source = HardcodedScripts.testLetSource,
        ) {
            assertEquals(5, variables.size)
            assertEquals(10, variables["a"])
            assertEquals(false, variables["b"])
            assertEquals(true, variables["c"])
            assertEquals("Hello", variables["d"])
            assertEquals(Pair(1, 2), variables["e"])
            assertNull(variables["z"])
        }
    }

    @Test
    fun `test basic math script`() {
        testScript(
            source = HardcodedScripts.testMathSource,
        ) {
            assertEquals(26, variables.size)
            assertEquals(30, variables["t_add"])
            assertEquals(-5, variables["t_sub"])
            assertEquals(6, variables["t_mul"])
            assertEquals(20, variables["t_div"])
            assertEquals(NAN, variables["t_div_zero"])
            assertEquals(0, variables["t_rem"])
            assertTrue(variables["t_true_1"] as Boolean)
            assertTrue(variables["t_true_2"] as Boolean)
            assertTrue(variables["t_true_3"] as Boolean)
            assertFalse(variables["t_false_1"] as Boolean)
            assertFalse(variables["t_false_2"] as Boolean)
            assertTrue(variables["t_false_3"] as Boolean)
            assertEquals("ab", variables["t_str_add_1"])
            assertEquals("a1", variables["t_str_add_2"])
            assertEquals("1a", variables["t_str_add_3"])
            assertTrue(variables["t_lt"] as Boolean)
            assertTrue(variables["t_lte"] as Boolean)
            assertFalse(variables["t_lt_false"] as Boolean)
            assertFalse(variables["t_lte_false"] as Boolean)
            assertTrue(variables["t_gt"] as Boolean)
            assertTrue(variables["t_gte"] as Boolean)
            assertFalse(variables["t_gt_false"] as Boolean)
            assertFalse(variables["t_gte_false"] as Boolean)
            assertFalse(variables["t_and_false"] as Boolean)
            assertTrue(variables["t_and_true"] as Boolean)
            assertTrue(variables["t_or"] as Boolean)
        }
    }

    @Test
    fun `test tuple script`() {
        testScript(
            ast = HardcodedScripts.testTupleAst,
            source = HardcodedScripts.testTupleSource,
        ) {
            assertEquals(4, variables.size)
            assertEquals(1, output.size)
            assertEquals(Pair(5, 10), variables["target"])
            assertEquals(5, variables["primeiro"])
            assertEquals(10, variables["segundo"])
            assertEquals(15, variables["soma"])
            assertEquals("soma = 15", output.first())
        }
    }

    @Test
    fun `test sum script`() {
        testScript(
            ast = HardcodedScripts.sumAst,
            source = HardcodedScripts.sumSource,
        ) {
            assertEquals(1, variables.size)
            assertEquals(1, output.size)
            assertTrue {
                variables["sum"] is Expression.Function
            }
            assertEquals("15", output.first())
        }
    }

    @Test
    fun `test print script`() {
        testScript(
            ast = HardcodedScripts.printAst,
            source = HardcodedScripts.printSource,
        ) {
            assertTrue(variables.isEmpty())
            assertEquals(1, output.size)
            assertEquals("Hello world", output.first())
        }
    }

    @Test
    fun `test fib script`() {
        testScript(
            ast = HardcodedScripts.fibAst,
            source = HardcodedScripts.fibSource,
        ) {
            assertEquals(1, variables.size)
            assertEquals(1, output.size)
            assertTrue {
                variables["fib"] is Expression.Function
            }
            assertEquals("55", output.first())
        }
    }

    @Test
    fun `test combination script`() {
        testScript(
            ast = HardcodedScripts.combinationAst,
            source = HardcodedScripts.combinationSource,
        ) {
            assertEquals(1, variables.size)
            assertEquals(1, output.size)
            assertTrue {
                variables["combination"] is Expression.Function
            }
            assertEquals("45", output.first())
        }
    }

    @Test
    fun `test print returns - with let`() {
        testScript(
            source = HardcodedScripts.printReturnSouce1,
        ) {
            assertNull(variables["_"])
            assertTrue(variables.isEmpty())
            assertEquals(2, output.size)
            assertEquals("1", output[0])
            assertEquals("2", output[1])
        }
    }

    @Test
    fun `test print returns - with fn`() {
        testScript(
            source = HardcodedScripts.printReturnSouce2,
        ) {
            assertNotNull(variables["f"])
            assertEquals(3, output.size)
            assertEquals("1", output[0])
            assertEquals("2", output[1])
            assertEquals("3", output[2])
        }
    }

    @Test
    fun `test print returns - print of prints`() {
        testScript(
            source = HardcodedScripts.printReturnSouce3,
        ) {
            assertTrue(variables.isEmpty())
            assertEquals(3, output.size)
            assertEquals("1", output[0])
            assertEquals("2", output[1])
            assertEquals("3", output[2])
        }
    }

    @Test
    fun `test print returns - tuple`() {
        testScript(
            source = HardcodedScripts.printReturnSouce4,
        ) {
            assertNotNull(variables["tuple"])
            assertEquals(3, output.size)
            assertEquals("1", output[0])
            assertEquals("2", output[1])
            assertEquals("(1, 2)", output[2])
        }
    }

    @Test
    fun `test immutability`() {
        testScript(
            source = HardcodedScripts.immutabilitySource,
        ) {
            assertEquals(0, output.size)
            assertEquals(2, variables["x"])
            assertEquals(1, variables["result1"])
            assertEquals(2, variables["result2"])
        }
    }

    @Test
    fun `test comments`() {
        testScript(
            source = HardcodedScripts.commentsSource,
        ) {

        }
    }

    private fun testScript(
        ast: String? = null,
        source: String? = null,
        block: RunTimeContext.() -> Unit,
    ) {
        ast?.let {
            testScriptFromAst(ast) {
                block(this)
            }
        }

        source?.let {
            testScriptFromSource(source) {
                block(this)
            }
        }
    }

    private fun testScriptFromAst(target: String, block: RunTimeContext.() -> Unit) {
        val result = AstHelper.mockAst(content = target).getOrThrow()
        val runner = ExpressionRunner(
            context = RunTimeContext(isTesting = true),
        )
        runner.run(result.expressions)
        block(runner.context)
    }

    private fun testScriptFromSource(source: String, block: RunTimeContext.() -> Unit) {
        val result = RinhaGrammar.parseToEnd(source)
        val runner = ExpressionRunner(
            context = RunTimeContext(isTesting = true),
        )

        runner.run(result)
        block(runner.context)
    }
}
