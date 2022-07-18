package com.namtarr.aabinstaller.view.settings

import com.namtarr.aabinstaller.domain.data.Storage
import com.namtarr.aabinstaller.internal.ViewModel
import com.namtarr.aabinstaller.model.Settings
import com.namtarr.aabinstaller.utils.awaitNonNullValue
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val storage: Storage
): ViewModel() {

    val store = Store(State(), ::reduce)

    init {
        viewModelScope.launch {
            val settings = storage.getServiceSettings()
            store.handleEvents(listOf(
                Event.AdbPathEvent(settings.adbPath),
                Event.BundletoolPathEvent(settings.bundletoolPath)
            ))
        }
    }

    private fun reduce(state: State, event: Event): State {
        return when (event) {
            is Event.AdbPathEvent -> state.copy(adbPath = event.path)
            is Event.BundletoolPathEvent -> state.copy(bundletoolPath = event.path)
        }
    }

    fun setAdbPath(path: String) = store.handleEvent(Event.AdbPathEvent(path))

    fun setBundletoolPath(path: String) = store.handleEvent(Event.BundletoolPathEvent(path))

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

    sealed interface Event {
        class AdbPathEvent(val path: String?): Event
        class BundletoolPathEvent(val path: String?): Event
    }
}