package com.namtarr.aabinstaller.view.settings

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.namtarr.aabinstaller.view.custom.AppBar
import dev.burnoo.cokoin.get

@Composable
@Preview
fun SettingsView(viewModel: SettingsViewModel = get()) {
    val state = viewModel.state.collectAsState().value
    val canSave = state.adbPath != null && state.bundletoolPath != null

    Column(modifier = Modifier.fillMaxHeight()) {
        AppBar("Settings")

        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxHeight().padding(horizontal = 32.dp, vertical = 96.dp)
        ) {

            TextField(
                value = state.adbPath.orEmpty(),
                onValueChange = { viewModel.setAdbPath(it) },
                label = { Text("ADB") },
                modifier = Modifier.fillMaxWidth()
            )

            TextField(
                value = state.bundletoolPath.orEmpty(),
                onValueChange = { viewModel.setBundletoolPath(it) },
                label = { Text("Bundletool") },
                modifier = Modifier.fillMaxWidth()
            )

            Row(modifier = Modifier.align(Alignment.End)) {
                TextButton(
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary),
                    onClick = { viewModel.save() },
                    enabled = canSave
                ) {
                    Text("Save")
                }
            }
        }
    }
}