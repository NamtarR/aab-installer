package com.namtarr.aabinstaller.di

import com.namtarr.aabinstaller.data.CommandRunnerImpl
import com.namtarr.aabinstaller.data.LocalFileStorage
import com.namtarr.aabinstaller.data.InMemoryCommandLogger
import com.namtarr.aabinstaller.domain.AdbService
import com.namtarr.aabinstaller.domain.BundleToolService
import com.namtarr.aabinstaller.domain.data.CommandLogger
import com.namtarr.aabinstaller.domain.data.CommandRunner
import com.namtarr.aabinstaller.domain.data.Storage
import org.koin.dsl.module

val serviceModule = module {
    single<CommandRunner> { CommandRunnerImpl(logger = get()) }
    single<CommandLogger> { InMemoryCommandLogger() }
    single<Storage> { LocalFileStorage() }

    factory { AdbService(storage = get(), runner = get()) }
    factory { BundleToolService(storage = get(), runner = get()) }
}