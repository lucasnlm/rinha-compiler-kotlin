package parser

import expressions.ExpressionLocation

/**
 * Represents an exception that occurred while parsing the AST.
 */
class AstParseException(
    message: String,
    location: ExpressionLocation? = null,
) : RuntimeException(
    location?.let { "$message at $it" } ?: message,
)
