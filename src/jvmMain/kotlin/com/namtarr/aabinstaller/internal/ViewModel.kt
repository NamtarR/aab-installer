package com.namtarr.aabinstaller.internal

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

abstract class ViewModel {

    private val viewModelJob = SupervisorJob()
    val viewModelScope = CoroutineScope(Dispatchers.Default + viewModelJob)

}