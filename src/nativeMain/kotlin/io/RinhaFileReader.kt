package io

import okio.FileSystem
import okio.IOException

/**
 * Represents a Rinha file reader.
 */
object RinhaFileReader {
    /**
     * Read file content from [filePath].
     * @param filePath The path of the file to read.
     * @param fileSystem The [FileSystem] to use. Default is [FileSystem.SYSTEM].
     * @return The file content or null if the file does not exist.
     */
    fun readFile(
        filePath: String,
        fileSystem: FileSystem = FileSystem.SYSTEM,
    ): Result<String> {
        return FileReader.readFile(filePath, fileSystem)
            .onFailure {
                throw IOException("can't read file '$filePath'")
            }.onSuccess {
                if (it.isBlank()) {
                    throw IOException("file '$filePath' has no code, it's empty")
                }
            }
    }
}
