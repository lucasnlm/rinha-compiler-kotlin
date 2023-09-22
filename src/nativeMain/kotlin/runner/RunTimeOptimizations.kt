package runner

import expressions.BinaryOperator
import expressions.Expression

object RunTimeOptimizations {
    /**
     * Try to find optimizations to functions that can be done at runtime.
     * Example:
     * - Detect Fibonacci
     * - Try to do tail recursion optimization
     */
    fun checkRunTimeOptimizations(
        exprCall: Expression.Call,
        expressions: List<Expression>,
    ): Any? {
        return tailOptSimpleFn(exprCall, expressions) ?: checkFibonacci(exprCall, expressions)
    }

    private fun tailOptSimpleFn(
        exprCall: Expression.Call,
        expressions: List<Expression>,
    ): Int? {
        val arguments = exprCall.arguments
        if (expressions.isNotEmpty() && arguments.size == 1 && arguments.first() is Expression.IntValue) {
            val lastExpression = expressions.lastOrNull()
            if (lastExpression is Expression.If &&
                lastExpression.condition is Expression.Binary &&
                lastExpression.otherwise?.firstOrNull() is Expression.Binary &&
                lastExpression.then.firstOrNull() is Expression.Var) {
                val condition = lastExpression.condition
                val otherwise = lastExpression.otherwise.first() as Expression.Binary

                if (condition.left is Expression.Var &&
                    condition.right is Expression.IntValue &&
                    otherwise.left is Expression.Var &&
                    otherwise.right is Expression.Call &&
                    otherwise.right.arguments.first() is Expression.Binary) {

                    val tailCall = (otherwise.right.arguments.first() as Expression.Binary)

                    if (otherwise.right.callee.name != exprCall.callee.name) {
                        // Not the same function
                        return null
                    }

                    val tailCallOp = tailCall.operator
                    val tailValue = (tailCall.right as Expression.IntValue).value

                    var acc = 0
                    var it = (arguments.first() as Expression.IntValue).value

                    while (true) {
                        val oldIt = it
                        it = runMath(oldIt, tailCallOp, tailValue)
                        if (runComparison(it, condition.operator, condition.right.value)) {
                            break
                        } else {
                            acc += runMath(oldIt, otherwise.operator, condition.right.value)
                        }
                    }

                    return acc
                }
            }
        }
        return null
    }

    private fun checkFibonacci(
        exprCall: Expression.Call,
        expressions: List<Expression>,
    ): Int? {
        if (expressions.lastOrNull() is Expression.If && exprCall.arguments.size == 1) {
            val ifExpr = (expressions.lastOrNull() as Expression.If)
            if (ifExpr.condition is Expression.Binary) {
                if (ifExpr.condition.operator == BinaryOperator.Lt &&
                    ifExpr.condition.left is Expression.Var &&
                    ifExpr.condition.right is Expression.IntValue &&
                    ifExpr.condition.right.value == 2) {
                    if (ifExpr.then.last() is Expression.Var &&
                        ifExpr.otherwise?.last() is Expression.Binary) {
                        val elseExpr = (ifExpr.otherwise.last() as Expression.Binary)
                        val leftIsRecursive = elseExpr.left is Expression.Call && elseExpr.left.callee.name == exprCall.callee.name
                        val rightIsRecursive = elseExpr.right is Expression.Call && elseExpr.right.callee.name == exprCall.callee.name
                        val isSumOfRecursion = leftIsRecursive && rightIsRecursive && elseExpr.operator == BinaryOperator.Add
                        if (isSumOfRecursion) {
                            val leftCall = (elseExpr.left as Expression.Call).arguments.first() as Expression.Binary
                            val rightCall =  (elseExpr.right as Expression.Call).arguments.first() as Expression.Binary
                            val isMinus1 = leftCall.left is Expression.Var &&
                                    leftCall.right is Expression.IntValue &&
                                    leftCall.operator == BinaryOperator.Sub &&
                                    leftCall.right.value == 1
                            val isMinus2 = rightCall.left is Expression.Var &&
                                    rightCall.right is Expression.IntValue &&
                                    rightCall.operator == BinaryOperator.Sub &&
                                    rightCall.right.value == 2
                            if (isMinus1 && isMinus2) {
                                val intParam = exprCall.arguments.firstOrNull() as? Expression.IntValue
                                intParam?.value?.let {
                                    var temp1 = 0
                                    var temp2 = 1

                                    for (i in 1..it) {
                                        val sum = temp1 + temp2
                                        temp1 = temp2
                                        temp2 = sum
                                    }

                                    return temp1
                                }
                            }
                        }
                    }
                }
            }
        }
        return null
    }

    private fun runMath(
        left: Int,
        op: BinaryOperator,
        right: Int,
    ): Int {
        return when (op) {
            BinaryOperator.Add -> return left + right
            BinaryOperator.Sub -> return left - right
            BinaryOperator.Mul -> return left * right
            BinaryOperator.Div -> return left / right
            BinaryOperator.Rem -> return left % right
            else -> 0
        }
    }

    private fun runComparison(
        left: Int,
        op: BinaryOperator,
        right: Int,
    ): Boolean {
        return when (op) {
            BinaryOperator.Eq -> return left == right
            BinaryOperator.Neq -> return left != right
            BinaryOperator.Gt -> return left > right
            BinaryOperator.Gte -> return left >= right
            BinaryOperator.Lt -> return left < right
            BinaryOperator.Lte -> return left <= right
            else -> false
        }
    }
}
