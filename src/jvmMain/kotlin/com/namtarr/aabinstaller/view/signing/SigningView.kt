package com.namtarr.aabinstaller.view.signing

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.namtarr.aabinstaller.view.custom.AppBar
import com.namtarr.aabinstaller.view.custom.EffectHandler
import com.namtarr.aabinstaller.view.custom.TextFieldButton
import com.namtarr.aabinstaller.view.external.LoadFileDialog
import dev.burnoo.cokoin.get

@Composable
@Preview
fun SigningView(viewModel: SigningViewModel = get()) {
    val state = viewModel.store.collectAsState().value
    var isFileDialogOpen by remember { mutableStateOf(false) }

    EffectHandler(viewModel.effects)

    if (isFileDialogOpen) {
        LoadFileDialog {
            isFileDialogOpen = false
            viewModel.setKeystore(it.orEmpty())
        }
    }

    Column(modifier = Modifier.fillMaxHeight()) {
        AppBar("Signing")

        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxHeight().padding(horizontal = 32.dp, vertical = 96.dp)
        ) {

            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                TextField(
                    value = state.keystore,
                    onValueChange = { viewModel.setKeystore(it) },
                    label = { Text("Keystore") },
                    modifier = Modifier.fillMaxWidth(0.6f),
                    trailingIcon = {
                        TextFieldButton(Icons.Filled.AddCircle) {
                            isFileDialogOpen = true
                        }
                    }
                )

                TextField(
                    value = state.keystorePass,
                    onValueChange = { viewModel.setKeystorePass(it) },
                    label = { Text("Keystore password") },
                    modifier = Modifier.fillMaxWidth().padding(start = 32.dp)
                )
            }

            Row(horizontalArrangement = Arrangement.SpaceBetween) {

                TextField(
                    value = state.keyAlias,
                    onValueChange = { viewModel.setKeyAlias(it) },
                    label = { Text("Key alias") },
                    modifier = Modifier.fillMaxWidth(0.5f).padding(end = 16.dp)
                )

                TextField(
                    value = state.keyPass,
                    onValueChange = { viewModel.setKeyPass(it) },
                    label = { Text("Key password") },
                    modifier = Modifier.fillMaxWidth().padding(start = 16.dp)
                )
            }

            Row(modifier = Modifier.align(Alignment.End)) {
                TextButton(
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary),
                    onClick = { viewModel.save() },
                    modifier = Modifier.padding(start = 8.dp),
                    content = { Text("Save") }
                )
            }
        }
    }
}