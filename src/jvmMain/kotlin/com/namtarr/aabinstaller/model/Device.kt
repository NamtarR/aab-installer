package com.namtarr.aabinstaller.model

class Device(val id: String, val model: String) {

    override fun toString(): String {
        return "$model: $id"
    }
}