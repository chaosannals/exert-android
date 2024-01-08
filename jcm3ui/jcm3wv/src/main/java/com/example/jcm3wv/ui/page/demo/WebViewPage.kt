package com.example.jcm3wv.ui.page.demo

import android.annotation.SuppressLint
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.isDebugInspectorInfoEnabled
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jcm3wv.ui.widget.JcWebView

// 在 JS 不修改 history 历史的情况下，修改 webClient 后 canGoBack 返回是正常。
// 和 vue 的区别在于 state 是 null ，而修改了历史 state 是被使用的。
// TODO 测试 vue 这种修改 历史的

@SuppressLint("SetJavaScriptEnabled")
@Preview
@Composable
fun WebViewPage() {
    val context = LocalContext.current

    val webView by remember(context) {
        derivedStateOf {
            WebView(context).apply {
                settings.apply {
                    javaScriptEnabled = true
                    isDebugInspectorInfoEnabled = true
                }

                webViewClient = object : WebViewClient() {

                }
                webChromeClient = object: WebChromeClient() {

                }
            }
        }
    }

    LaunchedEffect(Unit) {
        webView.loadUrl("file:///android_asset/webroot/index.html")
    }

    Box(
        contentAlignment= Alignment.Center,
        modifier = Modifier,
    ) {
        JcWebView(
            webView = webView,
            modifier = Modifier
                .fillMaxSize()
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 10.dp, end = 10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color.Red)
                    .clickable {
                        val cgb = webView.canGoBack()
                        Toast.makeText(context, "canGoBack: $cgb", Toast.LENGTH_SHORT).show()
                    }
            )

            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color.Gray)
                    .clickable {
                        webView.webViewClient = object : WebViewClient() {}
                        webView.webChromeClient = object : WebChromeClient() {}
                    }
            )
        }
    }
}