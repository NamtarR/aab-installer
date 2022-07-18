package com.namtarr.aabinstaller.view.signing

import com.namtarr.aabinstaller.domain.data.Storage
import com.namtarr.aabinstaller.internal.Effect
import com.namtarr.aabinstaller.internal.ViewModel
import com.namtarr.aabinstaller.model.SigningConfig
import com.namtarr.aabinstaller.utils.awaitNonNullValue
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SigningViewModel(
    private val storage: Storage
): ViewModel() {

    val store = MutableStateFlow(State())
    val effects = Channel<Effect>()

    fun setKeystore(path: String) = store.update { it.copy(keystore = path) }
    fun setKeystorePass(pass: String) = store.update { it.copy(keystorePass = pass) }
    fun setKeyAlias(alias: String) = store.update { it.copy(keyAlias = alias) }
    fun setKeyPass(pass: String) = store.update { it.copy(keyPass = pass) }

    fun save() = viewModelScope.launch {
        val state = store.awaitNonNullValue()
        storage.saveSigningConfig(
            SigningConfig(
                keystore = state.keystore,
                keystorePass = state.keystorePass,
                keyAlias = state.keyAlias,
                keyPass = state.keyPass
            )
        )
        store.update { State() }
        effects.trySend(Effect.ShowSnackbar("Signing config successfully saved"))
    }

    data class State(
        val keystore: String = "",
        val keystorePass: String = "",
        val keyAlias: String = "",
        val keyPass: String = ""
    )
}