package runner

import com.github.h0tk3y.betterParse.grammar.parseToEnd
import com.github.h0tk3y.betterParse.parser.ParseException
import expressions.BinaryOperator
import expressions.Expression
import parser.RinhaGrammar
import platform.posix.FP_NAN
import platform.posix.NAN
import kotlin.time.measureTime

class ExpressionRunner(
    val context: RunTimeContext = RunTimeContext(),
) {
    /**
     * Run the expressions from a Rinha source code.
     * @param source The source code to run.
     * @return The result of the last expression.
     */
    fun runFromSource(source: String): Any? {
        return runCatching {
            RinhaGrammar.parseToEnd(source)
        }.onFailure {
            if (it is ParseException) {
                println("e: Syntax error")
            } else {
                println("e: ${it.message}")
            }
        }.onSuccess { expressions ->
            run(expressions)?.also { response ->
                val last = context.output.lastOrNull()
                if (last == null || last != response.toString()) {
                    println(response.toString())
                }
            }
        }.getOrNull()
    }

    /**
     * Run the expressions from the AST.
     * @param expressions The list of expressions to run.
     * @throws RuntimeException If any expression fails.
     * @return The result of the last expression.
     */
    fun run(expressions: List<Expression>): Any? {
        if (expressions.isEmpty()) {
            throw RuntimeException("No expressions to run")
        }

        return expressions.fold<Expression, Any?>(null) { _, expression ->
            runExpression(
                expression = expression,
                scope = context.variables,
            )
        }
    }

    /**
     * Clear the output.
     */
    fun clearOutput() {
        context.output.clear()
    }

    /**
     * Dump the output.
     */
    fun dumpOutput() {
        context.output.joinToString(separator = "\n").also {
            if (!context.isTesting) {
                println(it)
            }
        }
    }

    /**
     * Print the global scope.
     */
    fun printGlobalScope() {
        if (context.variables.isEmpty()) {
            println("{}")
        } else {
            println("{")
            context.variables.forEach {
                when (it.value) {
                    is Int -> {
                        val variable = it.value as Int
                        println("  ${it.key}: $variable,")
                    }
                    is String -> {
                        val variable = it.value as String
                        println("  ${it.key}: \"$variable\",")
                    }
                    is Boolean -> {
                        val variable = it.value as Boolean
                        println("  ${it.key}: $variable,")
                    }
                    is Expression.Function -> {
                        val variable = it.value as Expression.Function
                        println("  ${it.key}: $variable,")
                    }
                    else -> {
                        if (it.value == null) {
                            println("  ${it.key}: null,")
                        } else {
                            println("  ${it.key}: Any,")
                        }
                    }
                }
            }
            println("}")
        }
    }

    private fun runExpression(
        expression: Expression,
        scope: MutableMap<String, Any?>,
    ): Any? {
        return when (expression) {
            is Expression.Var -> varExpression(expression, scope)
            is Expression.IntValue -> intValueExpression(expression)
            is Expression.BoolValue -> boolValueExpression(expression)
            is Expression.StrValue -> strValueExpression(expression)
            is Expression.Let -> letExpression(expression, scope)
            is Expression.Binary -> binaryExpression(expression, scope)
            is Expression.If -> ifExpression(expression, scope)
            is Expression.Function -> functionExpression(expression)
            is Expression.Call -> callExpression(expression, scope)
            is Expression.TupleValue -> tupleValueExpression(expression, scope)
            is Expression.First -> firstExpression(expression, scope)
            is Expression.Second -> secondExpression(expression, scope)
            is Expression.Print -> printExpression(expression, scope)
        }
    }

    private fun varExpression(
        expression: Expression.Var,
        scope: MutableMap<String, Any?>,
    ): Any? {
        val value = scope[expression.name] ?: context.variables[expression.name]
        return if (value is Lazy<*>) {
            value.value
        } else {
            value
        }
    }

    private fun binaryExpression(
        expression: Expression.Binary,
        scope: MutableMap<String, Any?>,
    ): Any {
        return when (expression.operator) {
            BinaryOperator.Add -> mathAddExpression(expression, scope)
            BinaryOperator.Sub -> mathSubExpression(expression, scope)
            BinaryOperator.Mul -> mathMulExpression(expression, scope)
            BinaryOperator.Div -> mathDivExpression(expression, scope)
            BinaryOperator.Rem -> mathRemExpression(expression, scope)
            BinaryOperator.Eq -> {
                val left = runExpression(expression.left, scope)
                val right = runExpression(expression.right, scope)
                left == right
            }
            BinaryOperator.Neq -> {
                val left = runExpression(expression.left, scope)
                val right = runExpression(expression.right, scope)
                left != right
            }
            BinaryOperator.Or -> logicOrExpression(expression, scope)
            BinaryOperator.And -> logicAndExpression(expression, scope)
            BinaryOperator.Lt -> {
                val left = runExpression(expression.left, scope)
                val right = runExpression(expression.right, scope)
                val asInt = left is Int && right is Int && left < right
                val asDouble = left is Double && right is Double && left < right
                asInt || asDouble
            }
            BinaryOperator.Lte -> {
                val left = runExpression(expression.left, scope)
                val right = runExpression(expression.right, scope)
                val asInt = left is Int && right is Int && left <= right
                val asDouble = left is Double && right is Double && left <= right
                asInt || asDouble
            }
            BinaryOperator.Gt -> {
                val left = runExpression(expression.left, scope)
                val right = runExpression(expression.right, scope)
                val asInt = left is Int && right is Int && left > right
                val asDouble = left is Double && right is Double && left > right
                asInt || asDouble
            }
            BinaryOperator.Gte -> {
                val left = runExpression(expression.left, scope)
                val right = runExpression(expression.right, scope)
                val asInt = left is Int && right is Int && left >= right
                val asDouble = left is Double && right is Double && left >= right
                asInt || asDouble
            }
        }
    }

    private fun mathRemExpression(
        expression: Expression.Binary,
        scope: MutableMap<String, Any?>,
    ): Any {
        val left = runExpression(expression.left, scope)
        val right = runExpression(expression.right, scope)
        return if (left is Int && right is Int) {
            left.rem(right)
        } else {
            throw RuntimeException("Invalid Sub binary expression")
        }
    }

    private fun mathDivExpression(
        expression: Expression.Binary,
        scope: MutableMap<String, Any?>,
    ): Any {
        val left = runExpression(expression.left, scope)

        return if (left == 0 || left == 0.0) {
            0
        } else {
            val right = runExpression(expression.right, scope)
            if (left is Int && right is Int) {
                if (right == 0) {
                    NAN
                } else {
                    left / right
                }
            } else if (left is Double && right is Double) {
                if (right == 0.0) {
                    FP_NAN
                } else {
                    left / right
                }
            } else {
                throw RuntimeException("Invalid Sub binary expression")
            }
        }
    }

    private fun mathMulExpression(
        expression: Expression.Binary,
        scope: MutableMap<String, Any?>,
    ): Any {
        val left = runExpression(expression.left, scope)

        return if (left == 0 || left == 0.0) {
            0
        } else {
            val right = runExpression(expression.right, scope)
            if (left is Int && right is Int) {
                left * right
            } else if (left is Double && right is Double) {
                left * right
            } else {
                throw RuntimeException("Invalid Sub binary expression")
            }
        }
    }

    private fun mathSubExpression(
        expression: Expression.Binary,
        scope: MutableMap<String, Any?>,
    ): Any {
        val left = runExpression(expression.left, scope)
        val right = runExpression(expression.right, scope)

        return if (left is Int && right is Int) {
            left - right
        } else if (left is Double && right is Double) {
            left - right
        } else {
            throw RuntimeException("Invalid Sub binary expression")
        }
    }

    private fun mathAddExpression(
        expression: Expression.Binary,
        scope: MutableMap<String, Any?>,
    ): Any {
        val left = runExpression(expression.left, scope)
        val right = runExpression(expression.right, scope)

        return if (left is Int && right is Int) {
            left + right
        } else if (left is Double && right is Double) {
            left + right
        } else if (left is String || right is String) {
            left.toString() + right.toString()
        } else if (left is String) {
            left + right.toString()
        } else {
            throw RuntimeException("Invalid Add binary expression")
        }
    }

    private fun logicOrExpression(
        expression: Expression.Binary,
        scope: MutableMap<String, Any?>,
    ): Boolean {
        val left = runExpression(expression.left, scope)
        val leftPart = ((left is Boolean && left) || (left is Int && left != 0))
        if (leftPart) {
            return true
        }

        val right = runExpression(expression.right, scope)
        return ((right is Boolean && right) || (right is Int && right != 0))
    }

    private fun logicAndExpression(
        expression: Expression.Binary,
        scope: MutableMap<String, Any?>,
    ): Boolean {
        val left = runExpression(expression.left, scope)
        val leftPart = ((left is Boolean && left) || (left is Int && left != 0))
        if (!leftPart) {
            return false
        }

        val right = runExpression(expression.right, scope)
        return ((right is Boolean && right) || (right is Int && right != 0))
    }

    private fun firstExpression(
        expression: Expression.First,
        scope: MutableMap<String, Any?>,
    ): Any? {
        return if (expression.value is Expression.TupleValue) {
            runExpression(expression.value.first, scope)
        } else {
            when (val value = runExpression(expression.value, scope)) {
                is Pair<*, *> -> value.first
                else -> throw RuntimeException("Invalid First expression")
            }
        }
    }

    private fun secondExpression(
        expression: Expression.Second,
        scope: MutableMap<String, Any?>,
    ): Any? {
        return if (expression.value is Expression.TupleValue) {
            runExpression(expression.value.second, scope)
        } else {
            when (val value = runExpression(expression.value, scope)) {
                is Pair<*, *> -> value.second
                else -> throw RuntimeException("Invalid First expression")
            }
        }
    }

    private fun ifExpression(
        expression: Expression.If,
        scope: MutableMap<String, Any?>,
    ): Any? {
        val condition = runExpression(
            expression = expression.condition,
            scope = scope,
        )
        return if (condition == true) {
            expression.then.fold<Expression, Any?>(null) { _, functionExpression ->
                runExpression(
                    expression = functionExpression,
                    scope = scope,
                )
            }
        } else {
            expression.otherwise?.fold<Expression, Any?>(null) { _, functionExpression ->
                runExpression(
                    expression = functionExpression,
                    scope = scope,
                )
            }
        }
    }

    private fun strValueExpression(
        expression: Expression.StrValue,
    ): Any {
        return expression.value
    }

    private fun intValueExpression(
        expression: Expression.IntValue,
    ): Any {
        return expression.value
    }

    private fun boolValueExpression(
        expression: Expression.BoolValue,
    ): Any {
        return expression.value
    }

    private fun tupleValueExpression(
        expression: Expression.TupleValue,
        scope: MutableMap<String, Any?>,
    ): Any {
        val first = runExpression(expression.first, scope)
        val second = runExpression(expression.second, scope)
        return first to second
    }

    private fun letExpression(
        expression: Expression.Let,
        scope: MutableMap<String, Any?>,
    ): Any? {
        scope[expression.name] = runExpression(expression.value, scope)
        return null
    }

    private fun functionExpression(
        expression: Expression.Function,
    ): Any {
        // Function will be executed only when called.
        return expression
    }

    private fun printExpression(
        expression: Expression.Print,
        scope: MutableMap<String, Any?>,
    ): Any? {
        val value = runExpression(expression.value, scope)

        context.output.run {
            if (size > context.maxOutputSize.coerceAtLeast(10)) {
                // Clean the output if it's too big
                context.output.joinToString(separator = "\n").also {
                    if (!context.isTesting) {
                        println(value)
                    }
                }

                clear()
            }

            add(value.toString())
        }

        return value
    }

    private fun callExpression(
        expression: Expression.Call,
        scope: MutableMap<String, Any?>,
    ): Any? {
        val targetWrapped = context.variables[expression.callee.name]
        val target = if (targetWrapped is Lazy<*>) {
            targetWrapped.value
        } else {
            targetWrapped
        }

        return when (target) {
            null -> {
                throw RuntimeException("Function '${expression.callee.name}' is not defined")
            }
            !is Expression.Function -> {
                throw RuntimeException("'${expression.callee.name}' is not valid function")
            }
            (target.parameters.size != expression.arguments.size) -> {
                throw RuntimeException("Missing param at '${expression.callee.name}' call")
            }
            else -> {
                val result: Any? = if (context.runtimeOptimization) {
                    RunTimeOptimizations.checkRunTimeOptimizations(expression, target.value)
                } else {
                    null
                }

                if (result != null) {
                    return result
                } else {
                    val newScope = target.parameters.mapIndexed { index: Int, param: String ->
                        param to runExpression(expression.arguments[index], scope)
                    }.associate {
                        it.first to it.second
                    }.toMutableMap()

                    target
                        .value
                        .fold<Expression, Any?>(null) { _, functionExpression ->
                            runExpression(
                                expression = functionExpression,
                                scope = newScope,
                            )
                        }
                }
            }
        }
    }
}
