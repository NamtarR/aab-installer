package com.namtarr.aabinstaller.data

import com.namtarr.aabinstaller.domain.data.ServiceDiscovery
import com.namtarr.aabinstaller.model.Settings
import com.namtarr.aabinstaller.utils.awaitNonNullValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.File

class StaticServiceDiscovery : ServiceDiscovery {

    private val scope = CoroutineScope(Dispatchers.IO)

    private val file = File("settings.json")
    private val settings = MutableStateFlow<Settings?>(null)

    init {
        scope.launch { readSettings() }
    }

    override suspend fun getAdbPath(): String? {
        return settings.awaitNonNullValue().adbPath
    }

    override suspend fun getBundletoolPath(): String? {
        return settings.awaitNonNullValue().bundletoolPath
    }

    override suspend fun updatePaths(adbPath: String?, bundletoolPath: String?) {
        val newSettings = Settings(adbPath, bundletoolPath)
        settings.value = newSettings
        file.writeText(Json.encodeToString(Settings.serializer(), Settings(adbPath, bundletoolPath)))
    }

    private suspend fun readSettings() = withContext(Dispatchers.IO) {
        settings.value = runCatching {
            Json.decodeFromString<Settings>(file.readText())
        }.getOrElse {
            it.printStackTrace()
            Settings()
        }
    }
}