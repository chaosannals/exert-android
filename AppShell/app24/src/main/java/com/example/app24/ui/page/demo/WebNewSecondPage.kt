package com.example.app24.ui.page.demo

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.app24.X5WebViewKit
import com.example.app24.ui.DesignPreview
import com.example.app24.ui.widget.X5WebView

@Composable
fun WebNewSecondPage() {
    val url by X5WebViewKit.lastUrl.subscribeAsState(initial = "")

    LaunchedEffect(Unit) {
        if (X5WebViewKit.isInited.value == true && url != "https://m.baidu.com") {
            Log.d("app24", "web-new-2-load ${url}")
            X5WebViewKit.loadUrl("https://m.baidu.com")
        } else {
            Log.d("app24", "webView new 2 isInited == false")
        }
    }

    X5WebView(
        modifier = Modifier
            .fillMaxSize()
    )
}

@Preview
@Composable
fun WebNewSecondPagePreview() {
    DesignPreview {
        WebNewSecondPage()
    }
}