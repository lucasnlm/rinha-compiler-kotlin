package io

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import okio.FileSystem
import okio.IOException

/**
 * Represents a JSON file reader.
 */
object JsonFileReader {
    /**
     * Read a JSON file content from [filePath].
     * @param filePath The path of the file to read.
     * @param fileSystem The [FileSystem] to use. Default is [FileSystem.SYSTEM].
     */
    fun readFile(
        filePath: String,
        fileSystem: FileSystem = FileSystem.SYSTEM,
    ): Result<JsonObject> {
        return FileReader.readFile(filePath, fileSystem)
            .onFailure {
                throw IOException("can't read file '$filePath'")
            }
            .onSuccess {
                if (it.isBlank()) {
                    throw IOException("file '$filePath' has no code, it's empty")
                }
            }
            .getOrThrow()
            .let(::parse)
            .onFailure {
                throw IOException("fail to parse JSON '$filePath'")
            }
    }

    /**
     * Parse a JSON string into a [JsonObject].
     */
    fun parse(jsonContent: String): Result<JsonObject> {
        val format = Json {
            isLenient = true
            coerceInputValues = true
        }

        return runCatching {
            format.decodeFromString<JsonObject>(jsonContent)
        }
    }
}
