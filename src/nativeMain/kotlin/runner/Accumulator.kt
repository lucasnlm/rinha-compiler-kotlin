package runner

/**
 * Holds the value of a temporary call of a recursive function.
 * This is used to implement the Tail Call Optimization at runtime.
 */
data class Accumulator(
    val scope: Map<String, Any?> = mapOf(),
    var sign: Int = 1,
    var value: Any? = null,
) {
    /**
     * Resolves the value of the accumulator.
     */
    fun resolve(accValue: Accumulator): Accumulator {
        return when (this.value) {
            is Int -> {
                copy(
                    value = accValue.asInt() + (this.value as Int) * accValue.sign,
                )
            }
            is String -> {
                copy(
                    value = (this.value as String) + accValue.asString(),
                )
            }
            else -> {
                copy(
                    value = accValue.value,
                )
            }
        }
    }

    /**
     * @return Returns the value as an integer.
     */
    fun asInt(): Int {
        return if (this.value is Int) {
            value as Int
        } else {
            INITIAL_INT_VALUE
        }.also {
            this.value = it
        }
    }

    /**
     * @return Returns the value as a string.
     */
    fun asString(): String {
        return if (this.value is String) {
            value as String
        } else if (value is Int) {
            value.toString()
        } else {
            INITIAL_STRING_VALUE
        }.also {
            this.value = it
        }
    }

    override fun toString(): String {
        return "Accumulator(sign=$sign, value=$value)"
    }

    companion object {
        const val INITIAL_STRING_VALUE = ""
        const val INITIAL_INT_VALUE = 0
    }
}
