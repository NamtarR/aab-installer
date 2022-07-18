package com.namtarr.aabinstaller.data

import com.namtarr.aabinstaller.domain.data.CommandLogger
import com.namtarr.aabinstaller.domain.data.CommandRunner
import com.namtarr.aabinstaller.model.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class CommandRunnerImpl(
    private val logger: CommandLogger
) : CommandRunner {

    override suspend fun run(command: String): Result<String> {
        return withContext(Dispatchers.IO) {
            suspendCoroutine {
                val parts = command.split("\\s".toRegex())
                val process = ProcessBuilder(*parts.toTypedArray())
                    .redirectOutput(ProcessBuilder.Redirect.PIPE)
                    .redirectError(ProcessBuilder.Redirect.PIPE)
                    .start()
                val exitCode = process.waitFor()

                if (exitCode == 0) {
                    val result = process.inputStream.bufferedReader().readText()
                    logger.log(command, result)
                    it.resume(Result.Success(result))
                }
                else {
                    val result = process.errorStream.bufferedReader().readText()
                    logger.log(command, result)
                    it.resume(Result.commandFailure(exitCode))
                }
            }
        }
    }
}