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
import parser.JsonObjectExt.objOrNull
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
    fun JsonObject.toExpressionOf(kind: String): Expression {
        return when (kind) {
            EXPRESSION_LET_ID -> parseLetExpression()
            EXPRESSION_TUPLE_ID -> parseTupleExpression()
            EXPRESSION_FUNCTION_ID -> parseFunctionExpression()
            EXPRESSION_IF_ID -> parseIfExpression()
            EXPRESSION_BINARY_ID -> parseBinaryExpression()
            EXPRESSION_VAR_ID -> parseVarExpression()
            EXPRESSION_INT_ID -> parseIntExpression()
            EXPRESSION_STR_ID -> parseStrExpression()
            EXPRESSION_FIRST_ID -> parseFirstExpression()
            EXPRESSION_SECOND_ID -> parseSecondExpression()
            EXPRESSION_BOOL_ID -> parseBoolExpression()
            EXPRESSION_CALL_ID -> parseCallExpression()
            EXPRESSION_PRINT_ID -> parsePrintExpression()
            else -> throw AstParseException("unexpected expression '$kind'", tryGetLocation())
        }
    }

    private fun JsonObject.parseExpressionByKind(): Expression {
        val kind = get(EXPRESSION_KIND)?.jsonPrimitive?.content ?: throw AstParseException("missing expression kind", tryGetLocation())
        return toExpressionOf(kind)
    }

    private fun JsonObject.tryGetLocation(): ExpressionLocation? {
        return runCatching {
            readExpressionLocation()
        }.getOrNull()
    }

    private fun JsonObject.parseTupleExpression(): Expression.TupleValue {
        return Expression.TupleValue(
            first = expr(EXPRESSION_TUPLE_FIRST),
            second = expr(EXPRESSION_TUPLE_SECOND),
        )
    }

    private fun JsonObject.parseFirstExpression(): Expression.First {
        return Expression.First(
            value = listOf(expr(EXPRESSION_FIRST_VALUE)),
        )
    }

    private fun JsonObject.parseSecondExpression(): Expression.Second {
        return Expression.Second(
            value = listOf(expr(EXPRESSION_SECOND_VALUE)),
        )
    }

    private fun JsonObject.parseBoolExpression(): Expression.BoolValue {
        return Expression.BoolValue(
            value = value(EXPRESSION_BOOL_VALUE).toBoolean(),
        )
    }

    private fun JsonObject.parseStrExpression(): Expression.StrValue {
        return Expression.StrValue(
            value = value(EXPRESSION_STR_VALUE),
        )
    }

    private fun JsonObject.parsePrintExpression(): Expression.Print {
        return Expression.Print(
            value = listOf(expr(EXPRESSION_PRINT_VALUE)),
        )
    }

    private fun JsonObject.parseCallExpression(): Expression.Call {
        val callee = obj(EXPRESSION_CALL_CALLEE).parseVarExpression()
        val arguments = array(EXPRESSION_CALL_ARGUMENTS)?.mapNotNull {
            it.jsonObject.parseExpressionByKind()
        } ?: throw AstParseException("invalid arguments at Call", tryGetLocation())

        return Expression.Call(
            callee = callee,
            arguments = arguments,
        )
    }

    private fun JsonObject.parseIntExpression(): Expression.IntValue {
        return Expression.IntValue(
            value = value(EXPRESSION_INT_VALUE).toInt(),
        )
    }

    private fun JsonObject.parseVarExpression(): Expression.Var {
        return Expression.Var(
            name = value(EXPRESSION_VAR_TEXT),
        )
    }

    private fun JsonObject.parseBinaryExpression(): Expression.Binary {
        val left = expr(EXPRESSION_BINARY_LEFT)
        val right = expr(EXPRESSION_BINARY_RIGHT)
        val operator = value(EXPRESSION_BINARY_OPERATOR)

        if (BinaryOperator.entries.find { it.name == operator } == null) {
            throw AstParseException("invalid operator '$operator' at Binary", tryGetLocation())
        }

        return Expression.Binary(
            left = left,
            right = right,
            operator = BinaryOperator.valueOf(operator),
        )
    }

    private fun JsonObject.parseIfExpression(): Expression.If {
        val condition = expr(EXPRESSION_IF_CONDITION)
        val then = expr(EXPRESSION_IF_THEN)
        val otherwise = exprOrNull(EXPRESSION_IF_OTHERWISE)

        return Expression.If(
            condition = condition,
            then = listOf(then),
            otherwise = listOfNotNull(otherwise),
        )
    }

    private fun JsonObject.parseFunctionExpression(): Expression.Function {
        val name = optName()
        val parameters = array(EXPRESSION_FUNCTION_PARAMS)?.mapNotNull {
            it.jsonObject.value(EXPRESSION_FUNCTION_PARAM_NAME)
        } ?: throw AstParseException("invalid parameters at Function", tryGetLocation())

        var currentFunctionExpr: JsonObject? = obj(EXPRESSION_FUNCTION_VALUE)
        val expressions = mutableListOf<Expression>()

        do {
            currentFunctionExpr?.parseExpressionByKind()?.let {
                expressions.add(it)
            }
            currentFunctionExpr = currentFunctionExpr?.next()
        } while (currentFunctionExpr != null)

        return Expression.Function(
            name = name,
            parameters = parameters,
            value = expressions,
        )
    }

    private fun JsonObject.parseLetExpression(): Expression.Let {
        return Expression.Let(
            name = obj(EXPRESSION_NAME).value(EXPRESSION_LET_TEXT),
            value = expr(EXPRESSION_LET_VALUE),
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

    private fun JsonObject.next(): JsonObject? {
        return (get(EXPRESSION_NEXT) ?: get(EXPRESSION_ROOT_NEXT))?.jsonObject
    }

    private fun parseExpressions(jsonObject: JsonObject): Result<List<Expression>> {
        val expressions = mutableListOf<Expression>()
        var currentExpr = jsonObject.next()

        do {
            val hasNext = currentExpr != null

            currentExpr?.let {
                val currentExpression = it.parseExpressionByKind()
                expressions.add(currentExpression)

                currentExpr = it.next()
            }
        } while (hasNext)

        return Result.success(expressions)
    }

    private fun JsonObject.expr(key: String): Expression {
        return obj(key).parseExpressionByKind()
    }

    private fun JsonObject.exprOrNull(key: String): Expression? {
        return objOrNull(key)?.parseExpressionByKind()
    }
}
