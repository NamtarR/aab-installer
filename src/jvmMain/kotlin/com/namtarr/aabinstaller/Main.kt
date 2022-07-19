import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import com.namtarr.aabinstaller.view.App

fun main() = application {
    Window(
        state = WindowState(width = 960.dp, height = 600.dp),
        onCloseRequest = ::exitApplication,
        title = "aab-installer",
        resizable = false
    ) {
        App()
    }
}
