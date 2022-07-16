package com.namtarr.aabinstaller.view.console

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.namtarr.aabinstaller.domain.data.CommandLogger
import com.namtarr.aabinstaller.view.custom.AppBar
import dev.burnoo.cokoin.get

@Composable
fun ConsoleView(logger: CommandLogger = get()) {
    Column(modifier = Modifier.fillMaxHeight()) {
        AppBar(title = "Console")

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(32.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(MaterialTheme.colors.onSurface.copy(alpha = TextFieldDefaults.BackgroundOpacity))
        ) {
            SelectionContainer {
                Text(
                    text = logger.print(),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(8.dp)
                        .verticalScroll(ScrollState(0), reverseScrolling = true)
                )
            }
        }
    }
}