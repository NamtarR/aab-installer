package com.namtarr.aabinstaller.data

import com.namtarr.aabinstaller.domain.BundleToolService
import com.namtarr.aabinstaller.domain.data.CommandRunner
import com.namtarr.aabinstaller.domain.data.Storage
import com.namtarr.aabinstaller.model.Device
import com.namtarr.aabinstaller.model.Settings
import com.namtarr.aabinstaller.model.SigningConfig
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

internal class BundleToolServiceTest {

    private val storage = mock<Storage> {
        onBlocking { getServiceSettings() } doReturn Settings(ADB_PATH, BUNDLETOOL_PATH)
    }

    private val commandRunner = mock<CommandRunner> {
        onBlocking { run(any()) } doReturn ""
    }

    private val bundleToolService = BundleToolService(storage, commandRunner)

    @Test
    fun installApks() {
        val apks = "/test/path/bundle.apks"
        val device = Device("987654321", "Test_Device")
        val command = "java -jar /test/path/bundletool.jar install-apks " +
                "--apks=/test/path/bundle.apks " +
                "--device-id=987654321" +
                "--adb=/test/path/adb"
        val result = runBlocking { bundleToolService.installApks(apks, device) }

        assertEquals("", result)

        runBlocking { verify(commandRunner).run(command) }
    }

    @Test
    fun buildApks() {
        val aab = "/test/path/bundle.aab"
        val signingConfig = SigningConfig(
            keystore = "/test/keystore.jks",
            keystorePass = "keystore-pass",
            keyAlias = "key-alias",
            keyPass = "key-pass"
        )
        val device = Device("987654321", "Test_Device.json")
        val command = "java -jar /test/path/bundletool.jar build-apks " +
                "--bundle=/test/path/bundle.aab " +
                "--ks=/test/keystore.jks " +
                "--ks-pass=pass:keystore-pass " +
                "--ks-key-alias=key-alias " +
                "--key-pass=pass:key-pass " +
                "--device-id=987654321" +
                "--adb=/test/path/adb"
        val result = runBlocking { bundleToolService.buildApks(aab, signingConfig, device) }

        assertEquals("", result)

        runBlocking { verify(commandRunner).run(command) }
    }

    companion object {
        private const val ADB_PATH = "/test/path/adb"
        private const val BUNDLETOOL_PATH = "/test/path/bundletool.jar"
    }
}