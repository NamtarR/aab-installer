package com.namtarr.aabinstaller.view.custom

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerIconDefaults
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TextFieldButton(
    imageVector: ImageVector,
    onClick: () -> Unit
) = Button(
    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
    shape = CircleShape,
    elevation = ButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp),
    contentPadding = PaddingValues(0.dp),
    modifier = Modifier.size(32.dp).pointerHoverIcon(PointerIconDefaults.Default),
    onClick = onClick
) {
    Icon(
        imageVector = imageVector,
        tint = MaterialTheme.colors.primary,
        contentDescription = ""
    )
}