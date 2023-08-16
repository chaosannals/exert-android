package com.example.bootdemo.ui.page.web

import android.Manifest
import android.annotation.SuppressLint
import android.util.Log
import android.webkit.WebView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.example.bootdemo.hiltActivityViewModel
import com.example.bootdemo.requestPermissionForResult
import com.example.bootdemo.ui.MainScaffoldViewModel

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewPage(
    activityVm: MainScaffoldViewModel = hiltActivityViewModel()
) {
    val context = LocalContext.current
    val inspectionMode = LocalInspectionMode.current
    val allowNet by context.requestPermissionForResult(permission = Manifest.permission.INTERNET)
    val webView by activityVm.webView.observeAsState()

    val isAble by remember(webView, inspectionMode, allowNet) {
        derivedStateOf {
            !inspectionMode && webView != null && allowNet
        }
    }
    var url by remember {
        mutableStateOf("https://html5test.com/")
    }

    LaunchedEffect(webView) {
        if (webView == null) {
            activityVm.webView.value = WebView(context)
        }
    }

    LaunchedEffect(url) {
        webView?.run {
            loadUrl(url)
        }
    }

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            if (isAble) {
                AndroidView(
                    factory = { webView!! },
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    it.settings.apply {
                        //支持js交互
                        javaScriptEnabled = true
                        //将图片调整到适合webView的大小
                        useWideViewPort = true
                        //缩放至屏幕的大小
                        loadWithOverviewMode = true

                        // 缩放操作（注：这几个最好不要开启，会有闪退问题）
                        setSupportZoom(false)
                        builtInZoomControls = false
                        displayZoomControls = false

                        //是否支持通过JS打开新窗口
                        //javaScriptCanOpenWindowsAutomatically = true

                        // 支持多窗口 onCreateWindow，此时要处理该事件
                        // 该事件会生成新的 WebView,除非是做浏览器，套壳一般禁用。
                        setSupportMultipleWindows(false)


                        //不加载缓存内容
                        //cacheMode = WebSettings.LOAD_NO_CACHE

                        // 页面报错继续
                        domStorageEnabled = true

                        // 关闭同步加载
                        blockNetworkImage = false
                        blockNetworkLoads = false

                        //setAllowFileAccessFromFileURLs(true)
                        //setAllowUniversalAccessFromFileURLs(true)
                        allowContentAccess = true
                        allowFileAccess = true

                        // http https 混合内容开启
                        mixedContentMode = android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW

                        Log.d("webview", userAgentString)
                    }
                }
            }
        }

        Row (
            modifier = Modifier
                .fillMaxWidth()
        ){
            Button(onClick = { url = "about:blank" }) {
                Text("空白")
            }
            Button(
                onClick =
                {
                    webView?.evaluateJavascript("""
                        window.location.replace('about:blank');
                    """.trimIndent()) {}
                },
            ) {
                Text("空白（JS）")
            }
            Button(onClick = { url = "https://m.bilibili.com" }) {
                Text("B站")
            }
            Button(
                onClick =
                {
                    webView?.evaluateJavascript("""
                        window.location.replace('https://m.bilibili.com');
                    """.trimIndent()) {}
                },
            ) {
                Text("B站（JS）")
            }
        }
    }
}

@Preview
@Composable
fun WebViewPagePreview() {
    WebViewPage()
}