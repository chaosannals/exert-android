package com.example.app24.ui.page.demo

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.app24.ScaffoldKit
import com.example.app24.X5WebViewKit
import com.example.app24.ui.DesignPreview

@Composable
fun WebFirstPage() {

    Column (
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxSize(),
    ) {
        Text("web-first-page")
    }

    DisposableEffect(Unit) {
        if (X5WebViewKit.isInited.value == true) {
            X5WebViewKit.webView.loadUrl("https://m.bilibili.com")
        } else {
            Log.d("app24", "webView isInited == false")
        }
        ScaffoldKit.isShowWebView.onNext(true)
        onDispose {
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