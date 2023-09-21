package io

import okio.FileSystem
import okio.Path.Companion.toPath
import okio.buffer
import okio.use

/**
 * Represents a file reader.
 */
object FileReader {
    /**
     * Read file content from [filePath].
     * @param filePath The path of the file to read.
     * @param fileSystem The [FileSystem] to use. Default is [FileSystem.SYSTEM].
     * @return The file content or null if the file does not exist.
     */
    fun readFile(
        filePath: String,
        fileSystem: FileSystem = FileSystem.SYSTEM,
    ): String? {
        val path = filePath.toPath(true)
        return runCatching {
            fileSystem.source(path).use { source ->
                source.buffer().use { buffer ->
                    buffer.readUtf8()
                }
            }
        }.getOrNull()
    }

    /**
     * Write [content] to [filePath].
     */
    fun writeFile(
        filePath: String,
        content: String,
        fileSystem: FileSystem = FileSystem.SYSTEM,
    ) {
        val path = filePath.toPath(true)
        runCatching {
            fileSystem.sink(path).use { sink ->
                sink.buffer().use { buffer ->
                    buffer.writeUtf8(content)
                }
            }
        }
    }
}
