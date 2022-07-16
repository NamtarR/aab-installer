package com.namtarr.aabinstaller.internal

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

abstract class ViewModel {

    val viewModelJob = SupervisorJob()
    val viewModelScope = CoroutineScope(Dispatchers.Default + viewModelJob)

    fun <State, Event> Store(
        initialState: State,
        reducer: (State, Event) -> State
    ) = StateReducerFlow(initialState, reducer, viewModelScope)
}