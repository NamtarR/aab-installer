package com.namtarr.aabinstaller.view.signing

import com.namtarr.aabinstaller.domain.data.Storage
import com.namtarr.aabinstaller.internal.Effect
import com.namtarr.aabinstaller.internal.ViewModel
import com.namtarr.aabinstaller.model.SigningConfig
import com.namtarr.aabinstaller.utils.awaitNonNullValue
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

class SigningViewModel(
    private val storage: Storage
): ViewModel() {

    val store = Store(State(), ::reduce)
    val effects = Channel<Effect>()

    fun setKeystore(path: String) = store.handleEvent(Event.KeystoreEvent(path))
    fun setKeystorePass(pass: String) = store.handleEvent(Event.KeystorePassEvent(pass))
    fun setKeyAlias(alias: String) = store.handleEvent(Event.KeyAliasEvent(alias))
    fun setKeyPass(pass: String) = store.handleEvent(Event.KeyPassEvent(pass))

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
        store.handleEvent(Event.ClearEvent)
        effects.trySend(Effect.ShowSnackbar("Signing config successfully saved"))
    }

    private fun reduce(state: State, event: Event): State {
        return when (event) {
            is Event.KeystoreEvent -> state.copy(keystore = event.text)
            is Event.KeystorePassEvent -> state.copy(keystorePass = event.text)
            is Event.KeyAliasEvent -> state.copy(keyAlias = event.text)
            is Event.KeyPassEvent -> state.copy(keyPass = event.text)
            is Event.ClearEvent -> State()
        }
    }

    data class State(
        val keystore: String = "",
        val keystorePass: String = "",
        val keyAlias: String = "",
        val keyPass: String = ""
    )

    sealed interface Event {
        class KeystoreEvent(val text: String): Event
        class KeystorePassEvent(val text: String): Event
        class KeyAliasEvent(val text: String): Event
        class KeyPassEvent(val text: String): Event
        object ClearEvent : Event
    }
}