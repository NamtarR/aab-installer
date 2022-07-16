package com.namtarr.aabinstaller.domain

import com.namtarr.aabinstaller.domain.data.CommandRunner
import com.namtarr.aabinstaller.domain.data.ServiceDiscovery
import com.namtarr.aabinstaller.model.Device
import com.namtarr.aabinstaller.model.SigningConfig

class BundleToolService(
    private val discovery: ServiceDiscovery,
    private val runner: CommandRunner
) {

    private fun param(name: String, value: String?) = value?.let { "--$name=$it" }
    private fun pass(name: String, value: String) = "--$name=pass:$value"

    suspend fun buildApks(aabPath: String, signingConfig: SigningConfig, device: Device?): String {
        val bundleTool = discovery.getBundletoolPath() ?: throw IllegalStateException("Bundletool is not found")
        val adb = discovery.getAdbPath() ?: throw IllegalStateException("Adb is not found")

        val output = aabPath.replace(".aab", ".apks")
        val command = listOfNotNull(
            JAVA_JAR,
            bundleTool,
            BUILD,
            param(BUNDLE, aabPath),
            param(OUTPUT, output),

            param(KS_FILE, signingConfig.keystore),
            pass(KS_PASS, signingConfig.keystorePass),
            param(KS_KEY_ALIAS, signingConfig.keyAlias),
            pass(KS_KEY_PASS, signingConfig.keyPass),

            UNIVERSAL.takeIf { device == null },

            DEVICE.takeIf { device != null },
            param(DEVICE_ID, device?.id),

            param(ADB, adb).takeIf { device != null }
        ).joinToString(" ")

        runner.run(command)

        return output
    }

    suspend fun installApks(apksPath: String, device: Device) {
        val bundleTool = discovery.getBundletoolPath() ?: throw IllegalStateException("Bundletool is not found")
        val adb = discovery.getAdbPath() ?: throw IllegalStateException("Adb is not found")
        val command = listOfNotNull(
            JAVA_JAR,
            bundleTool,
            INSTALL,
            param(APKS, apksPath),
            param(DEVICE_ID, device.id),
            param(ADB, adb)
        ).joinToString(" ")

        runner.run(command)
    }

    companion object {
        private const val JAVA_JAR = "java -jar"

        private const val INSTALL = "install-apks"
        private const val BUILD = "build-apks"

        private const val BUNDLE = "bundle"
        private const val APKS = "apks"
        private const val OUTPUT = "output"

        private const val KS_FILE = "ks"
        private const val KS_PASS = "ks-pass"
        private const val KS_KEY_ALIAS = "ks-key-alias"
        private const val KS_KEY_PASS = "key-pass"

        private const val DEVICE_ID = "device-id"
        private const val DEVICE_SPEC = "device-spec"
        private const val DEVICE = "--connected-device"

        private const val ADB = "adb"
        private const val UNIVERSAL = "--mode=universal"
    }
}