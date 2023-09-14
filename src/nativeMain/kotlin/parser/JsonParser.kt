package parser

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject

/**
 * Parse JSON strings.
 */
object JsonParser {
    /**
     * Parse a JSON string into a [JsonObject].
     * @param jsonContent The JSON string to parse.
     * @return The [JsonObject] or null if the JSON string is invalid.
     */
    fun parse(jsonContent: String): JsonObject? {
        val format = Json {
            isLenient = true
            coerceInputValues = true
        }

        return runCatching {
            format.decodeFromString<JsonObject>(jsonContent)
        }.getOrNull()
    }
}
