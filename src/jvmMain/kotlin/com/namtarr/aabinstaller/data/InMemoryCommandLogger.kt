package com.namtarr.aabinstaller.data

import com.namtarr.aabinstaller.domain.data.CommandLogger

class InMemoryCommandLogger : CommandLogger {

    private var log = "Console output:"

    override fun log(command: String, result: String) {
        log = buildString {
            append(log)
            append("\n\n> ")
            append(command)
            append("\n")
            append(result.replace("\t", "    "))
        }
    }

    override fun error(command: String, error: String) {
        log = buildString {
            append(log)
            append("\n\n> ")
            append(command)
            append("\n")
            append(error.replace("\t", "    "))
        }
    }

    override fun print(): String {
        return log
    }
}