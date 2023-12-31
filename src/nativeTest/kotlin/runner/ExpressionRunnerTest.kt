package runner

import Output
import expressions.Expression
import mocks.AstHelper
import mocks.HardcodedScripts
import parser.RinhaGrammar
import platform.posix.NAN
import kotlin.test.Ignore
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
    fun `test let print literals`() {
        testScript(
            source = HardcodedScripts.pritValuesSource,
        ) {
            assertEquals("10", output[0])
            assertEquals("false", output[1])
            assertEquals("true", output[2])
            assertEquals("hello", output[3])
            assertEquals("(1, 2)", output[4])
            assertEquals("(hello, world)", output[5])
            assertEquals("<#closure>", output[6])
        }
    }

    @Test
    fun `test basic math script`() {
        testScript(
            ast = HardcodedScripts.testMathSourceAst,
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
    fun `test fib script - runtimeOptimization off`() {
        testScript(
            ast = HardcodedScripts.fibAst,
            source = HardcodedScripts.fibSource,
            runtimeOptimization = false,
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
    fun `test ifConditionNotBoolean`() {
        runCatching {
            testScript(
                source = HardcodedScripts.ifConditionNotBoolean,
            )
        }.onFailure {
            assertEquals("condition of 'if' expression must be a boolean", it.message)
        }.onSuccess {
            assertFalse(true)
        }
    }

    @Test
    fun `test fib script break - runtimeOptimization off`() {
        runCatching {
            testScript(
                source = HardcodedScripts.fibBreakSource,
                runtimeOptimization = false,
            ) {
                // It will b
            }
        }.onFailure {
            assertEquals("recursive call limit exceeded", it.message)
        }.onSuccess {
            assertFalse(true)
        }
    }

    @Test
    fun `test fib script - runtimeOptimization on`() {
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
    fun `test custom sum script`() {
        testScript(
            ast = HardcodedScripts.sumCustomSourceAst,
            source = HardcodedScripts.sumCustomSource,
        ) {
            assertEquals("500500", output.first())
        }
    }

    @Test
    fun `test custom sum negative script`() {
        testScript(
            source = HardcodedScripts.sumCustomNegSource,
        ) {
            assertEquals(1, output.size)
            assertEquals("500", output.first())
        }
    }

    @Test
    fun `test multiplication with rec`() {
        testScript(
            source = HardcodedScripts.mulTestSource,
            ast = HardcodedScripts.mulTestSourceAst,
        ) {
            assertEquals(1, output.size)
            assertEquals("3628800", output.first())
        }
    }

    @Test
    fun `test with tuples`() {
        testScript(
            ast = HardcodedScripts.testWithTuplesAst,
        ) {
            assertEquals(variables["assert1"], "a")
            assertEquals(variables["assert2"], "b")
            assertEquals(variables["assert3"], "c")
            assertEquals(variables["assert4"], "d")
            assertEquals(variables["assert5"], "efg")
            assertEquals("done", output.first())
        }
    }

    @Test
    fun `test fib with closures`() {
        testScript(
            ast = HardcodedScripts.fibWithClosuresAst,
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
    fun `test custom sum with multiplication script`() {
        testScript(
            ast = HardcodedScripts.sumMulSourceAst,
            source = HardcodedScripts.sumMulSource,
            runtimeOptimization = false,
        ) {
            assertEquals(1, variables.size)
            assertEquals(1, output.size)
            assertEquals("2036", output.first())
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
            ast = HardcodedScripts.printReturnSource1Ast,
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
            ast = HardcodedScripts.printReturnSource2Ast,
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
            ast = HardcodedScripts.printReturnSource3Ast,
            source = HardcodedScripts.printReturnSource3,
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
            ast = HardcodedScripts.printReturnSource4Ast,
            source = HardcodedScripts.printReturnSource4,
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
            ast = HardcodedScripts.immutabilitySourceAst,
            source = HardcodedScripts.immutabilitySource,
        ) {
            assertEquals(2, variables["x"])
            assertEquals(1, variables["result1"])
            assertEquals(2, variables["result2"])
        }
    }

    @Test
    fun `test increment`() {
        testScript(
            ast = HardcodedScripts.incrementSourceAst,
            source = HardcodedScripts.incrementSource,
        ) {
            assertEquals(1, output.size)
            assertEquals("2", output.first())
        }
    }

    @Test
    fun `test multiInternalFunctionsSource`() {
        testScript(
            ast = HardcodedScripts.multiInternalFunctionsSourceAst,
            source = HardcodedScripts.multiInternalFunctionsSource,
        ) {
            assertEquals(1, output.size)
            assertEquals("10", output.first())
        }
    }

    @Test
    fun `test comments`() {
        testScript(
            source = HardcodedScripts.commentsSource,
        ) {
            assertEquals(1, variables["x"])
            assertNull(variables["y"])
            assertEquals(10, variables["z"])
            assertEquals(5, variables["w"])
        }
    }

    @Test
    fun `test function scope inside function`() {
        testScript(
            source = HardcodedScripts.functionScopeSource,
        ) {
            assertEquals(1, variables["result"])
        }
    }

    @Test
    fun `test fibtail source`() {
        testScript(
            source = HardcodedScripts.fibtailSource,
        ) {
            assertEquals("873876091", output.first())
        }
    }

    @Test
    fun `test fibtail ast`() {
        testScript(
            ast = HardcodedScripts.fibtailSourceAst,
        ) {
            assertEquals("873876091", output.first())
        }
    }

    @Test
    fun `test fibtail with closure source 10`() {
        testScript(
            ast = HardcodedScripts.fibtailClosureAstSource,
        ) {
            assertEquals("55", output.first())
        }
    }

    @Test
    @Ignore
    fun `test fibtail with closure source 100000`() {
        testScript(
            ast = HardcodedScripts.fibtailClosureAstSource2,
        ) {
            assertEquals("55", output.first())
        }
    }

    @Test
    fun `test addTupleSource`() {
        testScript(
            source = HardcodedScripts.addTupleSource,
        ) {
            assertEquals("6", output[0])
            assertEquals("5", output[1])
            assertEquals("4", output[2])
            assertEquals("3", output[3])
            assertEquals("2", output[4])
        }
    }

    @Test
    fun `test insertionSortSource`() {
        testScript(
            ast = HardcodedScripts.insertionSortAst,
            source = HardcodedScripts.insertionSortSource,
        ) {
            assertEquals(1, output.size)
            assertEquals("(1, (2, (3, (4, (5, 0)))))", output.first())
        }
    }

    @Test
    fun `test doubleTryErrorSource`() {
        testScript(
            source = HardcodedScripts.doubleTryErrorSource,
            fallbackOptimization = true,
        ) {
            assertEquals(1, output.size)
            assertEquals("1950399", output.first())
        }
    }

    @Test
    fun `test function scope`() {
        testScript(
            ast = HardcodedScripts.functionScopeAst,
        ) {
            assertEquals(1, output.size)
            assertEquals("1", output.first())
        }
    }

    @Test
    fun `test function scope 2`() {
        testScript(
            ast = HardcodedScripts.functionScopeAst2,
        ) {
            assertEquals(1, output.size)
            assertEquals("123", output.first())
        }
    }

    @Test
    fun `test resStringSource source`() {
        testScript(
            ast = HardcodedScripts.resStringSourceAst,
            source = HardcodedScripts.resStringSource,
        ) {
            assertEquals("54321", output.first())
        }
    }

    @Test
    fun `test anon print`() {
        testScript(
            ast = HardcodedScripts.anomPrintAst,
        ) {
            assertEquals(1, output.size)
            assertEquals("100", output.first())
        }
    }

    @Test
    fun `test impure print`() {
        testScript(
            ast = HardcodedScripts.impurePrintAst,
        ) {
            assertEquals(2, output.size)
            assertEquals("10", output[0])
            assertEquals("10", output[1])
        }
    }

    @Test
    fun `test context print`() {
        testScript(
            ast = HardcodedScripts.impurePrint2Ast,
        ) {
            assertEquals(3, output.size)
            assertEquals("10", output[0])
            assertEquals("20", output[1])
            assertEquals("10", output[2])
        }
    }

    @Test
    fun `test loop oom`() {
        testScript(
            ast = HardcodedScripts.loopOomAst,
        ) {
            assertEquals(1, output.size)
            assertEquals("2000", output.first())
        }
    }

    private fun testScript(
        ast: String? = null,
        source: String? = null,
        runtimeOptimization: Boolean = true,
        fallbackOptimization: Boolean = false,
        block: (RunTimeContext.() -> Unit)? = null,
    ) {
        ast?.let {
            block?.let {
                Output.print("AST output:")
            }
            testScriptFromAst(
                fallbackOptimization = fallbackOptimization,
                runtimeOptimization = runtimeOptimization,
                target = ast,
            ) {
                block?.invoke(this)
            }
        }

        source?.let {
            block?.let {
                Output.print("Source output:")
            }
            testScriptFromSource(
                fallbackOptimization = fallbackOptimization,
                runtimeOptimization = runtimeOptimization,
                source = source,
            ) {
                block?.invoke(this)
            }
        }
    }

    private fun testScriptFromAst(
        target: String,
        runtimeOptimization: Boolean = true,
        fallbackOptimization: Boolean = false,
        block: RunTimeContext.() -> Unit,
    ) {
        val result = AstHelper.mockAst(content = target).getOrThrow()
        val runner = ExpressionRunner(
            runtimeContext = RunTimeContext(
                isTesting = true,
                runtimeOptimization = runtimeOptimization,
                fallbackOptimization = fallbackOptimization,
            ),
        )
        runner.runFromExpressions(result)
        block(runner.runtimeContext)
    }

    private fun testScriptFromSource(
        runtimeOptimization: Boolean = true,
        fallbackOptimization: Boolean = false,
        source: String,
        block: RunTimeContext.() -> Unit,
    ) {
        val result = RinhaGrammar.parseSource(source)
        val runner = ExpressionRunner(
            runtimeContext = RunTimeContext(
                isTesting = true,
                runtimeOptimization = runtimeOptimization,
                fallbackOptimization = fallbackOptimization,
            ),
        )

        runner.runFromExpressions(result)
        block(runner.runtimeContext)
    }
}
