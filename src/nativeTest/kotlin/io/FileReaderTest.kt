package io

import okio.Path.Companion.toPath
import okio.fakefilesystem.FakeFileSystem
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class FileReaderTest {
    private val userHome = "/Users/user".toPath()
    private val testJson = userHome / "test.json"

    private val fileSystem = FakeFileSystem().apply {
        createDirectories(userHome)
        write(testJson) { writeUtf8("{\"too\": 123}") }
    }

    @Test
    fun `readFile - returns content when file exists`() {
        val fileContents = FileReader.readFile(
            filePath = "/Users/user/test.json",
            fileSystem = fileSystem,
        )
        assertEquals("{\"too\": 123}", fileContents)
    }

    @Test
    fun `readFile - returns null when file do not exist`() {
        val fileContents = FileReader.readFile(
            filePath = "/Users/user/non-existent.json",
            fileSystem = fileSystem,
        )
        assertNull(fileContents)
    }
}
