package com.namtarr.aabinstaller.view.install

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.namtarr.aabinstaller.view.custom.AppBar
import com.namtarr.aabinstaller.view.custom.EffectHandler
import com.namtarr.aabinstaller.view.custom.Spinner
import com.namtarr.aabinstaller.view.custom.TextFieldButton
import com.namtarr.aabinstaller.view.external.LoadFileDialog
import dev.burnoo.cokoin.get

@Composable
@Preview
fun InstallView(viewModel: InstallViewModel = get(), onAddNewConfig: () -> Unit) {
    val state = viewModel.store.collectAsState().value
    val canBuild = state.bundlePath.isNotBlank() && state.signingConfigs.isNotEmpty() && !state.isLoading
    val canInstall = state.devices.isNotEmpty() && state.selectedDevice < state.devices.size && !state.isLoading
    var isFileDialogOpen by remember { mutableStateOf(false) }

    EffectHandler(viewModel.effects)

    if (isFileDialogOpen) {
        LoadFileDialog(allowedExtensions = listOf("aab")) {
            isFileDialogOpen = false
            viewModel.setBundlePath(it.orEmpty())
        }
    }

    Column(modifier = Modifier.fillMaxHeight()) {
        Box(modifier = Modifier.height(76.dp)) {
            AppBar("Build & Install")
            if (state.isLoading) {
                LinearProgressIndicator(
                    color = MaterialTheme.colors.secondary,
                    modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter)
                )
            }
        }

        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 32.dp, vertical = 96.dp).fillMaxHeight()
        ) {

            TextField(
                value = state.bundlePath,
                onValueChange = { viewModel.setBundlePath(it) },
                label = { Text("App Bundle") },
                trailingIcon = {
                    TextFieldButton(Icons.Filled.AddCircle) {
                        isFileDialogOpen = true
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
            )

            Row(modifier = Modifier.padding(bottom = 16.dp)) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .padding(end = 12.dp)
                ) {
                    Spinner(
                        data = state.devices,
                        selected = state.selectedDevice,
                        onClick = { viewModel.selectDevice(it) },
                        label = "Device"
                    )
                    TextButton(
                        onClick = { viewModel.loadDevices() },
                        modifier = Modifier.align(Alignment.End),
                    ) {
                        Icon(Icons.Filled.Refresh, contentDescription = "", modifier = Modifier.size(20.dp))
                        Text("Refresh", modifier = Modifier.padding(start = 8.dp))
                    }
                }

                Column(modifier = Modifier.padding(start = 12.dp)) {
                    Spinner(
                        data = state.signingConfigs,
                        selected = state.selectedSigningConfig,
                        onClick = { viewModel.selectConfig(it) },
                        label = "Signing config"
                    )
                    TextButton(
                        onClick = { onAddNewConfig.invoke() },
                        modifier = Modifier.align(Alignment.End),
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = "", modifier = Modifier.size(20.dp))
                        Text("Add new", modifier = Modifier.padding(start = 8.dp))
                    }
                }
            }

            Row(modifier = Modifier.align(Alignment.End)) {
                OutlinedButton(
                    onClick = { viewModel.build() },
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colors.secondary),
                    modifier = Modifier.padding(end = 8.dp),
                    enabled = canBuild
                ) {
                    Text("Build")
                }
                TextButton(
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary),
                    onClick = { viewModel.buildInstall() },
                    enabled = canBuild && canInstall,
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text("Build & Install")
                }
            }
        }
    }
}