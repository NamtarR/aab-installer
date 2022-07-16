package com.namtarr.aabinstaller.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class Settings(
    @SerialName("adb_path")
    val adbPath: String? = null,

    @SerialName("bundletool_path")
    val bundletoolPath: String? = null
)