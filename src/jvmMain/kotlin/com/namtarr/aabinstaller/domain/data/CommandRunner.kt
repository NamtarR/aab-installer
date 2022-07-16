package com.namtarr.aabinstaller.domain.data

interface CommandRunner {

    suspend fun run(command: String): String
}