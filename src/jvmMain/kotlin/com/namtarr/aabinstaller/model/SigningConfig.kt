package com.namtarr.aabinstaller.model

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
class SigningConfig(
    @SerialName("keystore")
    val keystore: String,

    @SerialName("keystore_pass")
    val keystorePass: String,

    @SerialName("key_alias")
    val keyAlias: String,

    @SerialName("key_pass")
    val keyPass: String
) {
    override fun toString(): String {
        return "$keystore: $keyAlias"
    }
}