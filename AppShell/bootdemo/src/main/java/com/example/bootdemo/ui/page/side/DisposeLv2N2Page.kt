package com.example.bootdemo.ui.page.side

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

/// key 变动会调用上次注册的 onDispose闭包 后再执行块内代码，即再次注册 onDispose闭包。
@Composable
fun DisposeLv2N2Page() {
    var key1 by remember {
        mutableStateOf(false)
    }

    var key2 by remember {
        mutableStateOf(0L)
    }

    DisposableEffect(key1, key2) {
        Log.d("DisposableEffect", "Lv2 M2 start")
        onDispose {
            Log.d("DisposableEffect", "Lv2 M2 onDispose")
        }
    }

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxSize(),
    ) {
        Button(onClick = { key1 = !key1 }) {
            Text("key1: $key1")
        }
        Button(onClick = { key2 = System.currentTimeMillis() }) {
            Text("key2: $key2")
        }
        Button(onClick = {
            key1 = !key1
            key2 = System.currentTimeMillis()
        }) {
            Text("between")
        }
    }
}

@Preview
@Composable
fun DisposeLv2N2PagePreview() {
    DisposeLv2N2Page()
}