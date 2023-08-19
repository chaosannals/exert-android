package com.example.bootdemo.ui.page.side

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun DisposeLv1Page() {
    Column(
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxSize(),
    ) {
        EffectButton("dispose lv2", "dispose-lv2")
        EffectButton("dispose lv2 n2", "dispose-lv2-n2")
    }
    DisposableEffect(Unit) {
        Log.d("DisposableEffect", "Lv1 start")
        onDispose {
            Log.d("DisposableEffect", "Lv1 onDispose")
        }
    }
}

@Preview
@Composable
fun DisposeLv1PagePreview() {
    DisposeLv1Page()
}