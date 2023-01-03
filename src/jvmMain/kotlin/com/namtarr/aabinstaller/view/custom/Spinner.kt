package com.namtarr.aabinstaller.view.custom

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp

@Composable
fun <T> Spinner(
    data: List<T>,
    selected: Int,
    transform: (T) -> String = { it.toString() },
    label: String,
    modifier: Modifier = Modifier,
    onClick: (Int) -> Unit
) {
    var width by remember { mutableStateOf(0) }
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused = interactionSource.collectIsFocusedAsState()
    val focusManager = LocalFocusManager.current

    Box(modifier) {
        val selectedText = if (selected < data.size) transform.invoke(data[selected]) else ""
        TextField(
            value = selectedText,
            onValueChange = { },
            label = { Text(label) },
            singleLine = true,
            readOnly = true,
            trailingIcon = { Icon(Icons.Filled.ArrowDropDown, contentDescription = "") },
            interactionSource = interactionSource,
            modifier = Modifier
                .fillMaxWidth()
                .onSizeChanged { width = it.width }
        )
        DropdownMenu(
            expanded = isFocused.value,
            onDismissRequest = { focusManager.clearFocus() },
            modifier = Modifier.width(width.dp / 3 * 2) // it's a kind of magic
        ) {
            data.forEachIndexed { index, entry ->
                DropdownMenuItem(
                    onClick = {
                        onClick.invoke(index)
                        focusManager.clearFocus()
                    }
                ) {
                    Text(transform.invoke(entry))
                }
            }
        }
    }
}