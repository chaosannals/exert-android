package com.example.bootdemo.ui.page.ssh

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SshPage() {
    val coroutineScope = rememberCoroutineScope()

    var authInfo by remember {
        mutableStateOf(SshAuthInfo())
    }

    val controller = rememberSshController()
    val isAlive by controller.alive.observeAsState(false)


    var authInfoDialogVisible by remember {
        mutableStateOf(false)
    }

    var message by remember {
        mutableStateOf("")
    }
    var content by remember {
        mutableStateOf("")
    }

    val consoleController = rememberSshConsoleController {
        message += it
    }

    SshAuthInfoDialog(
        visible = authInfoDialogVisible,
        authInfo = authInfo,
        onDismissRequest = { authInfoDialogVisible=false },
        onConfirm = { authInfo = it }
    )

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxSize()
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
        ){
            Button(
                onClick = {
                    if (isAlive) {

                    } else {
                        controller.openSession(authInfo)
                        controller.openConsole(consoleController)
                    }
                }
            ) {
                Text(text = if (isAlive) "关闭" else "启动")
            }
        }
        Text(
            text = buildAnnotatedString {
                append(message)
            },
            color = Color.White,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(Color.Black)
        )

        TextField(
            label = { Text("命令:") },
            value = content,
            onValueChange = {content = it},
            keyboardActions = KeyboardActions(
                onDone = {
                    val command = content

                    content = ""
                }
            ),
            singleLine = false,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@Preview
@Composable
fun SshPagePreview() {
    SshPage()
}