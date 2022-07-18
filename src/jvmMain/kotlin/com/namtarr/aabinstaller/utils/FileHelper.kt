package com.namtarr.aabinstaller.utils

fun openFileManager(path: String) = runCatching {
    val platform = System.getProperty("os.name").lowercase()
    when {
        platform.contains("win") ->
            Runtime.getRuntime().exec("explorer.exe /select,$path")
        listOf("nix", "nux", "aix").any { platform.contains(it) } ->
            {}
        listOf("mac", "darwin").any { platform.contains(it) } ->
            {}
        else -> {
            // No known OS detected ¯\_(ツ)_/¯
        }
    }
}