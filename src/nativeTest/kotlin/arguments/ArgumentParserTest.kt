package arguments

import kotlin.test.Test
import kotlin.test.assertEquals

class ArgumentParserTest {
    @Test
    fun `get rinha file`() {
        mapOf(
            arrayOf("test.rinha") to Arguments(sourceFilePath = "test.rinha"),
            arrayOf("test.json") to Arguments(astFilePath = "test.json"),
            arrayOf<String>() to Arguments(),
        ).forEach {
            assertEquals(it.value, ArgumentParser.parse(it.key))
        }
    }

    @Test
    fun `get help command arg`() {
        mapOf(
            arrayOf("help") to Arguments(showHelp = true),
            arrayOf("--help") to Arguments(showHelp = true),
            arrayOf("-h") to Arguments(showHelp = true),
        ).forEach {
            assertEquals(it.value, ArgumentParser.parse(it.key))
        }
    }

    @Test
    fun `get repl command arg`() {
        mapOf(
            arrayOf("repl") to Arguments(runRepl = true),
        ).forEach {
            assertEquals(it.value, ArgumentParser.parse(it.key))
        }
    }
}
