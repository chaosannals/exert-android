package com.example.app24.ui.page.demo

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rxjava3.subscribeAsState
import com.example.app24.ScaffoldKit
import com.example.app24.X5WebViewKit

@Composable
fun WebJsSdkPage() {
    val url by X5WebViewKit.lastUrl.subscribeAsState(initial = "")

    DisposableEffect(url) {
        if (X5WebViewKit.isInited.value == true && url != "file:///android_asset/webviewcall/index.html") {
            Log.d("app24", "web-jssdk-load ${url}")
            X5WebViewKit.loadUrl("file:///android_asset/webviewcall/index.html")
        } else {
            Log.d("app24", "webView isInited == false")
        }
        ScaffoldKit.isShowWebView.onNext(true)
        onDispose {
            Log.d("app24", "web-jssdk-dispose")
            ScaffoldKit.isShowWebView.onNext(false)
        }
    }
}