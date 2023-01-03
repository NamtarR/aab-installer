package com.namtarr.aabinstaller.data

import com.namtarr.aabinstaller.domain.AdbService
import com.namtarr.aabinstaller.domain.data.CommandRunner
import com.namtarr.aabinstaller.domain.data.Storage
import com.namtarr.aabinstaller.model.Device
import com.namtarr.aabinstaller.model.Result
import com.namtarr.aabinstaller.model.Settings
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*

import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertIs

internal class AdbServiceTest {

    private val storage = mock<Storage>()

    private val commandRunner = mock<CommandRunner>()

    private val adbService = AdbService(storage, commandRunner)

    @Test
    fun getDevices_success() {
        storage.stub {
            onBlocking { getServiceSettings() } doReturn Settings(ADB_PATH, BUNDLETOOL_PATH)
        }

        commandRunner.stub {
            onBlocking { run(any()) } doReturn Result.Success(
                """
                List of devices attached
                987654321          device product:TestProduct1 model:TestModel1 device:TestDevice1 transport_id:1
                123456789          device product:TestProduct2 model:TestModel2 device:TestDevice2 transport_id:1
            """.trimIndent()
            )
        }

        val result = runBlocking { adbService.getDevices() }

        assertIs<Result.Success<List<Device>>>(result)
        assertContentEquals(
            expected = listOf(
                Device("987654321", "TestModel1"),
                Device("123456789", "TestModel2")
            ),
            actual = result.data
        )

        runBlocking { verify(commandRunner).run("/test/path/adb devices -l") }
    }

    @Test
    fun getDevices_adb_not_found() {
        storage.stub {
            onBlocking { getServiceSettings() } doReturn Settings(null, BUNDLETOOL_PATH)
        }

        val result = runBlocking { adbService.getDevices() }

        assertIs<Result.Failure.Service<List<Device>>>(result)
        assertEquals("ADB", result.service)
    }

    @Test
    fun getDevices_no_devices_found() {
        storage.stub {
            onBlocking { getServiceSettings() } doReturn Settings(ADB_PATH, BUNDLETOOL_PATH)
        }

        commandRunner.stub {
            onBlocking { run(any()) } doReturn Result.Success(
                """
                List of devices attached
            """.trimIndent()
            )
        }

        val result = runBlocking { adbService.getDevices() }

        assertIs<Result.Success<List<Device>>>(result)
        assertContentEquals(
            expected = emptyList(),
            actual = result.data
        )

        runBlocking { verify(commandRunner).run("/test/path/adb devices -l") }
    }

    companion object {
        private const val ADB_PATH = "/test/path/adb"
        private const val BUNDLETOOL_PATH = "/test/path/bundletool.jar"
    }
}