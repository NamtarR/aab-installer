package com.namtarr.aabinstaller.view.custom

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun AppBar(title: String, modifier: Modifier = Modifier) = androidx.compose.material.TopAppBar(
    title = {
        Text(
            title,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colors.onBackground,
            modifier = Modifier.padding(start = 16.dp)
        )
    },
    modifier = Modifier.height(72.dp).then(modifier),
    backgroundColor = MaterialTheme.colors.background,
    elevation = 0.dp
)