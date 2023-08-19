package com.example.bootdemo.ui.page.side

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun DisposeLv2Page() {
    DisposableEffect(Unit) {
        Log.d("DisposableEffect", "Lv2 start")
        onDispose {
            Log.d("DisposableEffect", "Lv2 onDispose")
        }
    }
}

@Preview
@Composable
fun DisposeLv2PagePreview() {
    DisposeLv2Page()
}