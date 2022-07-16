package com.namtarr.aabinstaller.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import java.io.IOException
import java.util.concurrent.TimeUnit

fun String.runCommand(): String {
    return try {
        val parts = this.split("\\s".toRegex())
        val process = ProcessBuilder(*parts.toTypedArray())
            .redirectOutput(ProcessBuilder.Redirect.PIPE)
            .redirectError(ProcessBuilder.Redirect.PIPE)
            .start()
        println(process.waitFor())
        process.inputStream.bufferedReader().readText()
    } catch (e: IOException) {
        e.printStackTrace()
        ""
    }
}

suspend fun <T : Any> Flow<T?>.awaitNonNullValue(): T {
    return filterNotNull().first()
}