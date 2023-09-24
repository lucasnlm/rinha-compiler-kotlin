package runner

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
            if (it is ParseException) {
                println("e: syntax error")
            } else {
                println("e: ${it.message}")
            }
        }.onSuccess { expressions ->
            runFromExpressions(expressions)?.also { response ->
                val last = runtimeContext.output.lastOrNull()
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
    fun runFromExpressions(expressions: List<Expression>): Any? {
        if (expressions.isEmpty()) {
            throw RuntimeException("no expressions to run")
        }

        val context = FunctionContext(
            scope = runtimeContext.variables,
            root = null,
            recursiveCall = 0,
        )

        return expressions.fold<Expression, Any?>(null) { _, expression ->
            expression.runExpression(context)
        }
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
            println("{}")
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
            }.also {
                if (!runtimeContext.isTesting && it.isNotBlank()) {
                    println(it)
                }
            }
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
            val right = right.runExpression(context.copy(canTailCallOptimize = false))
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
        val condition = this.condition.runExpression(context)
        return if (condition == true) {
            then.runExpression(context)
        } else {
            otherwise.runExpression(context)
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
        val first = this.first.runExpression(context)
        val second = this.second.runExpression(context)
        return first to second
    }

    private fun Expression.Let.defineExpression(
        context: FunctionContext,
    ): Any? {
        val value = this.value.runExpression(context)
        val sanitizedName = this.name.replace("_", "")
        if (sanitizedName.isNotBlank()) {
            if (RESERVED_WORDS.contains(sanitizedName)) {
                throw RuntimeException("can't use reserved word '$sanitizedName' as variable name")
            }
            context.scope[this.name] = value
        }
        return value
    }

    private fun Expression.Function.defineFunction(
        context: FunctionContext,
    ): Any {
        // Function will be executed only when called.
        return copy(scopeCopy = context.scope + runtimeContext.variables)
    }

    private fun Expression.Print.printExpr(
        context: FunctionContext,
    ): Any? {
        val result = if (this.value.size == 1) {
            this.value.first().runExpression(context).also {
                val asString = it.toString()
                runtimeContext.output.add(asString)
                println(asString)
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
        val callerName = this.callee.name
        val target = context.scope[callerName] ?: runtimeContext.variables[callerName]
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
            else -> {
                // Check for some predefined optimizations
                if (runtimeContext.runtimeOptimization) {
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
                    param to this.arguments[index].runExpression(newFunctionContext)
                }
                var newScope = target.scopeCopy.toMutableMap().apply { putAll(resolvedArguments) }

                if (context.canTailCallOptimize) {
                    return Accumulator(newScope)
                }

                val cached = if (runtimeContext.cacheEnabled && !context.canTailCallOptimize) {
                    runtimeContext.functionCache[callerName]?.get(newScope.toString())
                } else {
                    null
                }

                val isTailCallOptimizable = checkTailRecursive(this, target.value)
                var itResult: Any? = null
                var accumulator: Accumulator? = null

                do {
                    if (itResult is Accumulator) {
                        newScope = itResult.scope.toMutableMap()
                        accumulator = accumulator?.resolve(itResult) ?: itResult
                    }

                    val iterationContext = newFunctionContext.copy(
                        scope = newScope,
                        canTailCallOptimize = isTailCallOptimizable,
                    )

                    itResult = cached ?: target
                        .value
                        .fold<Expression, Any?>(null) { _, functionExpression ->
                            functionExpression.runExpression(iterationContext)
                        }.also {
                            if (runtimeContext.cacheEnabled && !isTailCallOptimizable) {
                                val cacheMap = runtimeContext.functionCache[callerName] ?: mutableMapOf()
                                cacheMap[newScope.toString()] = it
                                runtimeContext.functionCache[callerName] = cacheMap
                            }
                        }
                } while (itResult is Accumulator)

                if (accumulator != null) {
                    when (itResult) {
                        is Int -> {
                            itResult + accumulator.asInt()
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
        caller: Expression.Call,
        expressions: List<Expression>,
    ): Boolean {
        var result = false

        if (expressions.isNotEmpty()) {
            val callerName = caller.callee.name
            var count = 0
            for (expr in expressions) {
                count += expr.findCalls(callerName)
                if (count > 1) {
                    break
                }
            }
            result = (count == 1)
        }

        return result
    }

    private fun Expression.findCalls(
        callerName: String,
    ): Int {
        var count = 0

        if (this is Expression.Call && callee.name == callerName) {
            count++
        } else if (this is Expression.Binary) {
            count += left.findCalls(callerName)
            if (count > 1) {
                return count
            }
            count += right.findCalls(callerName)
        } else if (this is Expression.If) {
            count += this.then.findCalls(callerName)
            if (count > 1) {
                return count
            }
            count += this.otherwise.findCalls(callerName)
        }
        return count
    }

    companion object {
        private val RESERVED_WORDS = listOf("print", "let", "if", "else", "true", "false", "first", "second")
    }
}
