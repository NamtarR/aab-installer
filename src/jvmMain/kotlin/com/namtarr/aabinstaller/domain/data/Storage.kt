package com.namtarr.aabinstaller.domain.data

import com.namtarr.aabinstaller.model.Settings
import com.namtarr.aabinstaller.model.SigningConfig

interface Storage {
    suspend fun getSigningConfigs(): List<SigningConfig>

    suspend fun saveSigningConfig(config: SigningConfig)

    suspend fun getServiceSettings(): Settings

    suspend fun saveServiceSettings(newSettings: Settings)
}