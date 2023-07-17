package com.example.app24.ui.page.demo

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.app24.ui.DesignPreview
import com.example.app24.ui.LocalNavController

@Composable
fun Disposable2Page() {
    val navController = LocalNavController.current
    Log.d("app24", "Disposable2Page Compose")

    DisposableEffect(Unit) {
        Log.d("app24", "Disposable2Page Effect")
        onDispose {
            Log.d("app24", "Disposable2Page onDispose")
        }
    }
    Column(
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxSize()
    ) {
        Button(onClick = { navController.navigate("disposable-2-page") }) {
            Text("To Self")
        }
        Button(onClick = { navController.navigate("disposable-page") }) {
            Text("To 1")
        }
    }
}

@Preview
@Composable
fun Disposable2PagePreview() {
    DesignPreview {
        Disposable2Page()
    }
}