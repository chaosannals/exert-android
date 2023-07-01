package com.example.app24.ui.page.demo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Popup

// TODO 使用 Popup 模拟 Dialog
@Composable
fun Dialog2Page() {
    var isShow by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding()
//            .fillMaxSize(0.5f)
            .fillMaxSize()
    ) {
        Button(onClick = { isShow=true }) {
            Text(text = "显示")
        }

        if (isShow) {
            Popup(
                onDismissRequest={isShow = false},
            )
        }
    }
}