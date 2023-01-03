package com.namtarr.aabinstaller.view.model

import com.namtarr.aabinstaller.model.Device

sealed class DeviceOption(val device: Device?) {
    class Real(device: Device): DeviceOption(device) {
        override fun toString() = device.toString()
    }

    object Universal: DeviceOption(null) {
        override fun toString() = "Universal APK mode"
    }
}