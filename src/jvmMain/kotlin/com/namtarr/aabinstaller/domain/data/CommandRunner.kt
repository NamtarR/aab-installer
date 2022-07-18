package com.namtarr.aabinstaller.domain.data

import com.namtarr.aabinstaller.model.Result

interface CommandRunner {

    suspend fun run(command: String): Result<String>
}