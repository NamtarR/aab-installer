package com.namtarr.aabinstaller.data

import com.namtarr.aabinstaller.domain.data.CommandRunner
import com.namtarr.aabinstaller.domain.data.ServiceDiscovery
import org.junit.jupiter.api.Test

import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

internal class AdbServiceTest {

    private val serviceDiscovery = mock<ServiceDiscovery> {
        onBlocking { getAdbPath() } doReturn ADB_PATH
        onBlocking { getBundletoolPath() } doReturn BUNDLETOOL_PATH
    }

    private val commandRunner = mock<CommandRunner> {
        onBlocking { run(any()) } doReturn ""
    }

    @Test
    fun getDevices() {

    }

    companion object {
        private const val ADB_PATH = "/test/path/adb"
        private const val BUNDLETOOL_PATH = "/test/path/bundletool.jar"
    }
}