package com.namtarr.aabinstaller.view.settings

import com.namtarr.aabinstaller.domain.data.Storage
import com.namtarr.aabinstaller.internal.ViewModel
import com.namtarr.aabinstaller.model.Settings
import com.namtarr.aabinstaller.utils.awaitNonNullValue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val storage: Storage
): ViewModel() {

    val store = MutableStateFlow(State())

    init {
        viewModelScope.launch {
            val settings = storage.getServiceSettings()
            store.update {
                State(
                    adbPath = settings.adbPath,
                    bundletoolPath = settings.bundletoolPath
                )
            }
        }
    }

    fun setAdbPath(path: String) = store.update { it.copy(adbPath = path) }

    fun setBundletoolPath(path: String) = store.update { it.copy(bundletoolPath = path) }

    fun save() = viewModelScope.launch {
        val state = store.awaitNonNullValue()
        storage.saveServiceSettings(
            Settings(
                adbPath = state.adbPath,
                bundletoolPath = state.bundletoolPath
            )
        )
    }

    data class State(
        val adbPath: String? = null,
        val bundletoolPath: String? = null
    )
}