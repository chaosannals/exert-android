package com.example.app24.ui.widget

import android.webkit.WebSettings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.example.app24.X5WebViewKit

@Composable
fun X5WebView(
    modifier: Modifier = Modifier,
) {
    AndroidView(
        factory = { X5WebViewKit.webView },
        modifier = modifier
    ) {
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
//        it.loadUrl(startUrl)
//        webview = it

        X5WebViewKit.isInited.onNext(true)
    }
}