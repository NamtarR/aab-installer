package com.namtarr.aabinstaller.model

class CommandExecutionError(
    val exitCode: Int,
    override val message: String
): Throwable()