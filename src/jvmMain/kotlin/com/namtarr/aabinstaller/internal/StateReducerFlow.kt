package com.namtarr.aabinstaller.internal

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

class StateReducerFlow<State, Event>(
    initialState: State,
    reducer: (State, Event) -> State,
    scope: CoroutineScope
) : StateFlow<State> {

    private val events = Channel<Event>()

    private val state = events.receiveAsFlow()
        .runningFold(initialState, reducer)
        .stateIn(scope, SharingStarted.Eagerly, initialState)

    override suspend fun collect(collector: FlowCollector<State>) = state.collect(collector)

    override val replayCache = state.replayCache

    override val value = state.value

    fun handleEvent(event: Event) = events.trySend(event)

    suspend fun handleEvents(list: List<Event>) = list.forEach { events.send(it) }
}