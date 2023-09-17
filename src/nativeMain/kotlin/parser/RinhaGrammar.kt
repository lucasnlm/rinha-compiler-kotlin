package parser

import com.github.h0tk3y.betterParse.combinators.*
import com.github.h0tk3y.betterParse.grammar.parseToEnd
import com.github.h0tk3y.betterParse.grammar.parser

import com.github.h0tk3y.betterParse.combinators.oneOrMore
import com.github.h0tk3y.betterParse.grammar.Grammar
import com.github.h0tk3y.betterParse.lexer.literalToken
import com.github.h0tk3y.betterParse.lexer.regexToken
import com.github.h0tk3y.betterParse.lexer.token
import com.github.h0tk3y.betterParse.parser.Parser
import com.github.h0tk3y.betterParse.parser.parse
import expressions.Expression

object RinhaGrammar : Grammar<List<Expression>>() {
    private val LET by literalToken("let")

    private val ID by regexToken("[A-Za-z]\\w*")

    // Constant literals
    private val STRING_LITERAL by regexToken("\".*?\"")
    private val CHAR_LITERAL by regexToken("'.'")
    private val NUMBER_LITERAL by regexToken("\\d+")
    private val BOOLEAN_LITERAL by regexToken("(true)|(false)")

    private val MINUS by literalToken("-")

    private val WS by regexToken("\\s+", ignore = true)
    private val NEWLINE by regexToken("[\r\n]+", ignore = true)
    private val COMMA by literalToken(",")
    private val SEMI by literalToken(";")
    private val ASGN by literalToken("=")



    private val variable by ID use { text }

    private val numberConst by lazy {
        (optional(MINUS) map { if (it == null) 1 else -1 }) * NUMBER_LITERAL map {
            (signal, it) -> Expression.IntValue(signal * it.text.toInt())
        }
    }
    private val stringLiteral by STRING_LITERAL use {
        Expression.StrValue(text.removeSurrounding("\"", "\""))
    }
    private val booleanLiteral by BOOLEAN_LITERAL use {
        Expression.BoolValue(text.toBoolean())
    }


    private val letStatement: Parser<Expression> by
        (-LET * variable * -ASGN * numConst).map { (name, value) ->
            Expression.Let(name, value)
        }

    private val statement: Parser<Expression> by letStatement


    override val rootParser: Parser<List<Expression>> by oneOrMore(
        statement
    )
}