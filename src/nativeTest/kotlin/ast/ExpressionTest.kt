package ast

import expressions.Expression
import kotlin.test.Test
import kotlin.test.assertEquals

class ExpressionTest {
    @Test
    fun `function expression toString`() {
        val fnToStr = Expression.Function(
            parameters = listOf(),
            value = listOf(),
        ).toString()

        assertEquals("<#closure>", fnToStr)
    }
}
