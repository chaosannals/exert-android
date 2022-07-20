package com.example.jcmdemo.ui.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.example.jcmdemo.R

@Composable
fun HomePage () {
    var isShowDialog by remember { mutableStateOf(false) }

    if (isShowDialog) {
        AlertDialog(
            onDismissRequest = {
                isShowDialog = false
            },

            title = {
                Text(text = "标题")
            },
            text = {
                Text(text = "副标题")
            },

            confirmButton = {
                TextButton(
                    onClick = {
                        isShowDialog = false
                    },
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    Text("按钮")
                }
            },

            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false,
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Button(onClick = {
            isShowDialog = !isShowDialog
        }) {
            Text(text = if (isShowDialog) { "隐藏" }  else { "显示" })
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePagePreview() {
    HomePage()
}