package com.namtarr.aabinstaller.view

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.namtarr.aabinstaller.di.serviceModule
import com.namtarr.aabinstaller.di.viewModelModule
import com.namtarr.aabinstaller.view.build.BuildView
import com.namtarr.aabinstaller.view.console.ConsoleView
import com.namtarr.aabinstaller.view.settings.SettingsView
import com.namtarr.aabinstaller.view.signing.SigningView
import dev.burnoo.cokoin.Koin

enum class Screen(val title: String, val icon: ImageVector) {
    BUILD("Build", Icons.Filled.Build),
    SIGNING("Signing", Icons.Filled.Info),
    SETTINGS("Settings", Icons.Filled.Settings),
    CONSOLE("Console", Icons.Filled.PlayArrow)
}

val LocalSnackbarHostState = staticCompositionLocalOf { SnackbarHostState() }

@Composable
@Preview
fun App() {
    var selected by remember { mutableStateOf(0) }
    val screens = Screen.values()

    Koin(appDeclaration = { modules(serviceModule, viewModelModule) }) {
        MaterialTheme(colors = colors, typography = typography) {
            Row {
                MainMenu(
                    screens = screens,
                    selected = selected,
                    onSelected = { selected = it }
                )
                MainContent(
                    screens = screens,
                    selected = selected,
                    onSelected = { selected = it }
                )
            }
        }
    }
}

@Composable
fun MainMenu(screens: Array<Screen>, selected: Int, onSelected: (Int) -> Unit) {
    NavigationRail(
        backgroundColor = MaterialTheme.colors.primary
    ) {
        Spacer(modifier = Modifier.weight(1f))
        screens.forEachIndexed { index, screen ->
            NavigationRailItem(
                selected = selected == index,
                label = { Text(screen.title) },
                icon = { Icon(screen.icon, contentDescription = "") },
                onClick = { onSelected.invoke(index) },
                unselectedContentColor = MaterialTheme.colors.onPrimary,
                selectedContentColor = MaterialTheme.colors.secondary
            )
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun MainContent(screens: Array<Screen>, selected: Int, onSelected: (Int) -> Unit) {
    val snackbarHostState = remember { SnackbarHostState() }

    Box {
        CompositionLocalProvider(LocalSnackbarHostState provides snackbarHostState) {
            when (screens[selected]) {
                Screen.BUILD -> BuildView { onSelected.invoke(Screen.SIGNING.ordinal) }
                Screen.SIGNING -> SigningView()
                Screen.SETTINGS -> SettingsView()
                Screen.CONSOLE -> ConsoleView()
            }
        }
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .height(64.dp)
                .fillMaxWidth(0.6f)
                .align(Alignment.BottomCenter)
        )
    }
}