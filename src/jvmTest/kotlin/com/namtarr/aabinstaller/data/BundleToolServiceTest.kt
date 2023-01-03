package com.namtarr.aabinstaller.data

import com.namtarr.aabinstaller.domain.BundleToolService
import com.namtarr.aabinstaller.domain.data.CommandRunner
import com.namtarr.aabinstaller.domain.data.Storage
import com.namtarr.aabinstaller.domain.data.Unarchiver
import com.namtarr.aabinstaller.model.Device
import com.namtarr.aabinstaller.model.Result
import com.namtarr.aabinstaller.model.Settings
import com.namtarr.aabinstaller.model.SigningConfig
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import kotlin.test.assertIs

internal class BundleToolServiceTest {

    private val storage = mock<Storage>()
    private val commandRunner = mock<CommandRunner>()
    private val unarchiver = mock<Unarchiver>()
    private val bundleToolService = BundleToolService(storage, commandRunner, unarchiver)

    @Test
    fun installApks_success() {
        val apks = "/test/path/bundle.apks"
        val device = Device("987654321", "Test_Device")
        val command = "java -jar /test/path/bundletool.jar install-apks " +
                "--apks=/test/path/bundle.apks " +
                "--device-id=987654321 " +
                "--adb=/test/path/adb"

        storage.stub  {
            onBlocking { getServiceSettings() } doReturn Settings(ADB_PATH, BUNDLETOOL_PATH)
        }
        commandRunner.stub {
            onBlocking { run(any()) } doReturn Result.Success("")
        }

        val result = runBlocking { bundleToolService.install(apks, device) }

        assertIs<Result.Success<Unit>>(result)

        runBlocking { verify(commandRunner).run(command) }
    }

    @Test
    fun installApks_adb_not_found() {
        val apks = "/test/path/bundle.apks"
        val device = Device("987654321", "Test_Device")

        storage.stub  {
            onBlocking { getServiceSettings() } doReturn Settings(null, BUNDLETOOL_PATH)
        }

        val result = runBlocking { bundleToolService.install(apks, device) }

        assertIs<Result.Failure.Service<Unit>>(result)
        assertEquals("ADB", result.service)
    }

    @Test
    fun installApks_bundletool_not_found() {
        val apks = "/test/path/bundle.apks"
        val device = Device("987654321", "Test_Device")

        storage.stub  {
            onBlocking { getServiceSettings() } doReturn Settings(ADB_PATH, null)
        }

        val result = runBlocking { bundleToolService.install(apks, device) }

        assertIs<Result.Failure.Service<Unit>>(result)
        assertEquals("Bundletool", result.service)
    }

    @Test
    fun installApks_bundletool_exception() {
        val apks = "/test/path/bundle.apks"
        val device = Device("987654321", "Test_Device")
        val command = "java -jar /test/path/bundletool.jar install-apks " +
                "--apks=/test/path/bundle.apks " +
                "--device-id=987654321 " +
                "--adb=/test/path/adb"

        storage.stub  {
            onBlocking { getServiceSettings() } doReturn Settings(ADB_PATH, BUNDLETOOL_PATH)
        }
        commandRunner.stub {
            onBlocking { run(any()) } doReturn Result.Failure.Command(1)
        }

        val result = runBlocking { bundleToolService.install(apks, device) }

        assertIs<Result.Failure.Command<Unit>>(result)

        runBlocking { verify(commandRunner).run(command) }
    }

    @Test
    fun build_apks_success() {
        val aab = "/test/path/bundle.aab"
        val apks = "/test/path/bundle.apks"
        val signingConfig = SigningConfig(
            keystore = "/test/keystore.jks",
            keystorePass = "keystore-pass",
            keyAlias = "key-alias",
            keyPass = "key-pass"
        )
        val device = Device("987654321", "Test_Device.json")
        val command = "java -jar /test/path/bundletool.jar build-apks " +
                "--bundle=/test/path/bundle.aab " +
                "--output=/test/path/bundle.apks " +
                "--ks=/test/keystore.jks " +
                "--ks-pass=pass:keystore-pass " +
                "--ks-key-alias=key-alias " +
                "--key-pass=pass:key-pass " +
                "--overwrite " +
                "--connected-device " +
                "--device-id=987654321 " +
                "--adb=/test/path/adb"

        storage.stub  {
            onBlocking { getServiceSettings() } doReturn Settings(ADB_PATH, BUNDLETOOL_PATH)
        }
        commandRunner.stub {
            onBlocking { run(any()) } doReturn Result.Success("")
        }

        val result = runBlocking { bundleToolService.build(aab, signingConfig, device) }

        assertIs<Result.Success<String>>(result)
        assertEquals(result.data, apks)

        runBlocking { verify(commandRunner).run(command) }
    }

    @Test
    fun build_apk_success() {
        val aab = "/test/path/bundle.aab"
        val apks = "/test/path/bundle.apks"
        val apk = "/test/path/bundle.apk"
        val signingConfig = SigningConfig(
            keystore = "/test/keystore.jks",
            keystorePass = "keystore-pass",
            keyAlias = "key-alias",
            keyPass = "key-pass"
        )
        val command = "java -jar /test/path/bundletool.jar build-apks " +
                "--bundle=/test/path/bundle.aab " +
                "--output=/test/path/bundle.apks " +
                "--ks=/test/keystore.jks " +
                "--ks-pass=pass:keystore-pass " +
                "--ks-key-alias=key-alias " +
                "--key-pass=pass:key-pass " +
                "--overwrite " +
                "--mode=universal"

        storage.stub  {
            onBlocking { getServiceSettings() } doReturn Settings(ADB_PATH, BUNDLETOOL_PATH)
        }
        commandRunner.stub {
            onBlocking { run(any()) } doReturn Result.Success("")
        }
        unarchiver.stub {
            onBlocking { getFileFromArchive(apks, "universal.apk") } doReturn Result.Success(apk)
        }

        val result = runBlocking { bundleToolService.build(aab, signingConfig, null) }

        assertIs<Result.Success<String>>(result)
        assertEquals(result.data, apk)

        runBlocking { verify(commandRunner).run(command) }
    }

    @Test
    fun build_adb_not_found() {
        val apks = "/test/path/bundle.apks"
        val device = Device("987654321", "Test_Device")
        val signingConfig = SigningConfig(
            keystore = "/test/keystore.jks",
            keystorePass = "keystore-pass",
            keyAlias = "key-alias",
            keyPass = "key-pass"
        )

        storage.stub  {
            onBlocking { getServiceSettings() } doReturn Settings(null, BUNDLETOOL_PATH)
        }

        val result = runBlocking { bundleToolService.build(apks, signingConfig, device) }

        assertIs<Result.Failure.Service<Unit>>(result)
        assertEquals("ADB", result.service)
    }

    @Test
    fun build_bundletool_not_found() {
        val apks = "/test/path/bundle.apks"
        val device = Device("987654321", "Test_Device")
        val signingConfig = SigningConfig(
            keystore = "/test/keystore.jks",
            keystorePass = "keystore-pass",
            keyAlias = "key-alias",
            keyPass = "key-pass"
        )

        storage.stub  {
            onBlocking { getServiceSettings() } doReturn Settings(ADB_PATH, null)
        }

        val result = runBlocking { bundleToolService.build(apks, signingConfig, device) }

        assertIs<Result.Failure.Service<Unit>>(result)
        assertEquals("Bundletool", result.service)
    }

    @Test
    fun build_bundletool_exception() {
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
                "--output=/test/path/bundle.apks " +
                "--ks=/test/keystore.jks " +
                "--ks-pass=pass:keystore-pass " +
                "--ks-key-alias=key-alias " +
                "--key-pass=pass:key-pass " +
                "--overwrite " +
                "--connected-device " +
                "--device-id=987654321 " +
                "--adb=/test/path/adb"

        storage.stub  {
            onBlocking { getServiceSettings() } doReturn Settings(ADB_PATH, BUNDLETOOL_PATH)
        }
        commandRunner.stub {
            onBlocking { run(any()) } doReturn Result.Failure.Command(1)
        }

        val result = runBlocking { bundleToolService.build(aab, signingConfig, device) }

        assertIs<Result.Failure.Command<String>>(result)
        assertEquals(result.exitCode, 1)

        runBlocking { verify(commandRunner).run(command) }
    }

    companion object {
        private const val ADB_PATH = "/test/path/adb"
        private const val BUNDLETOOL_PATH = "/test/path/bundletool.jar"
    }
}