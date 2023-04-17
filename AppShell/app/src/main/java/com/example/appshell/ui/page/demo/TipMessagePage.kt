package com.example.appshell.ui.page.demo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.appshell.ui.LocalTipQueue
import com.example.appshell.ui.tip
import com.example.appshell.ui.widget.DesignPreview
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun TipMessagePage() {
    val tipQueue = LocalTipQueue.current
    val coroutineScope = rememberCoroutineScope()
    var text by remember { mutableStateOf("12345") }

    Column (
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {
        TextField(value = text, onValueChange = {text = it})
        Button(onClick = { tipQueue.tip(text) }) {
            Text("提示")
        }

        Button(
            onClick =
            {
                coroutineScope.launch(Dispatchers.IO) {
//                    delay(1000)
//                    tipQueue.tip(text)
//                    delay(1000)
//                    tipQueue.tip(text)
//                    delay(1000)
                    tipQueue.tip("1: ${text}")
                    tipQueue.tip("2: ${text}")
                    tipQueue.tip("3: ${text}")
                }
            },
        ) {
            Text("连发提示")
        }
    }
}

@Preview
@Composable
fun TipMessagePagePreview() {
    DesignPreview {
        TipMessagePage()
    }
}