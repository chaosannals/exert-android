package com.example.bootdemo.ui.page.ssh

import androidx.compose.runtime.*
import kotlinx.coroutines.DisposableHandle
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

@Composable
fun rememberSshConsoleController(
    onReadLine: suspend (String) -> Unit
) : SshConsoleController {
    val controller = remember {
        SshConsoleController(
            onReadLine=onReadLine,
        )
    }
    return controller
}

class SshConsoleController(
    val onReadLine: suspend (String) -> Unit,
    private val writeFlow: MutableSharedFlow<String> = MutableSharedFlow()
) : DisposableHandle {
    val writer: SharedFlow<String> get() = writeFlow

    suspend fun execute(command: String) {
        writeFlow.emit(command)
    }

    override fun dispose() {
        //
    }
}