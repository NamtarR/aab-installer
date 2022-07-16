package com.namtarr.aabinstaller.domain.data

interface ServiceDiscovery {

    suspend fun getAdbPath(): String?

    suspend fun getBundletoolPath(): String?

    suspend fun updatePaths(adbPath: String?, bundletoolPath: String?)
}