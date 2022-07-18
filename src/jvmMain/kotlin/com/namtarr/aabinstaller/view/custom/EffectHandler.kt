package com.namtarr.aabinstaller.view.custom

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.namtarr.aabinstaller.internal.Effect
import com.namtarr.aabinstaller.utils.openFileManager
import com.namtarr.aabinstaller.view.LocalSnackbarHostState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow

@Composable
fun EffectHandler(effects: Channel<Effect>) {
    val snackbarHostState = LocalSnackbarHostState.current

    LaunchedEffect(Unit) {
        effects.receiveAsFlow().onEach {
            when (it) {
                is Effect.OpenFileManager -> openFileManager(it.path)
                is Effect.ShowSnackbar -> snackbarHostState.showSnackbar(it.text)
            }
        }.launchIn(this)
    }
}