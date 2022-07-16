package com.namtarr.aabinstaller.view.signing

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.namtarr.aabinstaller.view.custom.AppBar
import dev.burnoo.cokoin.get

@Composable
@Preview
fun SigningView(viewModel: SigningViewModel = get()) {

    Column(modifier = Modifier.fillMaxHeight()) {
        AppBar("Signing")

        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxHeight().padding(horizontal = 32.dp, vertical = 96.dp)
        ) {

            var s by remember { mutableStateOf("") }

            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                TextField(
                    value = s,
                    onValueChange = { s = it },
                    label = { Text("Keystore") },
                    modifier = Modifier.fillMaxWidth(0.6f)
                )

                TextField(
                    value = s,
                    onValueChange = { s = it },
                    label = { Text("Keystore password") },
                    modifier = Modifier.fillMaxWidth().padding(start = 32.dp)
                )
            }

            Row(horizontalArrangement = Arrangement.SpaceBetween) {

                TextField(
                    value = "",
                    onValueChange = { /*viewModel.setBundlePath(it)*/ },
                    label = { Text("Key alias") },
                    modifier = Modifier.fillMaxWidth(0.5f).padding(end = 16.dp)
                )

                TextField(
                    value = "",
                    onValueChange = { /*viewModel.setBundlePath(it)*/ },
                    label = { Text("Key password") },
                    modifier = Modifier.fillMaxWidth().padding(start = 16.dp)
                )
            }

            Row(modifier = Modifier.align(Alignment.End)) {
                TextButton(
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary),
                    onClick = {  },
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text("Save")
                }
            }
        }
    }
}