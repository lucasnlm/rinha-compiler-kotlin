import expressions.Expression

object KtTranspiler {
    fun transpile(expressions: List<Expression>): String {
        val body = expressions.mapNotNull {
            transpileExpr(it)
        }.joinToString("\n")
        return wrapMain(body = body)
    }

    private fun transpileExpr(expr: Expression): String? {
        return when (expr) {
            is Expression.Let -> {
                "val ${expr.name} = ${transpileExpr(expr.value)}"
            }
            else -> {
                // Skip
                null
            }
        }
    }

    fun wrapMain(body: String): String {
        return """
        fun main() {
        $body
        }
        """.trimIndent()
    }
}