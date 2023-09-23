package runner

import com.github.h0tk3y.betterParse.grammar.parseToEnd
import com.github.h0tk3y.betterParse.parser.ParseException
import expressions.BinaryOperator
import expressions.Expression
import parser.RinhaGrammar
import platform.posix.NAN

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
            RinhaGrammar.parseSource(source)
        }.onFailure {
            if (it is ParseException) {
                println("e: syntax error")
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
            throw RuntimeException("no expressions to run")
        }

        return expressions.fold<Expression, Any?>(null) { _, expression ->
            runExpression(
                expression = expression,
                scope = context.variables,
                root = null,
                recursiveCall = 0,
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
     * Print the global scope.
     */
    fun printGlobalScope() {
        if (context.variables.isEmpty()) {
            println("{}")
        } else {
            context.variables.map {
                when (it.value) {
                    is Int -> {
                        val variable = it.value as Int
                        "${it.key}: $variable"
                    }

                    is String -> {
                        val variable = it.value as String
                        "${it.key}: \"$variable\""
                    }

                    is Boolean -> {
                        val variable = it.value as Boolean
                        "${it.key}: $variable"
                    }

                    is Expression.Function -> {
                        val variable = it.value as Expression.Function
                        "${it.key}: $variable"
                    }

                    else -> {
                        if (it.value == null) {
                            "${it.key}: null"
                        } else {
                            "${it.key}: Any"
                        }
                    }
                }
            }.joinToString(prefix = "{\n", separator = ",\n", postfix = ",\n}") {
                "  $it"
            }.also {
                if (!context.isTesting && it.isNotBlank()) {
                    println(it)
                }
            }
        }
    }

    private fun runExpression(
        expression: Expression,
        scope: MutableMap<String, Any?>,
        root: String?,
        recursiveCall: Int,
    ): Any? {
        return when (expression) {
            is Expression.Var -> varExpression(expression, scope)
            is Expression.IntValue -> intValueExpression(expression)
            is Expression.BoolValue -> boolValueExpression(expression)
            is Expression.StrValue -> strValueExpression(expression)
            is Expression.Let -> letExpression(expression, scope, root, recursiveCall)
            is Expression.Binary -> binaryExpression(expression, scope, root, recursiveCall)
            is Expression.If -> ifExpression(expression, scope, root, recursiveCall)
            is Expression.Function -> functionExpression(scope, expression)
            is Expression.Call -> callExpression(expression, scope, root, recursiveCall)
            is Expression.TupleValue -> tupleValueExpression(expression, scope, root, recursiveCall)
            is Expression.First -> firstExpression(expression, scope, root, recursiveCall)
            is Expression.Second -> secondExpression(expression, scope, root, recursiveCall)
            is Expression.Print -> printExpression(expression, scope, root, recursiveCall)
        }
    }

    private fun varExpression(
        expression: Expression.Var,
        scope: Map<String, Any?>,
    ): Any? {
        val value =
            scope[expression.name]
                ?: context.variables[expression.name]
                ?: throw RuntimeException("variable '${expression.name}' is not defined")

        return if (value is Lazy<*>) {
            value.value
        } else {
            value
        }
    }

    private fun binaryExpression(
        expression: Expression.Binary,
        scope: MutableMap<String, Any?>,
        root: String?,
        recursiveCall: Int,
    ): Any {
        return when (expression.operator) {
            BinaryOperator.Add -> {
                mathAddExpression(
                    expression = expression,
                    scope = scope,
                    root = root,
                    recursiveCall = recursiveCall,
                )
            }
            BinaryOperator.Sub -> {
                mathSubExpression(
                    expression = expression,
                    scope = scope,
                    root = root,
                    recursiveCall = recursiveCall,
                )
            }
            BinaryOperator.Mul -> mathMulExpression(
                expression = expression,
                scope = scope,
                root = root,
                recursiveCall = recursiveCall,
            )
            BinaryOperator.Div -> mathDivExpression(
                expression = expression,
                scope = scope,
                root = root,
                recursiveCall = recursiveCall,
            )
            BinaryOperator.Rem -> mathRemExpression(
                expression = expression,
                scope = scope,
                root = root,
                recursiveCall = recursiveCall,
            )
            BinaryOperator.Eq -> {
                comparativeExpression(
                    expression = expression,
                    scope = scope,
                    root = root,
                    recursiveCall = recursiveCall,
                ) { left, right -> left == right }
            }
            BinaryOperator.Neq -> {
                comparativeExpression(
                    expression = expression,
                    scope = scope,
                    root = root,
                    recursiveCall = recursiveCall,
                ) { left, right -> left != right }
            }
            BinaryOperator.Or -> {
                logicOrExpression(
                    expression = expression,
                    scope = scope,
                    root = root,
                    recursiveCall = recursiveCall,
                )
            }
            BinaryOperator.And -> {
                logicAndExpression(
                    expression = expression,
                    scope = scope,
                    root = root,
                    recursiveCall = recursiveCall,
                )
            }
            BinaryOperator.Lt -> {
                comparativeMathExpression(
                    expression = expression,
                    scope = scope,
                    root = root,
                    recursiveCall = recursiveCall,
                ) { left, right -> left < right }
            }
            BinaryOperator.Lte -> {
                comparativeMathExpression(
                    expression = expression,
                    scope = scope,
                    root = root,
                    recursiveCall = recursiveCall,
                ) { left, right -> left <= right }
            }
            BinaryOperator.Gt -> {
                comparativeMathExpression(
                    expression = expression,
                    scope = scope,
                    root = root,
                    recursiveCall = recursiveCall,
                ) { left, right -> left > right }
            }
            BinaryOperator.Gte -> {
                comparativeMathExpression(
                    expression = expression,
                    scope = scope,
                    root = root,
                    recursiveCall = recursiveCall,
                ) { left, right -> left >= right }
            }
        }
    }

    private fun comparativeMathExpression(
        expression: Expression.Binary,
        scope: MutableMap<String, Any?>,
        root: String?,
        recursiveCall: Int,
        block: (left: Int, right: Int) -> Boolean,
    ): Boolean {
        val left = runExpression(expression.left, scope, root, recursiveCall) as? Int
        val right = runExpression(expression.right, scope, root, recursiveCall) as? Int

        if (left == null) {
            throw RuntimeException("left side of the expression is null")
        }
        if (right == null) {
            throw RuntimeException("right side of the expression is null")
        }

        return block(left, right)
    }

    private fun comparativeExpression(
        expression: Expression.Binary,
        scope: MutableMap<String, Any?>,
        root: String?,
        recursiveCall: Int,
        block: (left: Any?, right: Any?) -> Boolean,
    ): Boolean {
        val left = runExpression(expression.left, scope, root, recursiveCall)
        val right = runExpression(expression.right, scope, root, recursiveCall)
        return block(left, right)
    }

    private fun mathRemExpression(
        expression: Expression.Binary,
        scope: MutableMap<String, Any?>,
        root: String?,
        recursiveCall: Int,
    ): Any {
        val left = runExpression(expression.left, scope, root, recursiveCall)
        val right = runExpression(expression.right, scope, root, recursiveCall)
        return if (left is Int && right is Int) {
            left.rem(right)
        } else {
            throw RuntimeException("invalid rem expression")
        }
    }

    private fun mathDivExpression(
        expression: Expression.Binary,
        scope: MutableMap<String, Any?>,
        root: String?,
        recursiveCall: Int,
    ): Any {
        val left = runExpression(expression.left, scope, root, recursiveCall)

        return if (left == 0 || left == 0.0) {
            0
        } else {
            val right = runExpression(expression.right, scope, root, recursiveCall)
            if (left is Int && right is Int) {
                if (right == 0) {
                    NAN
                } else {
                    (left / right)
                }
            } else {
                throw RuntimeException("invalid division expression")
            }
        }
    }

    private fun mathMulExpression(
        expression: Expression.Binary,
        scope: MutableMap<String, Any?>,
        root: String?,
        recursiveCall: Int,
    ): Any {
        val left = runExpression(expression.left, scope, root, recursiveCall)

        return if (left == 0 || left == 0.0) {
            0
        } else {
            val right = runExpression(expression.right, scope, root, recursiveCall)
            if (left is Int && right is Int) {
                left * right
            } else {
                throw RuntimeException("invalid multiplication expression")
            }
        }
    }

    private fun mathSubExpression(
        expression: Expression.Binary,
        scope: MutableMap<String, Any?>,
        root: String?,
        recursiveCall: Int,
    ): Any {
        val left = runExpression(expression.left, scope, root, recursiveCall)
        val right = runExpression(expression.right, scope, root, recursiveCall)

        return if (left is Int && right is Int) {
            left - right
        } else {
            throw RuntimeException("invalid sub expression")
        }
    }

    private fun mathAddExpression(
        expression: Expression.Binary,
        scope: MutableMap<String, Any?>,
        root: String?,
        recursiveCall: Int,
    ): Any {
        val left = runExpression(expression.left, scope, root, recursiveCall)
        val right = runExpression(expression.right, scope, root, recursiveCall)

        return if (left is Int && right is Int) {
            left + right
        } else if (left is String || right is String) {
            left.toString() + right.toString()
        } else if (left is String) {
            left + right.toString()
        } else {
            throw RuntimeException("invalid add expression")
        }
    }

    private fun logicOrExpression(
        expression: Expression.Binary,
        scope: MutableMap<String, Any?>,
        root: String?,
        recursiveCall: Int,
    ): Boolean {
        val left = runExpression(expression.left, scope, root, recursiveCall)
        val leftPart = ((left is Boolean && left) || (left is Int && left != 0))
        if (leftPart) {
            return true
        }

        val right = runExpression(expression.right, scope, root, recursiveCall)
        return ((right is Boolean && right) || (right is Int && right != 0))
    }

    private fun logicAndExpression(
        expression: Expression.Binary,
        scope: MutableMap<String, Any?>,
        root: String?,
        recursiveCall: Int,
    ): Boolean {
        val left = runExpression(expression.left, scope, root, recursiveCall)
        val leftPart = ((left is Boolean && left) || (left is Int && left != 0))
        if (!leftPart) {
            return false
        }

        val right = runExpression(expression.right, scope, root, recursiveCall)
        return ((right is Boolean && right) || (right is Int && right != 0))
    }

    private fun firstExpression(
        expression: Expression.First,
        scope: MutableMap<String, Any?>,
        root: String?,
        recursiveCall: Int,
    ): Any? {
        return if (expression.value.size > 1) {
            throw RuntimeException("'first' function can only one argument")
        } else {
            val value = expression.value.first()
            if (value is Expression.TupleValue) {
                runExpression(value.first, scope, root, recursiveCall)
            } else {
                when (val valueExpr = runExpression(value, scope, root, recursiveCall)) {
                    is Pair<*, *> -> valueExpr.first
                    else -> throw RuntimeException("'first' function can only handle Tuples")
                }
            }
        }
    }

    private fun secondExpression(
        expression: Expression.Second,
        scope: MutableMap<String, Any?>,
        root: String?,
        recursiveCall: Int,
    ): Any? {
        return if (expression.value.size > 1) {
            throw RuntimeException("'second' function can only one argument")
        } else {
            val value = expression.value.first()
            if (value is Expression.TupleValue) {
                runExpression(value.second, scope, root, recursiveCall)
            } else {
                when (val valueExpr = runExpression(value, scope, root, recursiveCall)) {
                    is Pair<*, *> -> valueExpr.second
                    else -> throw RuntimeException("'second' function can only handle Tuples")
                }
            }
        }
    }

    private fun ifExpression(
        expression: Expression.If,
        scope: MutableMap<String, Any?>,
        root: String?,
        recursiveCall: Int,
    ): Any? {
        val condition = runExpression(
            expression = expression.condition,
            scope = scope,
            root = root,
            recursiveCall = recursiveCall,
        )
        return if (condition == true) {
            expression.then.fold<Expression, Any?>(null) { _, functionExpression ->
                runExpression(
                    expression = functionExpression,
                    scope = scope,
                    root = root,
                    recursiveCall = recursiveCall,
                )
            }
        } else {
            expression.otherwise?.fold<Expression, Any?>(null) { _, functionExpression ->
                runExpression(
                    expression = functionExpression,
                    scope = scope,
                    root = root,
                    recursiveCall = recursiveCall,
                )
            }
        }
    }

    private fun strValueExpression(
        expression: Expression.StrValue,
    ): String {
        return expression.value
    }

    private fun intValueExpression(
        expression: Expression.IntValue,
    ): Int {
        return expression.value
    }

    private fun boolValueExpression(
        expression: Expression.BoolValue,
    ): Boolean {
        return expression.value
    }

    private fun tupleValueExpression(
        expression: Expression.TupleValue,
        scope: MutableMap<String, Any?>,
        root: String?,
        recursiveCall: Int,
    ): Any {
        val first = runExpression(expression.first, scope, root, recursiveCall)
        val second = runExpression(expression.second, scope, root, recursiveCall)
        return first to second
    }

    private fun letExpression(
        expression: Expression.Let,
        scope: MutableMap<String, Any?>,
        root: String?,
        recursiveCall: Int,
    ): Any? {
        val value = runExpression(
            expression = expression.value,
            scope = scope,
            root = root,
            recursiveCall = recursiveCall,
        )
        val sanitizedName = expression.name.replace("_", "")
        if (sanitizedName.isNotBlank()) {
            if (RESERVED_WORDS.contains(sanitizedName)) {
                throw RuntimeException("can't use reserved word '$sanitizedName' as variable name")
            }
            scope[expression.name] = value
        }
        return value
    }

    private fun functionExpression(
        scope: Map<String, Any?>,
        expression: Expression.Function,
    ): Any {
        // Function will be executed only when called.
        return expression.copy(scopeCopy = scope + context.variables)
    }

    private fun printExpression(
        expression: Expression.Print,
        scope: MutableMap<String, Any?>,
        root: String?,
        recursiveCall: Int,
    ): Any? {
        val result = if (expression.value.size == 1) {
            runExpression(
                expression = expression.value.first(),
                scope = scope,
                root = root,
                recursiveCall = recursiveCall,
            ).also {
                val asString = it.toString()
                context.output.add(asString)
                println(asString)
            }
        } else {
            throw RuntimeException("'print' function can handle only one argument")
        }

        context.output.run {
            if (size > context.maxOutputSize.coerceAtLeast(10)) {
                // Clean the output if it's too big
                clear()
            }
        }

        return result
    }

    private fun callExpression(
        expression: Expression.Call,
        scope: MutableMap<String, Any?>,
        root: String?,
        recursiveCall: Int = 0,
    ): Any? {
        val targetWrapped = context.variables[expression.callee.name]
        val target = if (targetWrapped is Lazy<*>) {
            targetWrapped.value
        } else {
            targetWrapped
        }

        return when (target) {
            null -> {
                throw RuntimeException("function '${expression.callee.name}' is not defined")
            }
            !is Expression.Function -> {
                throw RuntimeException("'${expression.callee.name}' is not a function")
            }
            (target.parameters.size != expression.arguments.size) -> {
                throw RuntimeException("missing param at '${expression.callee.name}' call")
            }
            else -> {
                val result: Any? = if (context.runtimeOptimization) {
                    RunTimeOptimizations.checkRunTimeOptimizations(
                        exprCall = expression,
                        expressions = target.value,
                    )
                } else {
                    null
                }

                val newRecursiveCall = if (root == expression.callee.name) {
                    // Recursive call detected
                    recursiveCall + 1
                } else {
                    1
                }

                if (recursiveCall > 900) {
                    throw RuntimeException("recursive call limit exceeded")
                }

                if (result != null) {
                    result
                } else {
                    val functionScope = (scope + target.scopeCopy).toMutableMap()

                    val resolvedArguments = target.parameters.mapIndexed { index: Int, param: String ->
                        param to runExpression(
                            expression = expression.arguments[index],
                            scope = functionScope,
                            root = expression.callee.name,
                            recursiveCall = newRecursiveCall,
                        )
                    }

                    val newScope = target.scopeCopy.toMutableMap().apply { putAll(resolvedArguments) }

                    val cached = if (context.cacheEnabled) {
                        context.functionCache[expression.callee.name]?.get(newScope.toString())
                    } else {
                        null
                    }

                    cached ?: target
                        .value
                        .fold<Expression, Any?>(null) { _, functionExpression ->
                            runExpression(
                                expression = functionExpression,
                                scope = newScope,
                                root = expression.callee.name,
                                recursiveCall = newRecursiveCall,
                            )
                        }.also {
                            if (context.cacheEnabled) {
                                val cacheMap = context.functionCache[expression.callee.name] ?: mutableMapOf()
                                cacheMap[newScope.toString()] = it
                                context.functionCache[expression.callee.name] = cacheMap
                            }
                        }
                }
            }
        }
    }

    companion object {
        private val RESERVED_WORDS = listOf("print", "let", "if", "else", "true", "false", "first", "second")
    }
}
