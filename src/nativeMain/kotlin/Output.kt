object Output {
    /**
     * Prints a value.
     */
    fun print(value: Any?): Any? {
        println(value)
        return value
    }

    /**
     * Prints a value as an error.
     * @param value The value to be printed.
     */
    fun error(value: Any?): Any? {
        println("e: $value, using v${AppName.version}")
        return value
    }
}