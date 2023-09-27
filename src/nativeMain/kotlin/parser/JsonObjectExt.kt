package parser

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

/**
 * Extension functions for [JsonObject].
 */
internal object JsonObjectExt {
    /**
     * Get a [JsonObject] from [key].
     * @return The [JsonObject] or null if the key does not exist.
     */
    fun JsonObject.obj(key: String): JsonObject {
        return get(key)?.jsonObject ?: throw AstParseException("can't find expr '$key'")
    }

    /**
     * Get a [JsonObject] from [key].
     * @return The [JsonObject] or null if the key does not exist.
     */
    fun JsonObject.objOrNull(key: String): JsonObject? {
        return get(key)?.jsonObject
    }

    /**
     * Get a [JsonArray] from [key].
     * @return The [JsonArray] or null if the key does not exist.
     */
    fun JsonObject.array(key: String): JsonArray? {
        return get(key)?.jsonArray
    }

    /**
     * Get a [String] from [key].
     * @return The [String] or null if the key does not exist.
     */
    fun JsonObject.value(key: String): String {
        return get(key)?.jsonPrimitive?.content ?: throw AstParseException("can't find '$key'")
    }

    /**
     * Get a [String] from [key].
     * @return The [String] or null if the key does not exist.
     */
    fun JsonObject.optValue(key: String): String? {
        return get(key)?.jsonPrimitive?.content
    }
}
