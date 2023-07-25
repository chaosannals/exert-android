package com.example.app24.ui.widget

import android.graphics.Bitmap
import android.util.Log
import android.webkit.WebSettings
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.example.app24.X5WebViewKit
import com.example.app24.js.JsSdk2
import com.example.app24.ui.DesignPreview
import com.tencent.smtt.export.external.interfaces.ConsoleMessage
import com.tencent.smtt.sdk.WebChromeClient
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient

class X5WebView2Controller(
    val webView: WebView?,
    val webViewClient: WebViewClient?,
    val webChromeClient: WebChromeClient?,
    val jsSdk: JsSdk2?,
) {
    fun loadUrl(url: String) {
        webView?.loadUrl(url)
    }
}

@Composable
fun rememberX5WebView2Controller() : X5WebView2Controller {
    val context = LocalContext.current
    val inspectionMode = LocalInspectionMode.current
    return remember(context, inspectionMode) {
        val webView = if (inspectionMode) {
            null
        } else {
            WebView(context)
        }
        val webViewClient = if (inspectionMode) {
            null
        } else {
            // 都慢于页面执行的 JS
            // 顺序由上往下：
            // onPageStarted
            // onPageFinished
            // onPageCommitVisible
            object: WebViewClient() {
                override fun onPageCommitVisible(wv: WebView?, url: String?) {
                    wv?.evaluateJavascript("""
                        if (!window.incToken) {
                            window.incToken = 1
                        } else {
                            window.incToken++
                        }
                        console.log('onPageCommitVisible before super', window.incToken);
                    """.trimIndent()) {

                    }
                    super.onPageCommitVisible(wv, url)
                    wv?.evaluateJavascript("""
                        if (!window.incToken) {
                            window.incToken = 1
                        } else {
                            window.incToken++
                        }
                        console.log('onPageCommitVisible after super', window.incToken);
                    """.trimIndent()) {

                    }
                }

                override fun onPageStarted(wv: WebView?, url: String?, favicon: Bitmap?) {
                    wv?.evaluateJavascript("""
                        if (!window.incToken) {
                            window.incToken = 1
                        } else {
                            window.incToken++
                        }
                        console.log('onPageStarted before super', window.incToken);
                    """.trimIndent()) {

                    }
                    super.onPageStarted(wv, url, favicon)
                    wv?.evaluateJavascript("""
                        if (!window.incToken) {
                            window.incToken = 1
                        } else {
                            window.incToken++
                        }
                        console.log('onPageStarted after super', window.incToken);
                    """.trimIndent()) {

                    }
                }

                override fun onPageFinished(wv: WebView?, url: String?) {
                    wv?.evaluateJavascript("""
                        if (!window.incToken) {
                            window.incToken = 1
                        } else {
                            window.incToken++
                        }
                        console.log('onPageFinished before super', window.incToken);
                    """.trimIndent()) {

                    }
                    super.onPageFinished(wv, url)
                    wv?.evaluateJavascript("""
                        if (!window.incToken) {
                            window.incToken = 1
                        } else {
                            window.incToken++
                        }
                        console.log('onPageFinished after super', window.incToken);
                    """.trimIndent()) {

                    }
                }
            }
        }

        val webChromeClient = if (inspectionMode) {
            null
        } else {
            object: WebChromeClient() {
                // 全新页面会快于，缓存页面后会慢于。html 内 JS
                override fun onProgressChanged(wv: WebView?, p: Int) {
                    wv?.evaluateJavascript("""
                        if (!window.incToken) {
                            window.incToken = 1
                        } else {
                            window.incToken++
                        }
                        console.log('onProgressChanged before super ($p)', window.incToken);
                    """.trimIndent()) {

                    }
                    super.onProgressChanged(wv, p)
                    wv?.evaluateJavascript("""
                        if (!window.incToken) {
                            window.incToken = 1
                        } else {
                            window.incToken++
                        }
                        console.log('onProgressChanged after super ($p)', window.incToken);
                    """.trimIndent()) {

                    }
                }

                override fun onConsoleMessage(cm: ConsoleMessage?): Boolean {
                    Log.d("app24", "[WebView2:${cm?.messageLevel()}] [${cm?.sourceId()},${cm?.lineNumber()}] ${cm?.message()}")
                    return true
                }
            }
        }

        val jsSdk = if (inspectionMode) {
            null
        } else {
            JsSdk2(
                onInit = {
//                webView?.evaluateJavascript("""
//                if (!window.incToken) {
//                    window.incToken = 1
//                } else {
//                    window.incToken++
//                }
//                console.log('jsSdk.init', window.incToken);
//                """.trimIndent()) {}
                },
            )
        }
        X5WebView2Controller(
            webView = webView,
            webViewClient = webViewClient,
            webChromeClient = webChromeClient,
            jsSdk = jsSdk,
        )
    }
}

@Composable
fun X5WebView2(
    controller: X5WebView2Controller,
    modifier: Modifier = Modifier,
) {
    val inspectionMode = LocalInspectionMode.current
    val m by remember(modifier) {
        derivedStateOf {
            modifier.background(Color.Gray)
        }
    }
    if (inspectionMode) {
        Box(
            modifier = m,
        )
    } else {
        AndroidView(
            factory = { controller.webView!! },
            modifier = m,
        ) {
            it.webViewClient = controller.webViewClient
            it.webChromeClient = controller.webChromeClient
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
            it.addJavascriptInterface(controller.jsSdk!!, "jsSdk")
            it.addJavascriptInterface("aaaa", "jsSdk.token") // 基础类型不可行
            it.addJavascriptInterface("bbbb", "bToken")// 基础类型不可行
        }
    }
}

@Preview
@Composable
fun X5WebView2Preview() {
    DesignPreview {
        val controller = rememberX5WebView2Controller()
        X5WebView2(
            controller = controller,
            modifier = Modifier
                .fillMaxSize()
        )
    }
}