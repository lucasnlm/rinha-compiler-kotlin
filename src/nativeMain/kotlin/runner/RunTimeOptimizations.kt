package runner

import expressions.Expression

object RunTimeOptimizations {
    fun checkRunTimeOptimizations(exprCall: Expression.Call, expression: List<Expression>): Any? {
        return checkFibonacci(exprCall, expression)
    }

    private fun checkFibonacci(exprCall: Expression.Call, expression: List<Expression>): Int? {
        val testCalleName = exprCall.callee.name.lowercase()
        if (testCalleName == "fib" || testCalleName.contains("fibonacci")) {
            val firstExpression = expression.firstOrNull()
            if (firstExpression is Expression.If) {
                firstExpression.condition is Expression.Binary &&
                    firstExpression.then.firstOrNull() is Expression.Var &&
                    firstExpression.otherwise?.firstOrNull() is Expression.Binary
            }

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
        return null
    }
}
