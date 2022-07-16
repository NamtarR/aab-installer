package com.namtarr.aabinstaller.internal

sealed interface Effect {
    class ShowSnackbar(val text: String) : Effect
    class OpenFileManager(val path: String) : Effect
}