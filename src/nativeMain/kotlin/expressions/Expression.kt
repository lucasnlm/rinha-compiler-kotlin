package expressions

/**
 * Represents an expression in the AST.
 */
sealed class Expression {
    /**
     * Represents a let expression in the AST.
     * @property name The name of the variable.
     * @property value The value of the variable.
     */
    data class Let(
        val name: String,
        val value: Expression,
    ) : Expression()

    /**
     * Represents a function expression in the AST.
     * @property name The name of the function.
     * @property parameters The parameters of the function.
     * @property value The value of the function.
     */
    data class Function(
        val name: String? = null,
        val parameters: List<String>,
        val value: List<Expression>,
    ) : Expression() {
        override fun toString(): String {
            val fnName = name ?: "fn"
            val params = parameters.joinToString(", ")
            return "$fnName($params) {...}"
        }
    }

    /**
     * Represents an if expression in the AST.
     * @property condition The condition of the `if` expression.
     * @property then The then expression of the `if` expression.
     * @property otherwise The otherwise expression of the `if` expression.
     */
    data class If(
        val condition: Expression,
        val then: List<Expression>,
        val otherwise: List<Expression>? = null,
    ) : Expression()

    /**
     * Represents a binary expression in the AST.
     * @property left The left side of the binary expression.
     * @property right The right side of the binary expression.
     * @property operator The operator of the binary expression.
     */
    data class Binary(
        val left: Expression,
        val right: Expression,
        val operator: BinaryOperator,
    ) : Expression()

    /**
     * Represents a variable expression in the AST.
     * @property name The name of the variable.
     */
    data class Var(
        val name: String,
    ) : Expression()

    /**
     * Represents an integer value expression in the AST.
     * @property value The value of the integer.
     */
    data class IntValue(
        val value: Int,
    ) : Expression()

    /**
     * Represents a boolean value expression in the AST.
     * @property value The value of the boolean.
     */
    data class BoolValue(
        val value: Boolean,
    ) : Expression()

    /**
     * Represents a string value expression in the AST.
     * @property value The value of the string.
     */
    data class StrValue(
        val value: String,
    ) : Expression()

    /**
     * Represents a tuple value expression in the AST.
     * @property first The first value of the tuple.
     * @property second The second value of the tuple.
     */
    data class TupleValue(
        val first: Expression,
        val second: Expression,
    ) : Expression()

    /**
     * Represents a first expression in the AST.
     * @property value The value of the first expression in a tuple.
     */
    data class First(
        val value: List<Expression>,
    ) : Expression()

    /**
     * Represents a second expression in the AST.
     * @property value The value of the second expression in a tuple.
     */
    data class Second(
        val value: List<Expression>,
    ) : Expression()

    /**
     * Represents a call [Function] expression in the AST.
     * @property callee The callee of the call expression.
     * @property arguments The arguments of the call expression.
     */
    data class Call(
        val callee: Var,
        val arguments: List<Expression>,
    ) : Expression()

    /**
     * Represents a print expression in the AST.
     */
    data class Print(
        val value: List<Expression>,
    ) : Expression()
}
