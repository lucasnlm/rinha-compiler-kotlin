package runner

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
            expression.runExpression(
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

    private fun Expression.runExpression(
        scope: MutableMap<String, Any?>,
        root: String?,
        recursiveCall: Int,
        canTailCallOptimize: Boolean = false,
    ): Any? {
        return when (this) {
            is Expression.Var -> getVariableValue(scope)
            is Expression.IntValue -> getIntValue()
            is Expression.BoolValue -> getBoolValue()
            is Expression.StrValue -> getStringValue()
            is Expression.Let -> defineExpression(scope, root, recursiveCall)
            is Expression.Binary -> processOperation(scope, root, recursiveCall, canTailCallOptimize)
            is Expression.If -> ifExpression(scope, root, recursiveCall, canTailCallOptimize)
            is Expression.Function -> defineFunction(scope)
            is Expression.Call -> callFunction(scope, root, recursiveCall, canTailCallOptimize)
            is Expression.TupleValue -> createTuple(scope, root, recursiveCall)
            is Expression.First -> first(scope, root, recursiveCall)
            is Expression.Second -> second(scope, root, recursiveCall)
            is Expression.Print -> print(scope, root, recursiveCall)
        }
    }

    private fun Expression.Var.getVariableValue(
        scope: Map<String, Any?>,
    ): Any {
        return scope[name] ?: context.variables[name] ?: throw RuntimeException("variable '${name}' is not defined")
    }

    private fun Expression.Binary.processOperation(
        scope: MutableMap<String, Any?>,
        root: String?,
        recursiveCall: Int,
        canTailCallOptimize: Boolean,
    ): Any {
        return when (this.operator) {
            BinaryOperator.Add -> {
                mathAddExpression(
                    expression = this,
                    scope = scope,
                    root = root,
                    recursiveCall = recursiveCall,
                    canTailCallOptimize = canTailCallOptimize,
                )
            }
            BinaryOperator.Sub -> {
                mathSubExpression(
                    expression = this,
                    scope = scope,
                    root = root,
                    recursiveCall = recursiveCall,
                    canTailCallOptimize = canTailCallOptimize,
                )
            }
            BinaryOperator.Mul -> mathMulExpression(
                expression = this,
                scope = scope,
                root = root,
                recursiveCall = recursiveCall,
                canTailCallOptimize = canTailCallOptimize,
            )
            BinaryOperator.Div -> mathDivExpression(
                expression = this,
                scope = scope,
                root = root,
                recursiveCall = recursiveCall,
                canTailCallOptimize = canTailCallOptimize,
            )
            BinaryOperator.Rem -> mathRemExpression(
                expression = this,
                scope = scope,
                root = root,
                recursiveCall = recursiveCall,
                canTailCallOptimize = canTailCallOptimize,
            )
            BinaryOperator.Eq -> {
                comparativeExpression(
                    expression = this,
                    scope = scope,
                    root = root,
                    recursiveCall = recursiveCall,
                    canTailCallOptimize = canTailCallOptimize,
                ) { left, right -> left == right }
            }
            BinaryOperator.Neq -> {
                comparativeExpression(
                    expression = this,
                    scope = scope,
                    root = root,
                    recursiveCall = recursiveCall,
                    canTailCallOptimize = canTailCallOptimize,
                ) { left, right -> left != right }
            }
            BinaryOperator.Or -> {
                logicOrExpression(
                    expression = this,
                    scope = scope,
                    root = root,
                    recursiveCall = recursiveCall,
                    canTailCallOptimize = canTailCallOptimize,
                )
            }
            BinaryOperator.And -> {
                logicAndExpression(
                    expression = this,
                    scope = scope,
                    root = root,
                    recursiveCall = recursiveCall,
                    canTailCallOptimize = canTailCallOptimize,
                )
            }
            BinaryOperator.Lt -> {
                comparativeMathExpression(
                    expression = this,
                    scope = scope,
                    root = root,
                    recursiveCall = recursiveCall,
                    canTailCallOptimize = canTailCallOptimize,
                ) { left, right -> left < right }
            }
            BinaryOperator.Lte -> {
                comparativeMathExpression(
                    expression = this,
                    scope = scope,
                    root = root,
                    recursiveCall = recursiveCall,
                    canTailCallOptimize = canTailCallOptimize,
                ) { left, right -> left <= right }
            }
            BinaryOperator.Gt -> {
                comparativeMathExpression(
                    expression = this,
                    scope = scope,
                    root = root,
                    recursiveCall = recursiveCall,
                    canTailCallOptimize = canTailCallOptimize,
                ) { left, right -> left > right }
            }
            BinaryOperator.Gte -> {
                comparativeMathExpression(
                    expression = this,
                    scope = scope,
                    root = root,
                    recursiveCall = recursiveCall,
                    canTailCallOptimize = canTailCallOptimize,
                ) { left, right -> left >= right }
            }
        }
    }

    private fun comparativeMathExpression(
        expression: Expression.Binary,
        scope: MutableMap<String, Any?>,
        root: String?,
        recursiveCall: Int,
        canTailCallOptimize: Boolean,
        block: (left: Int, right: Int) -> Boolean,
    ): Boolean {
        val left = expression.left.runExpression(scope, root, recursiveCall, canTailCallOptimize) as? Int
        val right = expression.right.runExpression(scope, root, recursiveCall, canTailCallOptimize) as? Int

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
        canTailCallOptimize: Boolean,
        block: (left: Any?, right: Any?) -> Boolean,
    ): Boolean {
        val left = expression.left.runExpression(scope, root, recursiveCall, canTailCallOptimize)
        val right = expression.right.runExpression(scope, root, recursiveCall, canTailCallOptimize)
        return block(left, right)
    }

    private fun mathRemExpression(
        expression: Expression.Binary,
        scope: MutableMap<String, Any?>,
        root: String?,
        recursiveCall: Int,
        canTailCallOptimize: Boolean,
    ): Any {
        val left = expression.left.runExpression(scope, root, recursiveCall, canTailCallOptimize)
        val right = expression.right.runExpression(scope, root, recursiveCall, canTailCallOptimize)
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
        canTailCallOptimize: Boolean,
    ): Any {
        val left = expression.left.runExpression(scope, root, recursiveCall, canTailCallOptimize)

        return if (left == 0 || left == 0.0) {
            0
        } else {
            val right = expression.right.runExpression(scope, root, recursiveCall)
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
        canTailCallOptimize: Boolean,
    ): Any {
        val left = expression.left.runExpression(scope, root, recursiveCall, canTailCallOptimize)

        return if (left == 0 || left == 0.0) {
            0
        } else {
            val right = expression.right.runExpression(scope, root, recursiveCall)
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
        canTailCallOptimize: Boolean,
    ): Any {
        val left = expression.left.runExpression(scope, root, recursiveCall, canTailCallOptimize)
        val right = expression.right.runExpression(scope, root, recursiveCall, canTailCallOptimize)

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
        canTailCallOptimize: Boolean,
    ): Any {
        val left = expression.left.runExpression(scope, root, recursiveCall, canTailCallOptimize)
        val right = expression.right.runExpression(scope, root, recursiveCall, canTailCallOptimize)

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

    private fun logicOrExpression(
        expression: Expression.Binary,
        scope: MutableMap<String, Any?>,
        root: String?,
        recursiveCall: Int,
        canTailCallOptimize: Boolean,
    ): Boolean {
        val left = expression.left.runExpression(scope, root, recursiveCall, canTailCallOptimize)
        val leftPart = ((left is Boolean && left) || (left is Int && left != 0))
        if (leftPart) {
            return true
        }

        val right = expression.right.runExpression(scope, root, recursiveCall, canTailCallOptimize)
        return ((right is Boolean && right) || (right is Int && right != 0))
    }

    private fun logicAndExpression(
        expression: Expression.Binary,
        scope: MutableMap<String, Any?>,
        root: String?,
        recursiveCall: Int,
        canTailCallOptimize: Boolean,
    ): Boolean {
        val left = expression.left.runExpression(scope, root, recursiveCall, canTailCallOptimize)
        val leftPart = ((left is Boolean && left) || (left is Int && left != 0))
        if (!leftPart) {
            return false
        }

        val right = expression.right.runExpression(scope, root, recursiveCall, canTailCallOptimize)
        return ((right is Boolean && right) || (right is Int && right != 0))
    }

    private fun Expression.First.first(
        scope: MutableMap<String, Any?>,
        root: String?,
        recursiveCall: Int,
    ): Any? {
        return if (this.value.size > 1) {
            throw RuntimeException("'first' function can only one argument")
        } else {
            val value = this.value.first()
            if (value is Expression.TupleValue) {
                value.first.runExpression(scope, root, recursiveCall)
            } else {
                when (val valueExpr = value.runExpression(scope, root, recursiveCall)) {
                    is Pair<*, *> -> valueExpr.first
                    else -> throw RuntimeException("'first' function can only handle Tuples")
                }
            }
        }
    }

    private fun Expression.Second.second(
        scope: MutableMap<String, Any?>,
        root: String?,
        recursiveCall: Int,
    ): Any? {
        return if (this.value.size > 1) {
            throw RuntimeException("'second' function can only one argument")
        } else {
            val value = this.value.first()
            if (value is Expression.TupleValue) {
                value.second.runExpression(scope, root, recursiveCall)
            } else {
                when (val valueExpr = value.runExpression(scope, root, recursiveCall)) {
                    is Pair<*, *> -> valueExpr.second
                    else -> throw RuntimeException("'second' function can only handle Tuples")
                }
            }
        }
    }

    private fun Expression.If.ifExpression(
        scope: MutableMap<String, Any?>,
        root: String?,
        recursiveCall: Int,
        canTailCallOptimize: Boolean,
    ): Any? {
        val condition = this.condition.runExpression(
            scope = scope,
            root = root,
            recursiveCall = recursiveCall,
        )
        return if (condition == true) {
            this.then.fold<Expression, Any?>(null) { _, functionExpression ->
                functionExpression.runExpression(
                    scope = scope,
                    root = root,
                    recursiveCall = recursiveCall,
                    canTailCallOptimize = canTailCallOptimize,
                )
            }
        } else {
            this.otherwise?.fold<Expression, Any?>(null) { _, functionExpression ->
                functionExpression.runExpression(
                    scope = scope,
                    root = root,
                    recursiveCall = recursiveCall,
                    canTailCallOptimize = canTailCallOptimize,
                )
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
        scope: MutableMap<String, Any?>,
        root: String?,
        recursiveCall: Int,
    ): Any {
        val first = this.first.runExpression(scope, root, recursiveCall)
        val second = this.second.runExpression(scope, root, recursiveCall)
        return first to second
    }

    private fun Expression.Let.defineExpression(
        scope: MutableMap<String, Any?>,
        root: String?,
        recursiveCall: Int,
    ): Any? {
        val value = this.value.runExpression(
            scope = scope,
            root = root,
            recursiveCall = recursiveCall,
        )
        val sanitizedName = this.name.replace("_", "")
        if (sanitizedName.isNotBlank()) {
            if (RESERVED_WORDS.contains(sanitizedName)) {
                throw RuntimeException("can't use reserved word '$sanitizedName' as variable name")
            }
            scope[this.name] = value
        }
        return value
    }

    private fun Expression.Function.defineFunction(
        scope: Map<String, Any?>,
    ): Any {
        // Function will be executed only when called.
        return copy(scopeCopy = scope + context.variables)
    }

    private fun Expression.Print.print(
        scope: MutableMap<String, Any?>,
        root: String?,
        recursiveCall: Int,
    ): Any? {
        val result = if (this.value.size == 1) {
            this.value.first().runExpression(
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

    private fun Expression.Call.callFunction(
        scope: MutableMap<String, Any?>,
        root: String?,
        recursiveCall: Int = 0,
        canTailCallOptimize: Boolean,
    ): Any? {
        val callerName = this.callee.name
        return when (val target = context.variables[callerName]) {
            null -> {
                throw RuntimeException("function '${callerName}' is not defined")
            }
            !is Expression.Function -> {
                throw RuntimeException("'${callerName}' is not a function")
            }
            (target.parameters.size != this.arguments.size) -> {
                throw RuntimeException("missing param at '${callerName}' call")
            }
            else -> {
                // Check for some predefined optimizations
                if (context.runtimeOptimization) {
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
                val recursiveCallDepth = if (root == callerName) {
                    // Recursive call detected
                    recursiveCall.inc().also {
                        if (it > 900) { throw RuntimeException("recursive call limit exceeded") }
                    }
                } else {
                    1
                }

                val functionScope = (scope + target.scopeCopy).toMutableMap()
                val resolvedArguments = target.parameters.mapIndexed { index: Int, param: String ->
                    param to this.arguments[index].runExpression(
                        scope = functionScope,
                        root = callerName,
                        recursiveCall = recursiveCallDepth,
                    )
                }
                var newScope = target.scopeCopy.toMutableMap().apply { putAll(resolvedArguments) }

                if (canTailCallOptimize) {
                    return Accumulator(newScope)
                }

                val cached = if (context.cacheEnabled && !canTailCallOptimize) {
                    context.functionCache[callerName]?.get(newScope.toString())
                } else {
                    null
                }

                val isTailCallOptimizable = checkTailRecursive(this, target.value)
                var itResult: Any? = null
                var accValue: Accumulator? = null

                do {
                    if (itResult is Accumulator) {
                        newScope = itResult.scope.toMutableMap()
                        accValue = accValue?.resolve(itResult) ?: itResult
                    }

                    itResult = cached ?: target
                        .value
                        .fold<Expression, Any?>(null) { _, functionExpression ->
                            functionExpression.runExpression(
                                scope = newScope,
                                root = callerName,
                                recursiveCall = recursiveCallDepth,
                                canTailCallOptimize = isTailCallOptimizable,
                            )
                        }.also {
                            if (context.cacheEnabled && !isTailCallOptimizable) {
                                val cacheMap = context.functionCache[callerName] ?: mutableMapOf()
                                cacheMap[newScope.toString()] = it
                                context.functionCache[callerName] = cacheMap
                            }
                        }
                } while (itResult is Accumulator)

                if (accValue != null) {
                    when (itResult) {
                        is Int -> {
                            itResult + accValue.asInt()
                        }
                        is String -> {
                            accValue.asString() + itResult
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
            count += this.then.sumOf {
                it.findCalls(callerName)
            }
            if (count > 1) {
                return count
            }
            count += this.otherwise?.sumOf {
                it.findCalls(callerName)
            } ?: 0
        }
        return count
    }

    companion object {
        private val RESERVED_WORDS = listOf("print", "let", "if", "else", "true", "false", "first", "second")
    }
}
