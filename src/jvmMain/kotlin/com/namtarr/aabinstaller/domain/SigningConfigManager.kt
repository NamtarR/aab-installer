package com.namtarr.aabinstaller.domain

import com.namtarr.aabinstaller.domain.data.Storage
import com.namtarr.aabinstaller.model.SigningConfig

class SigningConfigManager(
    private val storage: Storage
) {

    suspend fun getConfigs(): List<SigningConfig> {
        return listOf(
            SigningConfig(
                "staply.jks",
                "vault13online",
                "pachca",
                "vault15online"
            ),
            SigningConfig(
                "rt_academy.jks",
                "FTBRnp7ycGjzLM26fKPXmfcV",
                "delo",
                "iKVepaZ3N4qWtmHDYjft6iUB"
            )
        )
    }

    suspend fun createConfig(): Boolean {
        return true
    }
}