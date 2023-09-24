package parser

import com.github.h0tk3y.betterParse.combinators.asJust
import com.github.h0tk3y.betterParse.combinators.leftAssociative
import com.github.h0tk3y.betterParse.combinators.map
import com.github.h0tk3y.betterParse.combinators.oneOrMore
import com.github.h0tk3y.betterParse.combinators.optional
import com.github.h0tk3y.betterParse.combinators.or
import com.github.h0tk3y.betterParse.combinators.separatedTerms
import com.github.h0tk3y.betterParse.combinators.times
import com.github.h0tk3y.betterParse.combinators.unaryMinus
import com.github.h0tk3y.betterParse.combinators.use
import com.github.h0tk3y.betterParse.grammar.Grammar
import com.github.h0tk3y.betterParse.grammar.parseToEnd
import com.github.h0tk3y.betterParse.grammar.parser
import com.github.h0tk3y.betterParse.lexer.literalToken
import com.github.h0tk3y.betterParse.lexer.regexToken
import com.github.h0tk3y.betterParse.parser.Parser
import expressions.BinaryOperator
import expressions.Expression
import okio.Source

/**
 * Represents the grammar of the Rinha language.
 * This grammar uses betterParse to handle parsing.
 *
 * betterParse parses the grammar and returns a list of [Expression]s.
 * [Expression] is part of my own internal logic.
 */
@Suppress("unused")
object RinhaGrammar : Grammar<List<Expression>>() {
    private val LPAR by literalToken("(")
    private val RPAR by literalToken(")")

    private val LBRC by literalToken("{")
    private val RBRC by literalToken("}")

    private val LINE_COMMENT = "//.*".toRegex()
    private val MULTI_LINE_COMMENT = "/\\*(.|\\n)*?\\*/".toRegex()

    private val LET by regexToken("let\\b")

    private const val RESERVED_FN_PRINT = "print"
    private const val RESERVED_FN_FIRST = "first"
    private const val RESERVED_FN_SECOND = "second"

    private val FUN by regexToken("fn\\b")
    private val FUN_ARROW by literalToken("=>")

    private val PLUS by literalToken("+")
    private val MINUS by literalToken("-")
    private val DIV by literalToken("/")
    private val MOD by literalToken("%")
    private val TIMES by literalToken("*")
    private val OR by literalToken("||")
    private val AND by literalToken("&&")
    private val EQU by literalToken("==")
    private val NEQ by literalToken("!=")
    private val LEQ by literalToken("<=")
    private val GEQ by literalToken(">=")
    private val LT by literalToken("<")
    private val GT by literalToken(">")

    private val COMMA by literalToken(",")
    private val SEMI by literalToken(";", ignore = true)
    private val ASGN by literalToken("=")

    private val IF by regexToken("if\\b")
    private val ELSE by regexToken("else\\b")

    private val TRUE by regexToken("true\\b")
    private val FALSE by regexToken("false\\b")

    private val NUMBER by regexToken("\\d+")
    private val CHARLIT by regexToken("'.'")
    private val STRINGLIT by regexToken("\".*?\"")

    private val ID by regexToken("[A-Za-z_]\\w*")

    private val WS by regexToken("\\s+", ignore = true)
    private val NEWLINE by regexToken("[\r\n]+", ignore = true)

    /** Maps literal strings to its corresponding [BinaryOperator]. */
    private val signToKind = mapOf(
        OR to BinaryOperator.Or,
        AND to BinaryOperator.And,
        LT to BinaryOperator.Lt,
        GT to BinaryOperator.Gt,
        EQU to BinaryOperator.Eq,
        NEQ to BinaryOperator.Neq,
        LEQ to BinaryOperator.Lte,
        GEQ to BinaryOperator.Gte,
        PLUS to BinaryOperator.Add,
        MINUS to BinaryOperator.Sub,
        TIMES to BinaryOperator.Mul,
        DIV to BinaryOperator.Div,
        MOD to BinaryOperator.Rem,
    )

    /** Handle Ints. E.g: -1, 2. */
    private val numConst by
        (optional(MINUS) map { if (it == null) 1 else -1 }) * NUMBER map { (signal, number) ->
            Expression.IntValue(
                value = signal * number.text.toInt(),
            )
        }

    /** Handle constants. E.g: true, false, 'a', "a". */
    private val const by
        numConst or
            CHARLIT.map { Expression.StrValue(value = it.text) } or
            (TRUE asJust Expression.BoolValue(value = true)) or
            (FALSE asJust Expression.BoolValue(value = false))

    /** Handle function calls. E.g: foo(), bar(1, 2). */
    private val funCall: Parser<Expression> by
        (ID * -LPAR * separatedTerms(parser { statement }, COMMA, acceptZero = true) * -RPAR).map { (name, args) ->
            when (name.text) {
                RESERVED_FN_PRINT -> {
                    Expression.Print(value = args)
                }
                RESERVED_FN_FIRST -> {
                    Expression.First(value = args)
                }
                RESERVED_FN_SECOND -> {
                    Expression.Second(value = args)
                }
                else -> {
                    Expression.Call(
                        callee = Expression.Var(name.text),
                        arguments = args,
                    )
                }
            }
        }

    /** Handle variables. E.g: foo, bar. Or any other name not reserved. */
    private val variable by ID use { Expression.Var(name = text) }

    /** Handle string literals. E.g: "foo", "bar". */
    private val stringLiteral by STRINGLIT use {
        Expression.StrValue(value = text.removeSurrounding("\"", "\""))
    }

    /** Handle tuple literals. E.g: (1, 2), (foo, bar). */
    private val tupleLiteral: Parser<Expression.TupleValue> by
        (-LPAR * parser { statement } * -COMMA * parser { statement } * -RPAR).map { (first, second) ->
            Expression.TupleValue(first = first, second = second)
        }

    /** Handle parenthesized terms. E.g: (1), (foo). */
    private val parenTerm by -LPAR * parser(this::expr) * -RPAR

    /**
     * Handle non-indexed terms. E.g: 1, foo, (1), "foo", (1, 2), first(foo), second(foo).
     * This is helper to join all the terms together.
     **/
    private val nonIndexedTerm: Parser<Expression> by
        const or funCall or variable or parenTerm or stringLiteral or tupleLiteral

    /** In my case I can simplify term = nonIndexedTerm. */
    private val term = nonIndexedTerm

    /**
     * Handle multiplication, division and modulo operators.
     * E.g: 1 * 2, 1 / 2, 1 % 2.
     */
    private val multiplicationOperator by TIMES or DIV or MOD
    private val multiplicationOrTerm by leftAssociative(term, multiplicationOperator) { left, operator, right ->
        Expression.Binary(
            left = left,
            right = right,
            operator = signToKind[operator.type]!!,
        )
    }

    /**
     * Handle sum and subtraction operators.
     * E.g: 1 + 2, 1 - 2.
     */
    private val sumOperator by PLUS or MINUS
    private val math: Parser<Expression> = leftAssociative(multiplicationOrTerm, sumOperator) { left, operator, right ->
        Expression.Binary(
            left = left,
            right = right,
            operator = signToKind[operator.type]!!,
        )
    }

    /**
     * Handle comparison operators.
     * E.g: 1 == 2, 1 != 2, 1 < 2, 1 > 2, 1 <= 2, 1 >= 2.
     */
    private val comparisonOperator by EQU or NEQ or LT or GT or LEQ or GEQ
    private val comparisonOrMath: Parser<Expression> by (math * optional(comparisonOperator * math))
        .map { (left, tail) ->
            tail?.let { (operator, right) ->
                Expression.Binary(
                    left = left,
                    right = right,
                    operator = signToKind[operator.type]!!,
                )
            } ?: left
        }

    /** Handle AND operators. E.g: 1 && 2. */
    private val andChain by leftAssociative(comparisonOrMath, AND) { left, _, right ->
        Expression.Binary(
            left = left,
            right = right,
            operator = BinaryOperator.And,
        )
    }

    /** Handle OR operators. E.g: 1 || 2. */
    private val orChain by leftAssociative(andChain, OR) { left, _, right ->
        Expression.Binary(
            left = left,
            right = right,
            operator = BinaryOperator.Or,
        )
    }

    /** Handle all expressions bellow. */
    private val expr: Parser<Expression> by orChain

    /**
     * Handle if statements. ELSE is optional. ELSE IF is not implemented.
     * E.g: if (true) { 2 } else { 3 }, if (foo) { 2 }.
     */
    private val ifStatement: Parser<Expression.If> by
        (
            -IF * -LPAR * expr * -RPAR * -LBRC *
                parser { statement } * -RBRC *
                -ELSE * -LBRC * parser { statement } * -RBRC
            ).map { (condition, then, otherwise) ->
            Expression.If(
                condition = condition,
                then = then,
                otherwise = otherwise,
            )
        }

    /**
     * Handle function definitions.
     * The name is optional, the parameters are optional, the arrow is optional.
     * E.g: fn foo() => { 1 }, fn foo(a, b) => { a + b }, fn (n) { print(n) }.
     */
    private val functionDefinitionStatement: Parser<Expression.Function> by
        (
            -FUN * optional(ID) * -LPAR * separatedTerms(
                ID,
                COMMA,
                acceptZero = true,
            ) * -RPAR * -optional(FUN_ARROW) * -LBRC * parser { statementsChain } * -RBRC
            ).map { (name, args, functionBody) ->
            Expression.Function(
                name = name?.text,
                parameters = args.map { it.text },
                value = functionBody,
            )
        }

    /**
     * Handle assignment statements.
     * E.g: let foo = 1, let foo = bar, let foo = (1, 2), let foo = first(bar).
     */
    private val assignmentStatement: Parser<Expression.Let> by
        (-LET * variable * -ASGN * parser { statement }).map { (variableName, expression) ->
            Expression.Let(
                name = variableName.name,
                value = expression,
            )
        }

    /**
     * Join all the valid statements together.
     */
    private val statement: Parser<Expression> by
        functionDefinitionStatement or
            assignmentStatement or
            ifStatement or
            expr

    /**
     * Handle a chain of statements.
     * E.g: print("hello"); print("world"); print("foo").
     */
    private val statementsChain: Parser<List<Expression>> by
        separatedTerms(statement, optional(SEMI)) * -optional(SEMI) map { listOf(*it.toTypedArray()) }

    /**
     * Handle the root parser.
     * This is the "entry point"
     */
    override val rootParser: Parser<List<Expression>> by oneOrMore(statement)

    /**
     * Parse a [Source] and return a list of [Expression]s.
     */
    fun parseSource(source: String): List<Expression> {
        return source.replace(MULTI_LINE_COMMENT, "")
            .replace(LINE_COMMENT, "")
            .let(::parseToEnd)
    }
}
