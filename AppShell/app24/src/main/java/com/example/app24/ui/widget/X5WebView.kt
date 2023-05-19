package com.example.app24.ui.widget

import android.util.Log
import android.webkit.WebSettings
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.runtime.*
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.example.app24.X5WebViewKit

@Composable
fun X5WebView(
    modifier: Modifier = Modifier,
) {

    val webView by X5WebViewKit.webView.subscribeAsState(initial = X5WebViewKit.webView.value!!)
    val lastUrl by X5WebViewKit.lastUrl.subscribeAsState(initial = null)
    val dispatcher = LocalOnBackPressedDispatcherOwner.current


    LaunchedEffect(webView, lastUrl) {
        Log.d("app24", "webView Compose lastUrl: ${lastUrl}")
        lastUrl?.let {
            webView.loadUrl(it)
        }
    }

    Log.d("app24", "webView Compose")

    key(webView) { // 不加 key 不会动态调用 factory ,这特性不错，奇怪，之前的问题是动不动就调用 factory .
        AndroidView(
            factory =
            {
                Log.d("app24", "webView Compose factory: ${System.identityHashCode(webView)}")
                webView
            },
            modifier = modifier
        ) {
            Log.d("app24", "webView Compose AndroidView.update ${X5WebViewKit.lastUrl.value}")
            it.webViewClient = X5WebViewKit.webViewClient
            it.webChromeClient = X5WebViewKit.webChromeClient
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

                // 页面报错继续
                domStorageEnabled = true

                // 关闭同步加载
                blockNetworkImage = false
                blockNetworkLoads = false

                // 允许文件
                allowFileAccess = true
                allowContentAccess = true

                // http https 混合内容开启
                mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            }
            //        it.addJavascriptInterface(jsSdk, jssdkName)

            X5WebViewKit.isInited.onNext(true)
        }
    }
}