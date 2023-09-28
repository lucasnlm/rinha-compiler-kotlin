package runner

import Output
import com.github.h0tk3y.betterParse.parser.ParseException
import expressions.BinaryOperator
import expressions.Expression
import parser.RinhaGrammar
import platform.posix.NAN

class ExpressionRunner(
    val runtimeContext: RunTimeContext = RunTimeContext(),
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
            val message = if (it is ParseException) {
                "syntax error"
            } else {
                it.message
            }
            Output.error(message)
        }.onSuccess { expressions ->
            doubleTryRun(expressions)?.also { response ->
                val last = runtimeContext.output.lastOrNull()
                if (last == null || last != response.toString()) {
                    Output.print(response.toString())
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
    fun runFromExpressions(expressions: List<Expression>): Any? {
        if (expressions.isEmpty()) {
            throw RuntimeException("no expressions to run")
        }

        return doubleTryRun(expressions)
    }

    /**
     * Clear the output.
     */
    fun clearOutput() {
        runtimeContext.output.clear()
    }

    /**
     * Print the global scope.
     */
    fun printGlobalScope() {
        if (runtimeContext.variables.isEmpty()) {
            "{}"
        } else {
            runtimeContext.variables.map {
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
            }
        }.also {
            if (!runtimeContext.isTesting && it.isNotBlank()) {
                Output.print(it)
            }
        }
    }

    /**
     * Try with optimization. If it fails, try without optimization. :P
     * @param expressions The expressions to run.
     * @return The result of the last expression.
     */
    private fun doubleTryRun(expressions: List<Expression>): Any? {
        var tryAgain = false
        var result: Any? = null

        val context = FunctionContext(
            scope = runtimeContext.variables,
            root = null,
            recursiveCall = 0,
            runtimeOptimization = runtimeContext.runtimeOptimization,
        )

        runCatching {
            result = expressions.runExpressionsWith(context)
        }.onFailure {
            if (runtimeContext.output.isEmpty() && runtimeContext.fallbackOptimization) {
                tryAgain = true
            } else {
                throw it
            }
        }

        if (tryAgain && runtimeContext.fallbackOptimization) {
            result = expressions.runExpressionsWith(context.copy(runtimeOptimization = false))
        }

        return result
    }

    private fun List<Expression>.runExpressionsWith(
        context: FunctionContext,
    ): Any? {
        return fold<Expression, Any?>(null) { _, expression ->
            expression.runExpression(context)
        }
    }

    private fun Expression.runExpression(
        context: FunctionContext,
    ): Any? {
        return when (this) {
            is Expression.Var -> getVariableValue(context)
            is Expression.IntValue -> getIntValue()
            is Expression.BoolValue -> getBoolValue()
            is Expression.StrValue -> getStringValue()
            is Expression.Let -> defineExpression(context)
            is Expression.Binary -> processOperation(context)
            is Expression.If -> ifExpression(context)
            is Expression.Function -> defineFunction(context)
            is Expression.Call -> callFunction(context)
            is Expression.TupleValue -> createTuple(context)
            is Expression.First -> first(context)
            is Expression.Second -> second(context)
            is Expression.Print -> printExpr(context)
        }
    }

    private fun Expression.Var.getVariableValue(
        context: FunctionContext,
    ): Any {
        return context.scope[name] ?: runtimeContext.variables[name] ?: throw RuntimeException("variable '$name' is not defined")
    }

    private fun Expression.Binary.processOperation(
        context: FunctionContext,
    ): Any {
        return when (this.operator) {
            BinaryOperator.Add -> mathAddExpression(context)
            BinaryOperator.Sub -> mathSubExpression(context)
            BinaryOperator.Mul -> mathMulExpression(context)
            BinaryOperator.Div -> mathDivExpression(context)
            BinaryOperator.Rem -> mathRemExpression(context)
            BinaryOperator.Eq -> comparativeExpression(context) { left, right -> left == right }
            BinaryOperator.Neq -> comparativeExpression(context) { left, right -> left != right }
            BinaryOperator.Or -> logicOrExpression(context)
            BinaryOperator.And -> logicAndExpression(context)
            BinaryOperator.Lt -> comparativeMathExpression(context) { left, right -> left < right }
            BinaryOperator.Lte -> comparativeMathExpression(context) { left, right -> left <= right }
            BinaryOperator.Gt -> comparativeMathExpression(context) { left, right -> left > right }
            BinaryOperator.Gte -> comparativeMathExpression(context) { left, right -> left >= right }
        }
    }

    private fun Expression.Binary.comparativeMathExpression(
        context: FunctionContext,
        block: (left: Int, right: Int) -> Boolean,
    ): Boolean {
        val left = left.runExpression(context) as? Int
        val right = right.runExpression(context) as? Int

        if (left == null) {
            throw RuntimeException("left side of the expression is null")
        }
        if (right == null) {
            throw RuntimeException("right side of the expression is null")
        }

        return block(left, right)
    }

    private fun Expression.Binary.comparativeExpression(
        context: FunctionContext,
        block: (left: Any?, right: Any?) -> Boolean,
    ): Boolean {
        val left = left.runExpression(context)
        val right = right.runExpression(context)
        return block(left, right)
    }

    private fun Expression.Binary.mathRemExpression(
        context: FunctionContext,
    ): Any {
        val left = left.runExpression(context)
        val right = right.runExpression(context)
        return if (left is Int && right is Int) {
            left.rem(right)
        } else {
            throw RuntimeException("invalid rem expression")
        }
    }

    private fun Expression.Binary.mathDivExpression(
        context: FunctionContext,
    ): Any {
        val left = left.runExpression(context)

        return if (left == 0 || left == 0.0) {
            0
        } else {
            val right = right.runExpression(context)
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

    private fun Expression.Binary.mathMulExpression(
        context: FunctionContext,
    ): Any {
        val left = left.runExpression(context)

        return if (left == 0 || left == 0.0) {
            0
        } else {
            val right = right.runExpression(context.copy(tailCall = null))
            if (left is Int && right is Int) {
                left * right
            } else {
                throw RuntimeException("invalid multiplication expression")
            }
        }
    }

    private fun Expression.Binary.mathSubExpression(
        context: FunctionContext,
    ): Any {
        val left = left.runExpression(context)
        val right = right.runExpression(context)

        return if (left is Int && right is Int) {
            left - right
        } else if (left is Accumulator && right is Int) {
            left.apply {
                value = left.asInt() - right
            }
        } else if (right is Accumulator && left is Int) {
            right.apply {
                sign = -1
                value = left + right.asInt()
            }
        } else {
            throw RuntimeException("invalid sub expression")
        }
    }

    private fun Expression.Binary.mathAddExpression(
        context: FunctionContext,
    ): Any {
        val left = left.runExpression(context)
        val right = right.runExpression(context)

        return if (left is Int && right is Int) {
            left + right
        } else if (left is String && right is Accumulator) {
            right.apply {
                value = left + right.asString()
            }
        } else if (left is Accumulator && right is String) {
            left.apply {
                value = left.asString() + right
            }
        } else if (right is Accumulator && left is String) {
            left + right.asString()
        } else if (left is String || right is String) {
            left.toString() + right.toString()
        } else if (left is String) {
            left + right.toString()
        } else if (right is Accumulator && left is Int) {
            right.apply {
                value = asInt() + left
            }
        } else if (left is Accumulator && right is Int) {
            left.apply {
                value = asInt() + right
            }
        } else {
            throw RuntimeException("invalid add expression")
        }
    }

    private fun Expression.Binary.logicOrExpression(
        context: FunctionContext,
    ): Boolean {
        val left = left.runExpression(context)
        val leftPart = ((left is Boolean && left) || (left is Int && left != 0))
        if (leftPart) {
            return true
        }

        val right = right.runExpression(context)
        return ((right is Boolean && right) || (right is Int && right != 0))
    }

    private fun Expression.Binary.logicAndExpression(
        context: FunctionContext,
    ): Boolean {
        val left = left.runExpression(context)
        val leftPart = ((left is Boolean && left) || (left is Int && left != 0))
        if (!leftPart) {
            return false
        }

        val right = right.runExpression(context)
        return ((right is Boolean && right) || (right is Int && right != 0))
    }

    private fun Expression.First.first(
        context: FunctionContext,
    ): Any? {
        return if (this.value.size > 1) {
            throw RuntimeException("'first' function can only one argument")
        } else {
            val value = this.value.first()
            if (value is Expression.TupleValue) {
                value.first.runExpression(context)
            } else {
                when (val valueExpr = value.runExpression(context)) {
                    is Pair<*, *> -> valueExpr.first
                    else -> throw RuntimeException("'first' function can only handle Tuples")
                }
            }
        }
    }

    private fun Expression.Second.second(
        context: FunctionContext,
    ): Any? {
        return if (this.value.size > 1) {
            throw RuntimeException("'second' function can only one argument")
        } else {
            val value = this.value.first()
            if (value is Expression.TupleValue) {
                value.second.runExpression(context)
            } else {
                when (val valueExpr = value.runExpression(context)) {
                    is Pair<*, *> -> valueExpr.second
                    else -> throw RuntimeException("'second' function can only handle Tuples")
                }
            }
        }
    }

    private fun Expression.If.ifExpression(
        context: FunctionContext,
    ): Any? {
        val result = condition.runExpression(context)
        return if (result == true) {
            then.fold<Expression, Any?>(null) { _, expression ->
                expression.runExpression(context)
            }
        } else {
            otherwise.fold<Expression, Any?>(null) { _, expression ->
                expression.runExpression(context)
            }
        }
    }

    private fun Expression.StrValue.getStringValue(): String {
        return value
    }

    private fun Expression.IntValue.getIntValue(): Int {
        return value
    }

    private fun Expression.BoolValue.getBoolValue(): Boolean {
        return value
    }

    private fun Expression.TupleValue.createTuple(
        context: FunctionContext,
    ): Any {
        val first = first.runExpression(context)
        val second = second.runExpression(context)
        return first to second
    }

    private fun Expression.Let.defineExpression(
        context: FunctionContext,
    ): Any? {
        val value = value.runExpression(context)
        val sanitizedName = name.replace("_", "")
        if (sanitizedName.isNotBlank()) {
            if (RESERVED_WORDS.contains(sanitizedName)) {
                throw RuntimeException("can't use reserved word '$sanitizedName' as variable name")
            }
            context.scope[name] = value
        }
        return value
    }

    private fun Expression.Function.defineFunction(
        context: FunctionContext,
    ): Any {
        // Function will be executed only when called.
        return copy(
            scopeCopy = mutableMapOf<String, Any?>().apply {
                putAll(context.scope)
                putAll(runtimeContext.variables)
            },
        )
    }

    private fun Expression.Print.printExpr(
        context: FunctionContext,
    ): Any? {
        val result = if (value.size == 1) {
            value.first().runExpression(context).also {
                val asString = it.toString()
                runtimeContext.output.add(asString)
                Output.print(asString)
            }
        } else {
            throw RuntimeException("'print' function can handle only one argument")
        }

        runtimeContext.output.run {
            if (size > runtimeContext.maxOutputSize.coerceAtLeast(10)) {
                // Clean the output if it's too big
                clear()
            }
        }

        return result
    }

    private fun Expression.Call.callFunction(
        context: FunctionContext,
    ): Any? {
        val target = callee.runExpression(context)
        val callerName = if (callee is Expression.Var) {
            callee.name
        } else {
            target.hashCode().toString()
        }
        return when (target) {
            null -> {
                throw RuntimeException("function '$callerName' is not defined")
            }
            !is Expression.Function -> {
                throw RuntimeException("'$callerName' is not a function")
            }
            (target.parameters.size != this.arguments.size) -> {
                throw RuntimeException("missing param at '$callerName' call")
            }
            target.isInline() -> {
                target.value.first().runExpression(context)
            }
            else -> {
                // Check for some predefined optimizations
                if (context.runtimeOptimization && context.recursiveCall < 2) {
                    RunTimeOptimizations.checkRunTimeOptimizations(
                        exprCall = this,
                        expressions = target.value,
                    ).also {
                        if (it != null) {
                            return it
                        }
                    }
                }

                // Check for recursive calls
                val recursiveCallDepth = if (context.root == callerName) {
                    // Recursive call detected
                    context.recursiveCall.inc().also {
                        if (it > 900) { throw RuntimeException("recursive call limit exceeded") }
                    }
                } else {
                    1
                }

                val functionScope = (context.scope + target.scopeCopy).toMutableMap()
                val newFunctionContext = context.copy(
                    scope = functionScope,
                    root = callerName,
                    recursiveCall = recursiveCallDepth,
                )
                val resolvedArguments = target.parameters.mapIndexed { index: Int, param: String ->
                    param to arguments.getOrNull(index)?.runExpression(newFunctionContext)
                }
                var newScope = target.scopeCopy.toMutableMap().apply { putAll(resolvedArguments) }

                if (context.tailCall == callerName && context.runtimeOptimization && context.root == callerName) {
                    return Accumulator(newScope)
                }

                val tailCallOptimizable = checkTailRecursive(callerName, target.value)
                var itResult: Any? = null
                var accumulator: Accumulator? = null

                do {
                    if (itResult is Accumulator) {
                        newScope = itResult.scope.toMutableMap()
                        accumulator = accumulator?.resolve(itResult) ?: itResult
                    }

                    val iterationContext = newFunctionContext.copy(
                        scope = newScope,
                        tailCall = newFunctionContext.tailCall ?: tailCallOptimizable,
                    )

                    itResult = target
                        .value
                        .fold<Expression, Any?>(null) { _, functionExpression ->
                            functionExpression.runExpression(iterationContext)
                        }
                } while (itResult is Accumulator)

                if (accumulator != null) {
                    when (itResult) {
                        is Int -> {
                            (itResult * accumulator.sign) + accumulator.asInt()
                        }
                        is String -> {
                            accumulator.asString() + itResult
                        }
                        else -> {
                            itResult
                        }
                    }
                } else {
                    itResult
                }
            }
        }
    }

    private fun checkTailRecursive(
        callerName: String,
        expressions: List<Expression>,
    ): String? {
        var result = false

        if (expressions.isNotEmpty()) {
            var count = 0
            for (expr in expressions) {
                count += expr.findCalls(callerName)
                if (count > 1) {
                    break
                }
            }
            result = (count == 1)
        }

        return if (result) callerName else null
    }

    private fun Expression.findCalls(
        callerName: String,
    ): Int {
        var count = 0

        if (this is Expression.Call && callee is Expression.Var && callee.name == callerName) {
            count++
        } else if (this is Expression.Binary) {
            count += left.findCalls(callerName)
            if (count > 1) {
                return count
            }
            count += right.findCalls(callerName)
        } else if (this is Expression.If) {
            count += then.sumOf {
                it.findCalls(callerName)
            }
            if (count > 1) {
                return count
            }
            count += otherwise.sumOf {
                it.findCalls(callerName)
            }
        }
        return count
    }

    companion object {
        private val RESERVED_WORDS = listOf("print", "let", "if", "else", "true", "false", "first", "second")
    }
}
