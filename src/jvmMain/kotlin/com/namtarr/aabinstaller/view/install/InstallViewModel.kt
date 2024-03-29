package com.namtarr.aabinstaller.view.install

import com.namtarr.aabinstaller.domain.AdbService
import com.namtarr.aabinstaller.domain.BundleToolService
import com.namtarr.aabinstaller.domain.data.Storage
import com.namtarr.aabinstaller.internal.Effect
import com.namtarr.aabinstaller.internal.ViewModel
import com.namtarr.aabinstaller.model.Result
import com.namtarr.aabinstaller.model.SigningConfig
import com.namtarr.aabinstaller.utils.awaitNonNullValue
import com.namtarr.aabinstaller.view.model.DeviceOption
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class InstallViewModel(
    private val adbService: AdbService,
    private val bundleToolService: BundleToolService,
    private val storage: Storage
) : ViewModel() {

    val store = MutableStateFlow(State())
    val effects = Channel<Effect>()

    init {
        loadDevices()
        loadConfigs()
    }

    fun loadDevices() = viewModelScope.launch {
        adbService.getDevices().on { devices ->
            store.update { state ->
                val deviceOptions = listOf(DeviceOption.Universal) + devices.map { DeviceOption.Real(it) }
                val selected = state.selectedDevice.takeIf { it < deviceOptions.size } ?: 0
                state.copy(devices = deviceOptions, selectedDevice = selected)
            }
        }
    }

    fun loadConfigs() = viewModelScope.launch {
        store.update { it.copy(signingConfigs = storage.getSigningConfigs()) }
    }

    fun setBundlePath(path: String) = store.update { it.copy(bundlePath = path) }

    fun selectDevice(index: Int) = store.update { it.copy(selectedDevice = index) }

    fun selectConfig(index: Int) = store.update { it.copy(selectedSigningConfig = index) }

    fun build() = viewModelScope.launch {
        store.update { it.copy(isLoading = true) }
        val state = store.awaitNonNullValue()
        bundleToolService
            .build(
                aabPath = state.bundlePath,
                signingConfig = state.signingConfigs[state.selectedSigningConfig],
                device = state.devices[state.selectedDevice].device
            )
            .on {
                effects.trySend(Effect.OpenFileManager(it))
            }
        store.update { it.copy(isLoading = false) }
    }

    fun buildAndInstall() = viewModelScope.launch {
        store.update { it.copy(isLoading = true) }
        val state = store.awaitNonNullValue()
        val device = state.devices[state.selectedDevice].device ?: return@launch
        bundleToolService
            .build(
                aabPath = state.bundlePath,
                signingConfig = state.signingConfigs[state.selectedSigningConfig],
                device = device
            )
            .flatMap { path ->
                bundleToolService.install(
                    apksPath = path,
                    device = device
                )
            }
            .on {
                effects.trySend(Effect.ShowSnackbar("App is successfully installed."))
            }
        store.update { it.copy(isLoading = false) }
    }

    data class State(
        val devices: List<DeviceOption> = emptyList(),
        val selectedDevice: Int = 0,
        val signingConfigs: List<SigningConfig> = emptyList(),
        val selectedSigningConfig: Int = 0,
        val bundlePath: String = "",
        val isLoading: Boolean = false
    )

    private fun <T> Result<T>.on(success: (T) -> Unit) = on(
        success = success,
        commandFailure = {
            effects.trySend(Effect.ShowSnackbar("Process exited with a code $it. Check the details in the console tab."))
        },
        serviceFailure = {
            effects.trySend(Effect.ShowSnackbar("$it cannot be found. Check settings and try again."))
        }
    )
}