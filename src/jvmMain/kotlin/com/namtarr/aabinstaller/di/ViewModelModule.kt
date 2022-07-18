package com.namtarr.aabinstaller.di

import com.namtarr.aabinstaller.view.install.InstallViewModel
import com.namtarr.aabinstaller.view.settings.SettingsViewModel
import com.namtarr.aabinstaller.view.signing.SigningViewModel
import org.koin.dsl.module

val viewModelModule = module {
    factory { InstallViewModel(adbService = get(), bundleToolService = get(), storage = get()) }
    factory { SettingsViewModel(storage = get()) }
    factory { SigningViewModel(storage = get()) }
}