package com.namtarr.aabinstaller.view.settings

import com.namtarr.aabinstaller.domain.data.ServiceDiscovery
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val serviceDiscovery: ServiceDiscovery
) {

    val state = MutableStateFlow(State())
    val job = SupervisorJob()
    val scope = CoroutineScope(Dispatchers.Default + job)

    init {
        scope.launch {
            state.value = State(
                serviceDiscovery.getAdbPath(),
                serviceDiscovery.getBundletoolPath()
            )
        }
    }

    fun setAdbPath(path: String) {
        state.update { it.copy(adbPath = path) }
    }

    fun setBundletoolPath(path: String) {
        state.update { it.copy(bundletoolPath = path) }
    }

    fun save() {
        val current = state.value

        scope.launch {
            serviceDiscovery.updatePaths(current.adbPath, current.bundletoolPath)
        }
    }

    data class State(
        val adbPath: String? = null,
        val bundletoolPath: String? = null
    )
}