package com.namtarr.aabinstaller.model

sealed interface Result<T> {
    class Success<T>(val data: T): Result<T>

    sealed interface Failure<T>: Result<T> {
        class Service<T>(val service: String): Failure<T>
        class Command<T>(val exitCode: Int): Failure<T>
    }

    suspend fun <R> map(block: suspend (T) -> R): Result<R> {
        @Suppress("UNCHECKED_CAST")
        return when (this) {
            is Failure -> this as Result<R>
            is Success -> Success(block.invoke(data))
        }
    }

    suspend fun <R> flatMap(block: suspend (T) -> Result<R>): Result<R> {
        @Suppress("UNCHECKED_CAST")
        return when (this) {
            is Failure -> this as Result<R>
            is Success -> block.invoke(data)
        }
    }

    fun on(success: (T) -> Unit, serviceFailure: ((String) -> Unit)? = null, commandFailure: ((Int) -> Unit)? = null) {
        when (this) {
            is Success -> success.invoke(data)
            is Failure.Service -> serviceFailure?.invoke(service)
            is Failure.Command -> commandFailure?.invoke(exitCode)
        }
    }

    companion object {
        fun <T> serviceFailure(service: String) = Failure.Service<T>(service)
        fun <T> commandFailure(exitCode: Int) = Failure.Command<T>(exitCode)
    }
}