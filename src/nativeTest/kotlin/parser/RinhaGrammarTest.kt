package parser

import com.github.h0tk3y.betterParse.grammar.parseToEnd
import expressions.BinaryOperator
import expressions.Expression
import kotlin.test.Test
import kotlin.test.assertEquals

class RinhaGrammarTest {
    @Test
    fun `test LET parsing`() {
        mapOf(
            "let a = 1" to Expression.Let(
                name = "a",
                value = Expression.IntValue(1),
            ),
            "let b = \"value\"" to Expression.Let(
                name = "b",
                value = Expression.StrValue("value"),
            ),
            "let c = true" to Expression.Let(
                name = "c",
                value = Expression.BoolValue(true),
            ),
            "let d = false" to Expression.Let(
                name = "d",
                value = Expression.BoolValue(false),
            ),
            "let e = (10, 20)" to Expression.Let(
                name = "e",
                value = Expression.TupleValue(first = Expression.IntValue(10), second = Expression.IntValue(20)),
            ),
            "let f = (10, false)" to Expression.Let(
                name = "f",
                value = Expression.TupleValue(Expression.IntValue(10), Expression.BoolValue(false)),
            ),
            "let g = (false, true)" to Expression.Let(
                name = "g",
                value = Expression.TupleValue(Expression.BoolValue(false), Expression.BoolValue(true)),
            ),
            "let h = (\"B\", \"A\")" to Expression.Let(
                name = "h",
                value = Expression.TupleValue(Expression.StrValue("B"), Expression.StrValue("A")),
            ),
            "let i = if (true) { 1 } else { 2 }" to Expression.Let(
                name = "i",
                value = Expression.If(
                    condition = Expression.BoolValue(true),
                    then = listOf(Expression.IntValue(1)),
                    otherwise = listOf(Expression.IntValue(2)),
                ),
            ),
            "let j = if (false) { 2 } else { 3 }" to Expression.Let(
                name = "j",
                value = Expression.If(
                    condition = Expression.BoolValue(value = false),
                    then = listOf(Expression.IntValue(2)),
                    otherwise = listOf(Expression.IntValue(3)),
                ),
            ),
            "let k = fn (n) => { n }" to Expression.Let(
                name = "k",
                value = Expression.Function(
                    parameters = listOf("n"),
                    value = listOf(Expression.Var("n")),
                ),
            ),
            "let l = fn (z) => { z }" to Expression.Let(
                name = "l",
                value = Expression.Function(
                    parameters = listOf("z"),
                    value = listOf(Expression.Var("z")),
                ),
            ),
        ).forEach {
            val result = RinhaGrammar.parseToEnd(it.key).firstOrNull()
            assertEquals(it.value, result)
        }
    }

    @Test
    fun `test IF parsing`() {
        mapOf(
            "if (true) { 1 } else { 0 }" to
                Expression.If(
                    condition = Expression.BoolValue(true),
                    then = listOf(Expression.IntValue(1)),
                    otherwise = listOf(Expression.IntValue(0)),
                ),
            "if (x) { \"a\" } else { \"b\" }" to
                Expression.If(
                    condition = Expression.Var("x"),
                    then = listOf(Expression.StrValue("a")),
                    otherwise = listOf(Expression.StrValue("b")),
                ),
            "if (true || false) { x } else { y }" to
                Expression.If(
                    condition = Expression.Binary(
                        Expression.BoolValue(true),
                        Expression.BoolValue(false),
                        BinaryOperator.Or,
                    ),
                    then = listOf(Expression.Var("x")),
                    otherwise = listOf(Expression.Var("y")),
                ),
            "if (foo(10)) { 1 } else { 2 }" to
                Expression.If(
                    condition = Expression.Call(
                        callee = Expression.Var("foo"),
                        arguments = listOf(Expression.IntValue(10)),
                    ),
                    then = listOf(Expression.IntValue(1)),
                    otherwise = listOf(Expression.IntValue(2)),
                ),
        ).forEach {
            val result = RinhaGrammar.parseToEnd(it.key).firstOrNull()
            assertEquals(it.value, result)
        }
    }

    @Test
    fun `test FUN parsing`() {
        mapOf(
            "fn (n) => { n }" to
                Expression.Function(
                    parameters = listOf("n"),
                    value = listOf(Expression.Var("n")),
                ),
            "fn (n) { n }" to
                Expression.Function(
                    parameters = listOf("n"),
                    value = listOf(Expression.Var("n")),
                ),
            "fn (n) { n }" to
                Expression.Function(
                    parameters = listOf("n"),
                    value = listOf(Expression.Var("n")),
                ),
            "fn () => { n }" to
                Expression.Function(
                    parameters = listOf(),
                    value = listOf(Expression.Var("n")),
                ),
            "fn (x,y,z) => { x;y;z }" to
                Expression.Function(
                    parameters = listOf("x", "y", "z"),
                    value = listOf(Expression.Var("x"), Expression.Var("y"), Expression.Var("z")),
                ),
            "fn (x) { if (x) {1} else {2}}" to
                Expression.Function(
                    parameters = listOf("x"),
                    value = listOf(
                        Expression.If(
                            condition = Expression.Var("x"),
                            then = listOf(Expression.IntValue(1)),
                            otherwise = listOf(Expression.IntValue(2)),
                        ),
                    ),
                ),
        ).forEach {
            val result = RinhaGrammar.parseToEnd(it.key).firstOrNull()
            assertEquals(it.value, result)
        }
    }

    @Test
    fun `test TUPLE parsing`() {
        mapOf(
            "(10, 20)" to Expression.TupleValue(
                first = Expression.IntValue(10),
                second = Expression.IntValue(20),
            ),
            "first((1,2))" to Expression.First(
                listOf(
                    Expression.TupleValue(
                        first = Expression.IntValue(1),
                        second = Expression.IntValue(2),
                    ),
                ),
            ),
            "first(someVar)" to Expression.First(
                listOf(Expression.Var(name = "someVar")),

            ),
            "second((1,2))" to Expression.Second(
                listOf(
                    Expression.TupleValue(
                        first = Expression.IntValue(1),
                        second = Expression.IntValue(2),
                    ),
                ),
            ),
            "second(someVar)" to Expression.Second(
                listOf(Expression.Var(name = "someVar")),
            ),
            "(10, false)" to Expression.TupleValue(
                first = Expression.IntValue(10),
                second = Expression.BoolValue(false),
            ),
            "(false, true)" to Expression.TupleValue(
                first = Expression.BoolValue(false),
                second = Expression.BoolValue(true),
            ),
            "(\"B\", \"A\")" to Expression.TupleValue(
                first = Expression.StrValue("B"),
                second = Expression.StrValue("A"),
            ),
            "(\"B\", 10)" to Expression.TupleValue(
                first = Expression.StrValue("B"),
                second = Expression.IntValue(10),
            ),
            "(10, \"B\")" to Expression.TupleValue(
                first = Expression.IntValue(10),
                second = Expression.StrValue("B"),
            ),
            "(10, (20, 30))" to Expression.TupleValue(
                first = Expression.IntValue(10),
                second = Expression.TupleValue(Expression.IntValue(20), Expression.IntValue(30)),
            ),
        ).forEach {
            val result = RinhaGrammar.parseToEnd(it.key).firstOrNull()
            assertEquals(it.value, result)
        }
    }

    @Test
    fun `test literal parsing`() {
        mapOf(
            "1" to Expression.IntValue(1),
            "\"value\"" to Expression.StrValue("value"),
            "true" to Expression.BoolValue(true),
            "false" to Expression.BoolValue(false),
            "(10, 20)" to Expression.TupleValue(Expression.IntValue(10), Expression.IntValue(20)),
            "(10, false)" to Expression.TupleValue(Expression.IntValue(10), Expression.BoolValue(false)),
            "(false, true)" to Expression.TupleValue(Expression.BoolValue(false), Expression.BoolValue(true)),
            "(\"B\", \"A\")" to Expression.TupleValue(Expression.StrValue("B"), Expression.StrValue("A")),
            "(\"B\", 10)" to Expression.TupleValue(Expression.StrValue("B"), Expression.IntValue(10)),
            "(10, \"B\")" to Expression.TupleValue(Expression.IntValue(10), Expression.StrValue("B")),
            "(10, (20, 30))" to Expression.TupleValue(
                Expression.IntValue(10),
                Expression.TupleValue(Expression.IntValue(20), Expression.IntValue(30)),
            ),
        ).forEach {
            val result = RinhaGrammar.parseToEnd(it.key).firstOrNull()
            assertEquals(it.value, result)
        }
    }

    @Test
    fun `test binary operations parsing`() {
        mapOf(
            "1 + 2" to Expression.Binary(
                Expression.IntValue(1),
                Expression.IntValue(2),
                BinaryOperator.Add,
            ),
            "1 - 2" to Expression.Binary(
                Expression.IntValue(1),
                Expression.IntValue(2),
                BinaryOperator.Sub,
            ),
            "1 * 2" to Expression.Binary(
                Expression.IntValue(1),
                Expression.IntValue(2),
                BinaryOperator.Mul,
            ),
            "1 / 2" to Expression.Binary(
                Expression.IntValue(1),
                Expression.IntValue(2),
                BinaryOperator.Div,
            ),
            "1 % 2" to Expression.Binary(
                Expression.IntValue(1),
                Expression.IntValue(2),
                BinaryOperator.Rem,
            ),
            "1 == 2" to Expression.Binary(
                Expression.IntValue(1),
                Expression.IntValue(2),
                BinaryOperator.Eq,
            ),
            "1 != 2" to Expression.Binary(
                Expression.IntValue(1),
                Expression.IntValue(2),
                BinaryOperator.Neq,
            ),
            "1 < 2" to Expression.Binary(
                Expression.IntValue(1),
                Expression.IntValue(2),
                BinaryOperator.Lt,
            ),
            "1 > 2" to Expression.Binary(
                Expression.IntValue(1),
                Expression.IntValue(2),
                BinaryOperator.Gt,
            ),
            "1 <= 2" to Expression.Binary(
                Expression.IntValue(1),
                Expression.IntValue(2),
                BinaryOperator.Lte,
            ),
            "1 >= 2" to Expression.Binary(
                Expression.IntValue(1),
                Expression.IntValue(2),
                BinaryOperator.Gte,
            ),
            "true || false" to Expression.Binary(
                Expression.BoolValue(true),
                Expression.BoolValue(false),
                BinaryOperator.Or,
            ),
            "true && false" to Expression.Binary(
                Expression.BoolValue(true),
                Expression.BoolValue(false),
                BinaryOperator.And,
            ),
        ).forEach {
            val result = RinhaGrammar.parseToEnd(it.key).firstOrNull()
            assertEquals(it.value, result)
        }
    }

    @Test
    fun `test print parsing`() {
        mapOf(
            "print(1)" to Expression.Print(listOf(Expression.IntValue(1))),
            "print(\"value\")" to Expression.Print(listOf(Expression.StrValue("value"))),
            "print(true)" to Expression.Print(listOf(Expression.BoolValue(true))),
            "print(false)" to Expression.Print(listOf(Expression.BoolValue(false))),
            "print((10, 20))" to Expression.Print(
                listOf(
                    Expression.TupleValue(
                        Expression.IntValue(10),
                        Expression.IntValue(20),
                    ),
                ),
            ),
            "print((10, false))" to Expression.Print(
                listOf(
                    Expression.TupleValue(
                        Expression.IntValue(10),
                        Expression.BoolValue(false),
                    ),
                ),
            ),
            "print((false, true))" to Expression.Print(
                listOf(
                    Expression.TupleValue(
                        Expression.BoolValue(false),
                        Expression.BoolValue(true),
                    ),
                ),
            ),
            "print((\"B\", \"A\"))" to Expression.Print(
                listOf(
                    Expression.TupleValue(
                        Expression.StrValue("B"),
                        Expression.StrValue("A"),
                    ),
                ),
            ),
            "print((\"B\", 10))" to Expression.Print(
                listOf(
                    Expression.TupleValue(
                        Expression.StrValue("B"),
                        Expression.IntValue(10),
                    ),
                ),
            ),
            "print((10, \"B\"))" to Expression.Print(
                listOf(
                    Expression.TupleValue(
                        Expression.IntValue(10),
                        Expression.StrValue("B"),
                    ),
                ),
            ),
            "print((10, (20, 30)))" to Expression.Print(
                listOf(
                    Expression.TupleValue(
                        Expression.IntValue(10),
                        Expression.TupleValue(Expression.IntValue(20), Expression.IntValue(30)),
                    ),
                ),
            ),
            "print(\"value is\" + 10)" to Expression.Print(
                listOf(
                    Expression.Binary(
                        Expression.StrValue("value is"),
                        Expression.IntValue(10),
                        BinaryOperator.Add,
                    ),
                ),
            ),
        ).forEach {
            val result = RinhaGrammar.parseToEnd(it.key).firstOrNull()
            assertEquals(it.value, result)
        }
    }
}
