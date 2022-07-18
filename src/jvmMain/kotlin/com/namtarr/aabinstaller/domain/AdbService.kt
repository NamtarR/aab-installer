package com.namtarr.aabinstaller.domain

import com.namtarr.aabinstaller.domain.data.CommandRunner
import com.namtarr.aabinstaller.domain.data.Storage
import com.namtarr.aabinstaller.model.Device
import com.namtarr.aabinstaller.model.Result

class AdbService(
    private val storage: Storage,
    private val runner: CommandRunner
) {

    suspend fun getDevices(): Result<List<Device>> {
        val adb = storage.getServiceSettings().adbPath ?: return Result.serviceFailure("ADB")
        val command = listOf(adb, DEVICES).joinToString(" ")
        val result = runner.run(command)

        return result.map { parseDevicesResponse(it) }
    }

    private fun parseDevicesResponse(response: String): List<Device> {
        return response.split("\n")
            .asSequence()
            .drop(1)
            .filter { it.isNotBlank() }
            .map { line ->
                val parts = line.split(" ")

                Device(
                    id = parts.first(),
                    model = parts
                        .first { it.startsWith("model") }
                        .replace("model:", "")
                )
            }
            .toList()
    }

    companion object {
        private const val DEVICES = "devices -l"
    }
}