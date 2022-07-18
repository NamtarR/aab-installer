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

    private val name = keystore.split(splitRegex).lastOrNull().orEmpty()
    override fun toString(): String {
        return "$name: $keyAlias"
    }

    companion object {
        private val splitRegex = "[/\\\\]".toRegex()
    }
}