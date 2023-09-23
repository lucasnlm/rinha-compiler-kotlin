package io

import mocks.HardcodedScripts
import okio.Path.Companion.toPath
import okio.fakefilesystem.FakeFileSystem
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class RinhaFileReaderTest {
    private val userHome = "/Users/user".toPath()
    private val testRinha = userHome / "test.rinha"

    private val fileSystem = FakeFileSystem().apply {
        createDirectories(userHome)
        write(testRinha) { writeUtf8(HardcodedScripts.sumSource) }
    }

    @Test
    fun `readFile - returns content when file exists`() {
        val fileContents = RinhaFileReader.readFile(
            filePath = "/Users/user/test.rinha",
            fileSystem = fileSystem,
        )
        assertEquals(HardcodedScripts.sumSource, fileContents.getOrNull())
    }

    @Test
    fun `readFile - returns null when file do not exist`() {
        val fileContents = runCatching {
            RinhaFileReader.readFile(
                filePath = "/Users/user/non-existent.rinha",
                fileSystem = fileSystem,
            )
        }
        assertNull(fileContents.getOrNull())
    }
}
