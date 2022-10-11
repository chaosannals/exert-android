package com.example.jcmdemo.ui.page

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.webkit.WebResourceRequest
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.example.jcmdemo.ui.sdp
import com.tencent.smtt.sdk.WebChromeClient
import com.tencent.smtt.sdk.WebSettings
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient

@Composable
fun WebViewX5Box() {
    var text by remember {
        mutableStateOf("https://m.bilibili.com")
    }
    var progress by remember {
        mutableStateOf(0)
    }
    var webview: WebView? by remember {
        mutableStateOf(null)
    }
    val webviewClient = object: WebViewClient() {
        override fun shouldOverrideUrlLoading(wv: WebView?, url: String?): Boolean {
            if(null == url) return false
            try {
                if (!url.startsWith("http://")
                    && !url.startsWith("https://")) {
                    //处理非http和https开头的链接地址
                    Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        wv?.context?.applicationContext?.startActivity(this)
                    }
                    return true
                }
            }catch (e:Exception){
                Log.e("webview","url: ${url} ${e}")
                //没有安装和找到能打开(「xxxx://openlink.cc....」、「weixin://xxxxx」等)协议的应用
                return true
            }
            return super.shouldOverrideUrlLoading(wv, url)
        }
    }
    val webchromeClient = object: WebChromeClient() {

    }

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            TextField(
                value = text,
                onValueChange = { text = it },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "浏览",
                        tint = Color.Gray,
                        modifier = Modifier
                            .size(30.sdp)
                            .aspectRatio(1.0f)
                            .clickable {
                                webview?.let {
                                    if (text.isNotEmpty()) {
                                        it.loadUrl(text)
                                    }
                                }
                            },
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
        Box(
            contentAlignment = Alignment.TopStart,
            modifier = Modifier
                .height(1.sdp)
                .fillMaxWidth()
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth((progress / 100).toFloat())
                    .background(Color.Blue)
            )
        }

        AndroidView(
            factory = { WebView(it) },
            modifier = Modifier
                .fillMaxSize()
        ) {
            it.webViewClient = webviewClient
            it.webChromeClient = webchromeClient
            it.settings.apply {
                //支持js交互
                javaScriptEnabled = true
                //将图片调整到适合webView的大小
                useWideViewPort = true
                //缩放至屏幕的大小
                loadWithOverviewMode = true
                //缩放操作
                setSupportZoom(true)
                builtInZoomControls = true
                displayZoomControls = true
                //是否支持通过JS打开新窗口
                javaScriptCanOpenWindowsAutomatically = true
                //不加载缓存内容
                cacheMode = WebSettings.LOAD_NO_CACHE

                mixedContentMode = 0

                Log.d("webview x5", userAgentString)
            }
            webview = it
        }
    }
}

@Preview
@Composable
fun WebViewX5BoxPreview() {
    WebViewX5Box()
}