package com.namtarr.aabinstaller.di

import com.namtarr.aabinstaller.data.CommandRunnerImpl
import com.namtarr.aabinstaller.data.FileStorage
import com.namtarr.aabinstaller.data.InMemoryCommandLogger
import com.namtarr.aabinstaller.data.StaticServiceDiscovery
import com.namtarr.aabinstaller.domain.AdbService
import com.namtarr.aabinstaller.domain.BundleToolService
import com.namtarr.aabinstaller.domain.SigningConfigManager
import com.namtarr.aabinstaller.domain.data.CommandLogger
import com.namtarr.aabinstaller.domain.data.CommandRunner
import com.namtarr.aabinstaller.domain.data.ServiceDiscovery
import com.namtarr.aabinstaller.domain.data.Storage
import org.koin.dsl.module

val serviceModule = module {
    single<ServiceDiscovery> { StaticServiceDiscovery() }
    single<CommandRunner> { CommandRunnerImpl(logger = get()) }
    single<CommandLogger> { InMemoryCommandLogger() }
    single<Storage> { FileStorage() }

    factory { AdbService(discovery = get(), runner = get()) }
    factory { BundleToolService(discovery = get(), runner = get()) }
    factory { SigningConfigManager(storage = get()) }
}