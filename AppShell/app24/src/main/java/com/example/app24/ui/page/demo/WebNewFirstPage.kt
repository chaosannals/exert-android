package com.example.app24.ui.page.demo

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.app24.X5WebViewKit
import com.example.app24.ui.DesignPreview
import com.example.app24.ui.widget.X5WebView

@Composable
fun WebNewFirstPage() {
    val url by X5WebViewKit.lastUrl.subscribeAsState(initial = "")

//    LaunchedEffect(Unit) {
//        if (X5WebViewKit.isInited.value == true && url != "https://m.bilibili.com") {
//            Log.d("app24", "web-new-1-load ${url}")
//            X5WebViewKit.loadUrl("https://m.bilibili.com")
//        } else {
//            Log.d("app24", "webView new 1 isInited == false")
//        }
//    }
//    // AndroidView 的 factory 返回的对象必须通过父级 removeView 才能重新被挂载到新的 AndroidView 上。
//    X5WebView(
//        modifier = Modifier
//            .fillMaxSize()
//    )
}

@Preview
@Composable
fun WebNewFirstPagePreview() {
    DesignPreview {
        WebNewFirstPage()
    }
}