package transpiler

import expressions.Expression

object IrTranspiler {
    fun transpile(expressions: List<Expression>): String {
        val stringBuilder = StringBuilder()

        expressions.forEach {
            when (it) {
                is Expression.IntValue -> {
                    stringBuilder.append(function(it))
                }
            }
        }

        return mainFunction()
    }

    private fun mainFunction(): String {
        return """
            ; Definition of main function   
            define dso_local noundef i32 @main() #0 {
         
                ret i32 0
            }
        """.trimIndent()
    }

    private fun header(fileName: String): String {
        return """
            ; ModuleID = '$fileName'
            source_filename = "$fileName"
            target datalayout = "e-m:e-p270:32:32-p271:32:32-p272:64:64-i64:64-f80:128-n8:16:32:64-S128"
            target triple = "x86_64-pc-linux-gnu"
        """.trimIndent()
    }

    private fun intIr(expression: Expression.IntValue): String {

    }
}
