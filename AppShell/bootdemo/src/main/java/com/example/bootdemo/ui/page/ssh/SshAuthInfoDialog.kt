package com.example.bootdemo.ui.page.ssh

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.net.toFile

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SshAuthInfoDialog(
    visible: Boolean,
    authInfo: SshAuthInfo,
    onDismissRequest: () -> Unit,
    onConfirm: (authInfo: SshAuthInfo) -> Unit,
) {
    if (visible) {
        var host by remember(authInfo) {
            mutableStateOf(authInfo.host)
        }
        var port by remember(authInfo) {
            mutableStateOf(authInfo.port)
        }
        var user by remember(authInfo) {
            mutableStateOf(authInfo.user)
        }
        var password by remember(authInfo) {
            mutableStateOf(authInfo.password)
        }
        var privateKey by remember(authInfo) {
            mutableStateOf(authInfo.privateKey)
        }
        val fileLoader = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()) {
            privateKey = it?.toFile()?.readText()
        }

        Dialog(
            onDismissRequest = onDismissRequest
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .heightIn(min = 400.dp)
                    .background(Color.White)
            ) {
                TextField(
                    label = {Text("Host")},
                    value = host,
                    onValueChange = {host = it}
                )
                TextField(
                    label = {Text("端口")},
                    value = port.toString(),
                    onValueChange = {port = it.toInt()}
                )
                TextField(
                    label = {Text("用户名")},
                    value = user,
                    onValueChange = {user = it}
                )
                TextField(
                    label = {Text("密码")},
                    value = password ?: "",
                    onValueChange = { password = it}
                )
                Button(onClick = { fileLoader.launch("*") }) {
                    Text(text = "选择密钥")
                }
                Button(
                    onClick =
                    {
                        onConfirm(
                            SshAuthInfo(
                                host = host,
                                port = port,
                                user = user,
                                password = password,
                                privateKey = privateKey,
                            )
                        )
                    }
                ) {
                    Text(text = "确认")
                }
            }
        }
    }
}

@Preview
@Composable
fun SshAuthInfoDialogPreview() {
    var authInfo by remember {
        mutableStateOf(SshAuthInfo())
    }
    var authInfoDialogVisible by remember {
        mutableStateOf(true)
    }
    SshAuthInfoDialog(
        visible = authInfoDialogVisible,
        authInfo = authInfo,
        onDismissRequest = {authInfoDialogVisible = false},
        onConfirm = { authInfo = it }
    )
}