package com.namtarr.aabinstaller.domain.data

interface CommandLogger {

    fun log(command: String, result: String)

    fun error(command: String, error: String)

    fun print(): String
}