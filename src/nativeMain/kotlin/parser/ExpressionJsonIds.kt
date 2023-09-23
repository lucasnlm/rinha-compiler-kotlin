package parser

/**
 * Expressions names used by the Json parser.
 */
object ExpressionJsonIds {
    const val EXPRESSION_NAME = "name"
    const val EXPRESSION_ROOT_NEXT = "expression"
    const val EXPRESSION_KIND = "kind"
    const val EXPRESSION_NEXT = "next"
    const val EXPRESSION_LOCATION = "location"

    const val LOCATION_START = "start"
    const val LOCATION_END = "end"
    const val LOCATION_FILENAME = "filename"

    const val EXPRESSION_LET_ID = "Let"
    const val EXPRESSION_LET_TEXT = "text"
    const val EXPRESSION_LET_VALUE = "value"

    const val EXPRESSION_FUNCTION_ID = "Function"
    const val EXPRESSION_FUNCTION_PARAMS = "parameters"
    const val EXPRESSION_FUNCTION_PARAM_NAME = "text"
    const val EXPRESSION_FUNCTION_VALUE = "value"

    const val EXPRESSION_IF_ID = "If"
    const val EXPRESSION_IF_CONDITION = "condition"
    const val EXPRESSION_IF_THEN = "then"
    const val EXPRESSION_IF_OTHERWISE = "otherwise"

    const val EXPRESSION_BINARY_ID = "Binary"
    const val EXPRESSION_BINARY_LEFT = "lhs"
    const val EXPRESSION_BINARY_RIGHT = "rhs"
    const val EXPRESSION_BINARY_OPERATOR = "op"

    const val EXPRESSION_VAR_ID = "Var"
    const val EXPRESSION_VAR_TEXT = "text"

    const val EXPRESSION_INT_ID = "Int"
    const val EXPRESSION_INT_VALUE = "value"

    const val EXPRESSION_STR_ID = "Str"
    const val EXPRESSION_STR_VALUE = "value"

    const val EXPRESSION_BOOL_ID = "Bool"
    const val EXPRESSION_BOOL_VALUE = "value"

    const val EXPRESSION_TUPLE_ID = "Tuple"
    const val EXPRESSION_TUPLE_FIRST = "first"
    const val EXPRESSION_TUPLE_SECOND = "second"

    const val EXPRESSION_FIRST_ID = "First"
    const val EXPRESSION_FIRST_VALUE = "value"

    const val EXPRESSION_SECOND_ID = "Second"
    const val EXPRESSION_SECOND_VALUE = "value"

    const val EXPRESSION_CALL_ID = "Call"
    const val EXPRESSION_CALL_CALLEE = "callee"
    const val EXPRESSION_CALL_ARGUMENTS = "arguments"

    const val EXPRESSION_PRINT_ID = "Print"
    const val EXPRESSION_PRINT_VALUE = "value"
}
