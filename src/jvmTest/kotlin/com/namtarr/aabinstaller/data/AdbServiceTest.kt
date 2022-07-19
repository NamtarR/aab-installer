package com.namtarr.aabinstaller.data

import com.namtarr.aabinstaller.domain.AdbService
import com.namtarr.aabinstaller.domain.data.CommandRunner
import com.namtarr.aabinstaller.domain.data.Storage
import com.namtarr.aabinstaller.model.Device
import com.namtarr.aabinstaller.model.Result
import com.namtarr.aabinstaller.model.Settings
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import kotlin.test.assertContentEquals
import kotlin.test.assertIs

internal class AdbServiceTest {

    private val storage = mock<Storage> {
        onBlocking { getServiceSettings() } doReturn Settings(ADB_PATH, BUNDLETOOL_PATH)
    }

    private val commandRunner = mock<CommandRunner> {
        onBlocking { run(any()) } doReturn Result.Success(
            """
                List of devices attached
                987654321          device product:TestProduct1 model:TestModel1 device:TestDevice1 transport_id:1
                123456789          device product:TestProduct2 model:TestModel2 device:TestDevice2 transport_id:1
            """.trimIndent()
        )
    }

    private val adbService = AdbService(storage, commandRunner)

    @Test
    fun getDevices() {
        val command = "/test/path/adb devices -l"
        val devices = listOf(
            Device("987654321", "TestModel1"),
            Device("123456789", "TestModel2")
        )
        val result = runBlocking { adbService.getDevices() }

        assertIs<Result.Success<List<Device>>>(result)
        assertContentEquals(devices, result.data)

        runBlocking { verify(commandRunner).run(command) }
    }

    companion object {
        private const val ADB_PATH = "/test/path/adb"
        private const val BUNDLETOOL_PATH = "/test/path/bundletool.jar"
    }
}