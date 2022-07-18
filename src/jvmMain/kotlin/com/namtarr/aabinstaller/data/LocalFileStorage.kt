package com.namtarr.aabinstaller.data

import com.namtarr.aabinstaller.domain.data.Storage
import com.namtarr.aabinstaller.model.Settings
import com.namtarr.aabinstaller.model.SigningConfig
import com.namtarr.aabinstaller.utils.awaitNonNullValue
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

class LocalFileStorage : Storage {

    private val job = SupervisorJob()
    private val scope = CoroutineScope(job + Dispatchers.IO)

    private val json = Json {
        prettyPrint = true
    }

    private val settingsFile = File("settings.json")
    private val settings = MutableStateFlow<Settings?>(null)

    private val signingFile = File("signing.json")
    private val signing = MutableStateFlow<List<SigningConfig>?>(null)

    init {
        scope.launch {
            readSigning()
            readSettings()
        }
    }
    override suspend fun getSigningConfigs(): List<SigningConfig> {
        return signing.awaitNonNullValue()
    }

    override suspend fun saveSigningConfig(config: SigningConfig) {
        val newList = signing.value.orEmpty() + config
        signing.value = newList
        scope.launch {
            signingFile.bufferedWriter().use {
                it.write(json.encodeToString(newList))
            }
        }
    }

    override suspend fun getServiceSettings(): Settings {
        return settings.awaitNonNullValue()
    }

    override suspend fun saveServiceSettings(newSettings: Settings) {
        settings.value = newSettings
        scope.launch {
            settingsFile.bufferedWriter().use {
                it.write(json.encodeToString(newSettings))
            }
        }
    }

    private fun readSettings() {
        settings.value = runCatching {
            json.decodeFromString<Settings>(
                settingsFile.bufferedReader().use { it.readText() }
            )
        }.getOrDefault(Settings())
    }

    private fun readSigning() {
        signing.value = runCatching {
            json.decodeFromString<List<SigningConfig>>(
                signingFile.bufferedReader().use { it.readText() }
            )
        }.getOrDefault(emptyList())
    }
}