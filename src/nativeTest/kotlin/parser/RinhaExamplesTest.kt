package parser

import com.github.h0tk3y.betterParse.grammar.parseToEnd
import expressions.BinaryOperator
import expressions.Expression
import kotlin.test.Test
import kotlin.test.assertEquals

class RinhaExamplesTest {
    @Test
    fun `test rinha print`() {
        val input = """
            print ("Hello world")
        """.trimIndent()
        val result = RinhaGrammar.parseToEnd(input)
        val expected = listOf(
            Expression.Print(
                value = listOf(
                    Expression.StrValue(
                        value = "Hello world",
                    ),
                ),
            ),
        )
        assertEquals(expected, result)
    }

    @Test
    fun `test sum rinha`() {
        val input = """
            let sum = fn (n) => {
              if (n == 1) {
                n
              } else {
                n + sum(n - 1)
              }
            };
            
            print (sum(5))
        """.trimIndent()
        val result = RinhaGrammar.parseToEnd(input)
        val expected = listOf(
            Expression.Let(
                name = "sum",
                value = Expression.Function(
                    parameters = listOf("n"),
                    value = listOf(
                        Expression.If(
                            condition = Expression.Binary(
                                left = Expression.Var(
                                    name = "n",
                                ),
                                right = Expression.IntValue(
                                    value = 1,
                                ),
                                operator = BinaryOperator.Eq,
                            ),
                            then = listOf(
                                Expression.Var(
                                    name = "n",
                                ),
                            ),
                            otherwise = listOf(
                                Expression.Binary(
                                    left = Expression.Var(
                                        name = "n",
                                    ),
                                    right = Expression.Call(
                                        callee = Expression.Var(
                                            name = "sum",
                                        ),
                                        arguments = listOf(
                                            Expression.Binary(
                                                left = Expression.Var(
                                                    name = "n",
                                                ),
                                                right = Expression.IntValue(
                                                    value = 1,
                                                ),
                                                operator = BinaryOperator.Sub,
                                            ),
                                        ),
                                    ),
                                    operator = BinaryOperator.Add,
                                ),
                            ),
                        ),
                    ),
                ),
            ),
            Expression.Print(
                value = listOf(
                    Expression.Call(
                        callee = Expression.Var(
                            name = "sum",
                        ),
                        arguments = listOf(
                            Expression.IntValue(
                                value = 5,
                            ),
                        ),
                    ),
                ),
            ),
        )
        assertEquals(expected, result)
    }
}
