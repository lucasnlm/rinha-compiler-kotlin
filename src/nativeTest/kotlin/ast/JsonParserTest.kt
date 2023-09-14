package ast

import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import parser.JsonParser
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class JsonParserTest {
    @Test
    fun `parse valid json`() {
        val originalJson = """
            {
                "foo": 123,
                "array": [1, 2, 3],
                "obj": {
                    "foo": "bar"
                }
            }
        """.trimIndent()

        val parsedJson = JsonParser.parse(originalJson)

        assertNotNull(parsedJson)
        assertEquals(3, parsedJson.keys.size)
        assertEquals(123, parsedJson["foo"]?.jsonPrimitive?.int)
        assertEquals(listOf(1, 2, 3), parsedJson["array"]?.jsonArray?.map { it.jsonPrimitive.int })
        assertEquals("bar", parsedJson["obj"]?.jsonObject?.get("foo")?.jsonPrimitive?.content)
    }

    @Test
    fun `parse invalid json`() {
        val originalJson = """
            {
                "foo": 123,
                "arra
            }
        """.trimIndent()

        val parsedJson = JsonParser.parse(originalJson)

        assertNull(parsedJson)
    }
}
