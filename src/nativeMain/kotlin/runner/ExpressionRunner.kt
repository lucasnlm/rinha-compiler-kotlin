package runner

import expressions.BinaryOperator
import expressions.Expression
import platform.posix.FP_NAN
import platform.posix.NAN

class ExpressionRunner(
    val context: RunTimeContext = RunTimeContext(),
) {
    /**
     * Run the expressions from the AST.
     * @param expressions The list of expressions to run.
     * @throws RuntimeException If any expression fails.
     */
    fun run(expressions: List<Expression>) {
        if (expressions.isEmpty()) {
            throw RuntimeException("No expressions to run")
        }

        expressions.forEach {
            runExpression(it, context.variables)
        }
    }

    private fun runExpression(
        expression: Expression,
        scope: MutableMap<String, Any?>,
    ): Any? {
        return when (expression) {
            is Expression.Root -> null
            is Expression.Let -> letExpression(expression, scope)
            is Expression.Function -> functionExpression(expression)
            is Expression.Print -> printExpression(expression, scope)
            is Expression.Call -> callExpression(expression, scope)
            is Expression.IntValue -> intValueExpression(expression)
            is Expression.BoolValue -> boolValueExpression(expression)
            is Expression.TupleValue -> tupleValueExpression(expression, scope)
            is Expression.StrValue -> strValueExpression(expression)
            is Expression.First -> firstExpression(expression, scope)
            is Expression.Second -> secondExpression(expression, scope)
            is Expression.If -> ifExpression(expression, scope)
            is Expression.Binary -> binaryExpression(expression, scope)
            is Expression.Var -> varExpression(expression, scope)
        }
    }

    private fun varExpression(
        expression: Expression.Var,
        scope: MutableMap<String, Any?>,
    ): Any? {
        return scope[expression.name] ?: context.variables[expression.name]
    }

    private fun binaryExpression(
        expression: Expression.Binary,
        scope: MutableMap<String, Any?>,
    ): Any {
        val left = runExpression(expression.left, scope)
        val right = runExpression(expression.right, scope)
        return when (expression.operator) {
            BinaryOperator.Add -> {
                if (left is Int && right is Int) {
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
            BinaryOperator.Sub -> {
                if (left is Int && right is Int) {
                    left - right
                } else if (left is Double && right is Double) {
                    left - right
                } else {
                    throw RuntimeException("Invalid Sub binary expression")
                }
            }
            BinaryOperator.Mul -> {
                if (left is Int && right is Int) {
                    left * right
                } else if (left is Double && right is Double) {
                    left * right
                } else {
                    throw RuntimeException("Invalid Sub binary expression")
                }
            }
            BinaryOperator.Div -> {
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
            BinaryOperator.Rem -> {
                if (left is Int && right is Int) {
                    left.rem(right)
                } else {
                    throw RuntimeException("Invalid Sub binary expression")
                }
            }
            BinaryOperator.Eq -> {
                left == right
            }
            BinaryOperator.Neq -> {
                left != right
            }
            BinaryOperator.Or -> {
                val asBoolean = ((left is Boolean && left) || (right is Boolean && right))
                val asInt = ((left is Int && left != 0) || (right is Int && right != 0))
                asBoolean || asInt
            }
            BinaryOperator.And -> {
                val asBoolean = ((left is Boolean && left) && (right is Boolean && right))
                val asInt = (left is Int && left != 0) && (right is Int && right != 0)
                asBoolean || asInt
            }
            BinaryOperator.Lt -> {
                val asInt = left is Int && right is Int && left < right
                val asDouble = left is Double && right is Double && left < right
                asInt || asDouble
            }
            BinaryOperator.Lte -> {
                val asInt = left is Int && right is Int && left <= right
                val asDouble = left is Double && right is Double && left <= right
                asInt || asDouble
            }
            BinaryOperator.Gt -> {
                val asInt = left is Int && right is Int && left > right
                val asDouble = left is Double && right is Double && left > right
                asInt || asDouble
            }
            BinaryOperator.Gte -> {
                val asInt = left is Int && right is Int && left >= right
                val asDouble = left is Double && right is Double && left >= right
                asInt || asDouble
            }
        }
    }

    private fun firstExpression(
        expression: Expression.First,
        scope: MutableMap<String, Any?>,
    ): Any? {
        return when (val value = runExpression(expression.value, scope)) {
            is Pair<*, *> -> value.first
            else -> throw RuntimeException("Invalid First expression")
        }
    }

    private fun secondExpression(
        expression: Expression.Second,
        scope: MutableMap<String, Any?>,
    ): Any? {
        return when (val value = runExpression(expression.value, scope)) {
            is Pair<*, *> -> value.second
            else -> throw RuntimeException("Invalid First expression")
        }
    }

    private fun ifExpression(
        expression: Expression.If,
        scope: MutableMap<String, Any?>,
    ): Any? {
        val condition = runExpression(expression.condition, scope)
        return if (condition == true) {
            runExpression(expression.then, scope)
        } else {
            runExpression(expression.otherwise, scope)
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
        if (!context.isTesting) {
            println(value)
        }
        context.output.add(value.toString())
        return null
    }

    private fun callExpression(
        expression: Expression.Call,
        scope: MutableMap<String, Any?>,
    ): Any? {
        return when (val target = context.variables[expression.callee.name]) {
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
