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

    private val splitRegex = "\\s".toRegex()
    private val envRegex = "\\{\\{\\S+}}".toRegex()

    override suspend fun run(command: String): Result<String> {
        return withContext(Dispatchers.IO) {
            suspendCoroutine { continuation ->
                val parts = command.split(splitRegex).map(::replaceEnvVarsIfNeeded)
                val process = ProcessBuilder(*parts.toTypedArray())
                    .redirectOutput(ProcessBuilder.Redirect.PIPE)
                    .redirectError(ProcessBuilder.Redirect.PIPE)
                    .start()
                val exitCode = process.waitFor()

                if (exitCode == 0) {
                    val result = process.inputStream.bufferedReader().readText()
                    logger.log(command, result)
                    continuation.resume(Result.Success(result))
                }
                else {
                    val result = process.errorStream.bufferedReader().readText()
                    logger.log(command, result)
                    continuation.resume(Result.commandFailure(exitCode))
                }
            }
        }
    }

    private fun replaceEnvVarsIfNeeded(part: String): String? {
        val matched = envRegex.find(part) ?: return part
        val variable = System.getenv(
            matched.value.removeSurrounding("{{", "}}")
        )
        if (variable.isNullOrBlank()) {
            return null
        }
        return part.replace(envRegex, variable)
    }
}