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

/// Compose 组件在切换时，旧组件是在新组件 Compose 后再调用 onDispose
/// 进入 DisposablePage 再切换 Disposable2Page 的顺序是即：
/// 1. DisposablePage Compose
/// 2. Disposable2Page Compose
/// 3. DisposablePage onDispose
/// 路由切换自身也是如此，即：
/// 1. DisposablePage Compose
/// 2. DisposablePage Compose
/// 3. DisposablePage onDispose

@Composable
fun DisposablePage() {
    val navController = LocalNavController.current
    Log.d("app24", "DisposablePage Compose")

    DisposableEffect(Unit) {
        Log.d("app24", "DisposablePage Effect")
        onDispose {
            Log.d("app24", "DisposablePage onDispose")
        }
    }

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxSize()
    ) {
        Button(onClick = { navController.navigate("disposable-page") }) {
            Text("To Self")
        }
        Button(onClick = { navController.navigate("disposable-2-page") }) {
            Text("To 2")
        }
    }
}

@Preview
@Composable
fun DisposablePagePreview() {
    DesignPreview {
        DisposablePage()
    }
}