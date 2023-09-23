package parser

import expressions.BinaryOperator
import expressions.Expression
import expressions.ExpressionLocation
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import parser.ExpressionJsonIds.EXPRESSION_BINARY_ID
import parser.ExpressionJsonIds.EXPRESSION_BINARY_LEFT
import parser.ExpressionJsonIds.EXPRESSION_BINARY_OPERATOR
import parser.ExpressionJsonIds.EXPRESSION_BINARY_RIGHT
import parser.ExpressionJsonIds.EXPRESSION_BOOL_ID
import parser.ExpressionJsonIds.EXPRESSION_BOOL_VALUE
import parser.ExpressionJsonIds.EXPRESSION_CALL_ARGUMENTS
import parser.ExpressionJsonIds.EXPRESSION_CALL_CALLEE
import parser.ExpressionJsonIds.EXPRESSION_CALL_ID
import parser.ExpressionJsonIds.EXPRESSION_FIRST_ID
import parser.ExpressionJsonIds.EXPRESSION_FIRST_VALUE
import parser.ExpressionJsonIds.EXPRESSION_FUNCTION_ID
import parser.ExpressionJsonIds.EXPRESSION_FUNCTION_PARAMS
import parser.ExpressionJsonIds.EXPRESSION_FUNCTION_PARAM_NAME
import parser.ExpressionJsonIds.EXPRESSION_FUNCTION_VALUE
import parser.ExpressionJsonIds.EXPRESSION_IF_CONDITION
import parser.ExpressionJsonIds.EXPRESSION_IF_ID
import parser.ExpressionJsonIds.EXPRESSION_IF_OTHERWISE
import parser.ExpressionJsonIds.EXPRESSION_IF_THEN
import parser.ExpressionJsonIds.EXPRESSION_INT_ID
import parser.ExpressionJsonIds.EXPRESSION_INT_VALUE
import parser.ExpressionJsonIds.EXPRESSION_KIND
import parser.ExpressionJsonIds.EXPRESSION_LET_ID
import parser.ExpressionJsonIds.EXPRESSION_LET_TEXT
import parser.ExpressionJsonIds.EXPRESSION_LET_VALUE
import parser.ExpressionJsonIds.EXPRESSION_LOCATION
import parser.ExpressionJsonIds.EXPRESSION_NAME
import parser.ExpressionJsonIds.EXPRESSION_NEXT
import parser.ExpressionJsonIds.EXPRESSION_PRINT_ID
import parser.ExpressionJsonIds.EXPRESSION_PRINT_VALUE
import parser.ExpressionJsonIds.EXPRESSION_ROOT_NEXT
import parser.ExpressionJsonIds.EXPRESSION_SECOND_ID
import parser.ExpressionJsonIds.EXPRESSION_SECOND_VALUE
import parser.ExpressionJsonIds.EXPRESSION_STR_ID
import parser.ExpressionJsonIds.EXPRESSION_STR_VALUE
import parser.ExpressionJsonIds.EXPRESSION_TUPLE_FIRST
import parser.ExpressionJsonIds.EXPRESSION_TUPLE_ID
import parser.ExpressionJsonIds.EXPRESSION_TUPLE_SECOND
import parser.ExpressionJsonIds.EXPRESSION_VAR_ID
import parser.ExpressionJsonIds.EXPRESSION_VAR_TEXT
import parser.ExpressionJsonIds.LOCATION_END
import parser.ExpressionJsonIds.LOCATION_FILENAME
import parser.ExpressionJsonIds.LOCATION_START
import parser.JsonObjectExt.array
import parser.JsonObjectExt.obj
import parser.JsonObjectExt.value

object AstParser {
    /**
     * Parses the AST from the given file path Json.
     * @param fileContent The file content to parse.
     * @return The [Result] with the list of [Expression].
     */
    fun parseAstFile(
        fileContent: JsonObject,
    ): Result<List<Expression>> {
        val parsedExpressions = parseExpressions(fileContent).onFailure {
            return Result.failure(IllegalArgumentException("syntax error. $it"))
        }.getOrThrow()

        return Result.success(parsedExpressions)
    }

    /**
     * List of parsers for each expression kind.
     */
    private val expressionParserMap by lazy {
        mapOf(
            EXPRESSION_LET_ID to ::parseLetExpression,
            EXPRESSION_FUNCTION_ID to ::parseFunctionExpression,
            EXPRESSION_IF_ID to ::parseIfExpression,
            EXPRESSION_BINARY_ID to ::parseBinaryExpression,
            EXPRESSION_VAR_ID to ::parseVarExpression,
            EXPRESSION_INT_ID to ::parseIntExpression,
            EXPRESSION_STR_ID to ::parseStrExpression,
            EXPRESSION_TUPLE_ID to ::parseTupleExpression,
            EXPRESSION_FIRST_ID to ::parseFirstExpression,
            EXPRESSION_SECOND_ID to ::parseSecondExpression,
            EXPRESSION_BOOL_ID to ::parseBoolExpression,
            EXPRESSION_CALL_ID to ::parseCallExpression,
            EXPRESSION_PRINT_ID to ::parsePrintExpression,
        )
    }

    private fun JsonObject.tryGetLocation(): ExpressionLocation? {
        return runCatching {
            readExpressionLocation()
        }.getOrNull()
    }

    private fun parseTupleExpression(json: JsonObject): Expression.TupleValue = with(json) {
        val first = obj(EXPRESSION_TUPLE_FIRST)?.parseExpression()
            ?: throw AstParseException("invalid first at Tuple", tryGetLocation())
        val second = obj(EXPRESSION_TUPLE_SECOND)?.parseExpression()
            ?: throw AstParseException("invalid second at Tuple", tryGetLocation())

        return Expression.TupleValue(
            first = first,
            second = second,
        )
    }

    private fun parseFirstExpression(json: JsonObject): Expression.First = with(json) {
        val value = obj(EXPRESSION_FIRST_VALUE)?.parseExpression()
            ?: throw AstParseException("invalid value at First", tryGetLocation())

        return Expression.First(
            value = listOf(value),
        )
    }

    private fun parseSecondExpression(json: JsonObject): Expression.Second = with(json) {
        val value = obj(EXPRESSION_SECOND_VALUE)?.parseExpression()
            ?: throw AstParseException("invalid value at Second", tryGetLocation())

        return Expression.Second(
            value = listOf(value),
        )
    }

    private fun parseBoolExpression(json: JsonObject): Expression.BoolValue = with(json) {
        val value = value(EXPRESSION_BOOL_VALUE)
            ?: throw AstParseException("invalid value at Bool", tryGetLocation())

        return Expression.BoolValue(
            value = value.toBoolean(),
        )
    }

    private fun parseStrExpression(json: JsonObject): Expression.StrValue = with(json) {
        val value = value(EXPRESSION_STR_VALUE)
            ?: throw AstParseException("invalid value at Str", tryGetLocation())

        return Expression.StrValue(
            value = value,
        )
    }

    private fun parsePrintExpression(json: JsonObject): Expression.Print = with(json) {
        val value = obj(EXPRESSION_PRINT_VALUE)?.parseExpression()
            ?: throw AstParseException("invalid value at Print", tryGetLocation())

        return Expression.Print(
            value = listOf(value),
        )
    }

    private fun parseCallExpression(json: JsonObject): Expression.Call = with(json) {
        val calleeObj = obj(EXPRESSION_CALL_CALLEE)
            ?: throw AstParseException("invalid callee at Call", tryGetLocation())
        val callee = parseVarExpression(calleeObj)
        val arguments = array(EXPRESSION_CALL_ARGUMENTS)?.mapNotNull {
            it.jsonObject.parseExpression()
        } ?: throw AstParseException("invalid arguments at Call", tryGetLocation())

        return Expression.Call(
            callee = callee,
            arguments = arguments,
        )
    }

    private fun parseIntExpression(json: JsonObject): Expression.IntValue = with(json) {
        val value = value(EXPRESSION_INT_VALUE)
            ?: throw AstParseException("invalid value at Int", tryGetLocation())

        return Expression.IntValue(
            value = value.toInt(),
        )
    }

    private fun parseVarExpression(json: JsonObject): Expression.Var = with(json) {
        val name = value(EXPRESSION_VAR_TEXT)
            ?: throw AstParseException("invalid name at Var", tryGetLocation())

        return Expression.Var(
            name = name,
        )
    }

    private fun parseBinaryExpression(json: JsonObject): Expression.Binary = with(json) {
        val left = obj(EXPRESSION_BINARY_LEFT)?.parseExpression()
            ?: throw AstParseException("invalid left at Binary", tryGetLocation())
        val right = obj(EXPRESSION_BINARY_RIGHT)?.parseExpression()
            ?: throw AstParseException("invalid right at Binary", tryGetLocation())
        val operator = value(EXPRESSION_BINARY_OPERATOR)
            ?: throw AstParseException("invalid operator at Binary", tryGetLocation())

        if (BinaryOperator.entries.find { it.name == operator } == null) {
            throw AstParseException("invalid operator '$operator' at Binary", tryGetLocation())
        }

        return Expression.Binary(
            left = left,
            right = right,
            operator = BinaryOperator.valueOf(operator),
        )
    }

    private fun parseIfExpression(json: JsonObject): Expression.If = with(json) {
        val condition = obj(EXPRESSION_IF_CONDITION)?.parseExpression()
            ?: throw AstParseException("invalid condition at If", tryGetLocation())
        val then = obj(EXPRESSION_IF_THEN)?.parseExpression()
            ?: throw AstParseException("invalid then at If", tryGetLocation())
        val otherwise = obj(EXPRESSION_IF_OTHERWISE)?.parseExpression()

        return Expression.If(
            condition = condition,
            then = listOf(then),
            otherwise = otherwise?.let { listOf(it) },
        )
    }

    private fun parseFunctionExpression(json: JsonObject): Expression.Function = with(json) {
        val name = optName()
        val parameters = array(EXPRESSION_FUNCTION_PARAMS)?.mapNotNull {
            it.jsonObject.value(EXPRESSION_FUNCTION_PARAM_NAME)
        } ?: throw AstParseException("invalid parameters at Function", tryGetLocation())

        var currentFunctionExpr = obj(EXPRESSION_FUNCTION_VALUE)
        val expressions = mutableListOf<Expression>()

        do {
            val expr = currentFunctionExpr?.parseExpression()
                ?: throw AstParseException("invalid value at Function", tryGetLocation())
            expressions.add(expr)
            currentFunctionExpr = currentFunctionExpr.next()
        } while (currentFunctionExpr != null)

        return Expression.Function(
            name = name,
            parameters = parameters,
            value = expressions,
        )
    }

    private fun parseLetExpression(json: JsonObject): Expression.Let = with(json) {
        val name = obj(EXPRESSION_NAME)?.value(EXPRESSION_LET_TEXT)
            ?: throw AstParseException("missing name at Let", tryGetLocation())
        val value = obj(EXPRESSION_LET_VALUE)?.parseExpression()
            ?: throw AstParseException("invalid value at Let", tryGetLocation())

        return Expression.Let(
            name = name,
            value = value,
        )
    }

    private fun JsonObject.readExpressionLocation(): ExpressionLocation {
        val locationObject = get(EXPRESSION_LOCATION)?.jsonObject
            ?: throw AstParseException("missing expression location", tryGetLocation())
        val start = locationObject[LOCATION_START]?.jsonPrimitive?.int
        val end = locationObject[LOCATION_END]?.jsonPrimitive?.int
        val fileName = locationObject[LOCATION_FILENAME]?.jsonPrimitive?.content

        return if (start == null || end == null || fileName == null) {
            throw AstParseException("invalid expression location")
        } else {
            ExpressionLocation(
                start = start,
                end = end,
                fileName = fileName,
            )
        }
    }

    private fun JsonObject.optName(): String? {
        return get(EXPRESSION_NAME)?.jsonPrimitive?.content
    }

    private fun JsonObject.parseExpression(): Expression {
        val kind = get(EXPRESSION_KIND)?.jsonPrimitive?.content ?: throw AstParseException("Missing expression kind", tryGetLocation())
        val parser = expressionParserMap[kind] ?: throw AstParseException("Unexpected expression '$kind'", tryGetLocation())
        return parser(this)
    }

    private fun JsonObject.next(): JsonObject? {
        return (get(EXPRESSION_NEXT) ?: get(EXPRESSION_ROOT_NEXT))?.jsonObject
    }

    private fun parseExpressions(jsonObject: JsonObject): Result<List<Expression>> {
        val expressions = mutableListOf<Expression>()
        var currentExpr = jsonObject.next()

        do {
            val hasNext = currentExpr != null

            currentExpr?.let {
                val currentExpression = it.parseExpression()
                expressions.add(currentExpression)

                currentExpr = it.next()
            }
        } while (hasNext)

        return Result.success(expressions)
    }
}
