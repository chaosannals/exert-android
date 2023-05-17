package com.example.app24.ui.page.demo

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.app24.ScaffoldKit
import com.example.app24.X5WebViewKit
import com.example.app24.ui.DesignPreview

@Composable
fun WebFirstPage() {
    val url by X5WebViewKit.lastUrl.subscribeAsState(initial = "")

    Column (
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxSize(),
    ) {
        Text("web-first-page")
    }

    DisposableEffect(url) {
        if (X5WebViewKit.isInited.value == true && url != "https://m.bilibili.com") {
            Log.d("app24", "web-1-load ${url}")
            X5WebViewKit.loadUrl("https://m.bilibili.com")
        } else {
            Log.d("app24", "webView isInited == false")
        }
        ScaffoldKit.isShowWebView.onNext(true)
        onDispose {
            Log.d("app24", "web-1-dispose")
            ScaffoldKit.isShowWebView.onNext(false)
        }
    }
}

@Preview
@Composable
fun WebFirstPagePreview() {
    DesignPreview {
        WebFirstPage()
    }
}