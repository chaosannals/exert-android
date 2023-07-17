package com.example.app24.ui.page.demo

import android.util.Log
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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

    Button(onClick = { navController.navigate("disposable-page") }) {
        Text("To 1")
    }
}

@Preview
@Composable
fun Disposable2PagePreview() {
    DesignPreview {
        Disposable2Page()
    }
}