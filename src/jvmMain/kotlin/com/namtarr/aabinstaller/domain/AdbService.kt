package com.namtarr.aabinstaller.domain

import com.namtarr.aabinstaller.domain.data.CommandRunner
import com.namtarr.aabinstaller.domain.data.ServiceDiscovery
import com.namtarr.aabinstaller.model.Device

class AdbService(
    private val discovery: ServiceDiscovery,
    private val runner: CommandRunner
) {

    suspend fun getDevices(): List<Device> {
        val adb = discovery.getAdbPath() ?: throw IllegalStateException("Adb is not found")
        val command = listOf(adb, DEVICES).joinToString(" ")
        val result = runner.run(command)

        return parseDevicesResponse(result)
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