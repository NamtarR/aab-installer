package com.namtarr.aabinstaller.view.external

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.AwtWindow
import java.awt.FileDialog
import java.awt.Frame

@Composable
fun LoadFileDialog(
    parent: Frame? = null,
    title: String = "Select a file",
    allowedExtensions: List<String> = emptyList(),
    onCloseRequest: (result: String?) -> Unit
) = AwtWindow(
    create = {
        object : FileDialog(parent, title, LOAD) {

            init {
                file = allowedExtensions.joinToString(";") { "*.$it" }
            }

            override fun setVisible(value: Boolean) {
                super.setVisible(value)
                if (value) {
                    onCloseRequest(files.firstOrNull()?.absolutePath)
                }
            }
        }
    },
    dispose = FileDialog::dispose
)