package com.example.appimop.ui.widget

import android.app.Activity
import android.graphics.Bitmap
import android.net.Uri
import android.os.Message
import android.util.Log
import android.webkit.WebSettings
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.example.appimop.WebViewOpenFileChooserEvent
import com.example.appimop.WebViewProgressChangedEvent
import com.example.appimop.X5WebMultiViewKit
import com.example.appimop.X5WebMultiViewKit.canGoBack2
import com.example.appimop.X5WebMultiViewKit.onProgressChangedPublisher
import com.example.appimop.X5WebMultiViewKit.onShowFileChooserPublisher
import com.example.appimop.X5WebMultiViewKit.rememberWebView
import com.example.appimop.ui.DesignPreview
import com.example.appimop.ui.LocalNavController
import com.tencent.smtt.export.external.interfaces.ConsoleMessage
import com.tencent.smtt.export.external.interfaces.SslError
import com.tencent.smtt.export.external.interfaces.SslErrorHandler
import com.tencent.smtt.export.external.interfaces.WebResourceError
import com.tencent.smtt.export.external.interfaces.WebResourceRequest
import com.tencent.smtt.export.external.interfaces.WebResourceResponse
import com.tencent.smtt.sdk.ValueCallback
import com.tencent.smtt.sdk.WebChromeClient
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient

private val IMAGE_MIME_PATTERN = Regex(".*?(image|png|jpeg|jpg).*?")

@Composable
fun X5WebMultiViewBox(
    key: String,
) {
    val context = LocalContext.current
    val navController = LocalNavController.current
    val webView by context.rememberWebView(key)

    // 模拟只有现阶段可以获取的值。
    val jssdkName = "jssdk"
    val jssdkScheme = "appsdk"

    BackHandler(true) {
        Log.d("appimop", "webView Compose Dispose onBack Handler")
        if (webView.canGoBack2()) {
            webView.goBack()
        } else if (navController.backQueue.isEmpty()) {
            (context as? Activity)?.finish()
        } else {
            navController.popBackStack()
        }
    }

    DisposableEffect(webView) {
        Log.d("web-multi-view", "dispose: $key")
        val onLoadUrlDisposable = X5WebMultiViewKit.onLoadUrlPublisher.subscribe {
            if (it.key == key) {
                webView.loadUrl(it.url)
            }
        }
        onDispose {
            onLoadUrlDisposable.dispose()
        }
    }

//    key(webView) {
        Log.d("web-multi-view", "key: $key")
        val webViewClient = remember {
            object: WebViewClient() {
                override fun onReceivedSslError(
                    wv: WebView?,
                    handler: SslErrorHandler?,
                    error: SslError?
                ) {
                    handler?.proceed()
                }

                override fun onPageStarted(p0: WebView?, p1: String?, p2: Bitmap?) {
                    super.onPageStarted(p0, p1, p2)
                }

                override fun shouldInterceptRequest(
                    wv: WebView?,
                    req: WebResourceRequest?
                ): WebResourceResponse? {
                    // 生成返回 JSSDK
                    if (req?.url?.scheme == jssdkScheme) {
                        return WebResourceResponse(
                            "application/javascript",
                            "utf-8",
                            200,
                            "Ok",
                            mapOf (
                                "Cache-Control" to "no-cache",
                            ),
                            """
                        ${jssdkName}.launchMap = {};
                        ${jssdkName}.launch = (name, param) => {
                            return new Promise((resolve, reject) => {
                                var uuid = String(new Date().getTime());
                                ${jssdkName}.launchMap[uuid] = {
                                    resolve: resolve,
                                    reject: reject,
                                };
                                ${jssdkName}.launchDispatch(uuid, name, param);
                            });
                        };
                    """.trimIndent().byteInputStream(),
                        )
                    }
                    return super.shouldInterceptRequest(wv, req)
                }

                override fun onReceivedError(p0: WebView?, p1: WebResourceRequest?, p2: WebResourceError?) {
                    Log.d("app24", "webView error ${p1?.url} ${p2?.errorCode} ${p2?.description}")
                }

                override fun onReceivedHttpError(
                    p0: WebView?,
                    p1: WebResourceRequest?,
                    p2: WebResourceResponse?
                ) {
                    Log.d("app24", "webview error http ${p1?.url} ${p2?.statusCode}")
                }
            }
        }

        val webChromeClient = remember{
            object: WebChromeClient() {
                override fun onProgressChanged(wv: WebView?, newProgress: Int) {
                    onProgressChangedPublisher.onNext(
                        WebViewProgressChangedEvent(key, wv, newProgress)
                    )
                    super.onProgressChanged(wv, newProgress)
                }

                override fun onConsoleMessage(p0: ConsoleMessage?): Boolean {
                    Log.d("app24", "WebViewConsole:[${p0?.messageLevel()}] [${p0?.sourceId()},${p0?.lineNumber()}] ${p0?.message()}")
                    return true
                }

                override fun onCreateWindow(wv: WebView?, isDialog: Boolean, isUserGesture: Boolean, resultMsg: Message?): Boolean {
                    (resultMsg?.obj as? WebView.WebViewTransport)?.webView = webView // 唯一窗口，多窗口需要创建。
                    resultMsg?.sendToTarget()
                    return true
                }

                override fun onShowFileChooser(
                    webView: WebView,
                    filePathCallback: ValueCallback<Array<Uri>>,
                    fileChooserParams: FileChooserParams,
                ): Boolean {
                    val acceptRaw = fileChooserParams.acceptTypes.first()
                    val acceptType = if (IMAGE_MIME_PATTERN.matches(acceptRaw)) {
                        "image/*"
                    } else {
                        "video/mp4"
                    }

                    onShowFileChooserPublisher.onNext(
                        WebViewOpenFileChooserEvent(
                            key, webView, filePathCallback, acceptType
                        )
                    )
                    Log.d("app24", "onShowFileChooser: ${acceptType}")
                    return true
                }
            }
        }

        AndroidView(
            factory = {webView},
            modifier = Modifier
                .fillMaxSize()
        ) {
            it.webViewClient = webViewClient
            it.webChromeClient = webChromeClient
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
        }
//    }
}

@Preview
@Composable
fun X5WebMultiViewBoxPreview() {
    DesignPreview {
        X5WebMultiViewBox(
            key = "aaaa",
        )
    }
}