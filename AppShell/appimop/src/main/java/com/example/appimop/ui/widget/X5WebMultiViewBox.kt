package com.example.appimop.ui.widget

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Bitmap
import android.net.Uri
import android.os.Message
import android.util.Log
import android.view.ViewGroup
import android.webkit.WebSettings
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.appimop.WebViewAccessCustomSchemeEvent
import com.example.appimop.WebViewLongClickImageEvent
import com.example.appimop.WebViewOpenFileChooserEvent
import com.example.appimop.WebViewProgressChangedEvent
import com.example.appimop.X5WebMultiViewKit
import com.example.appimop.X5WebMultiViewKit.canGoBack2
import com.example.appimop.X5WebMultiViewKit.download
import com.example.appimop.X5WebMultiViewKit.onProgressChangedPublisher
import com.example.appimop.X5WebMultiViewKit.onShowFileChooserPublisher
import com.example.appimop.X5WebMultiViewKit.rememberWebView
import com.example.appimop.X5WebMultiViewKit.savePicture
import com.example.appimop.X5WebMultiViewKit.saveVideo
import com.example.appimop.ui.DesignPreview
import com.example.appimop.ui.LocalNavController
import com.example.appimop.ui.sdp
import com.example.appimop.ui.ssp
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private val IMAGE_MIME_PATTERN = Regex(".*?(image|png|jpeg|jpg).*?")

fun newBackPage() : WebResourceResponse {
    return WebResourceResponse(
        "text/html",
        "utf-8",
        200,
        "Ok",
        mapOf(),
        """<script> window.history.go(-1); </script>""".trimIndent().byteInputStream()
    )
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun X5WebMultiViewBox(
    key: String,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val navController = LocalNavController.current
    val webViewScope = rememberCoroutineScope()
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

    var longClickImage: WebViewLongClickImageEvent? by remember {
        mutableStateOf(null)
    }

    DisposableEffect(webView) {
        Log.d("web-multi-view", "dispose: $key")
        val onLongClickImageDisposable = X5WebMultiViewKit.onLongClickImagePublisher.subscribe {
            if (it.key == key) {
                longClickImage = it
            }
        }

        val onDownloadVideoDisposable = X5WebMultiViewKit.onDownloadVideoPublisher.subscribe {
            if (it.key == key) {
                webViewScope.download(it.url) {
                    it?.let {
                        context.saveVideo(it.data, it.mimeType)
                    }
                }
            }
        }

        val onLoadUrlDisposable = X5WebMultiViewKit.onLoadUrlPublisher.subscribe {
            if (it.key == key) {
                webView.loadUrl(it.url)
            }
        }

        onDispose {
            onLongClickImageDisposable.dispose()
            onDownloadVideoDisposable.dispose()
            onLoadUrlDisposable.dispose()

        }
    }

    // TODO
    // 此处不精确。全局保有的 webview 在此处不一定就会被回收。
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    webView.onResume()
                }
                Lifecycle.Event.ON_PAUSE -> {
                    webView.onPause()
                }
                Lifecycle.Event.ON_DESTROY -> {
                    webView.destroy()
                }
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        Log.d("web-multi-view", "dispose start")
        onDispose {
            Log.d("web-multi-view", "on dispose")
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    key(webView) {
        Log.d("web-multi-view", "key: $key")
        val webViewClient = remember {
            object : WebViewClient() {
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
                    req?.url?.let {
                        if (it.scheme == jssdkScheme) {
                            X5WebMultiViewKit.onAccessCustomSchemePublisher.onNext(
                                WebViewAccessCustomSchemeEvent(key, req.url)
                            )
                            if (it.path == "/to_location_page") {
                                webViewScope.launch(Dispatchers.Main) {
                                    navController.navigate("location")
                                }
                                return newBackPage()
                            } else if (it.path == "/to_args") {
                                val s = it.getQueryParameter("s")
                                val b = it.getQueryParameter("b")
                                webViewScope.launch(Dispatchers.Main) {
                                    navController.navigate("args?s=${s}&b=${b}")
                                }
                                return newBackPage()
                            } else if (it.path == "/jssdk.js") {
                                return WebResourceResponse(
                                    "application/javascript",
                                    "utf-8",
                                    200,
                                    "Ok",
                                    mapOf(
                                        "Cache-Control" to "no-cache",
                                    ),
                                    """
                                        console.log('init jssdk.');
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
                                        ${jssdkName}.callApp = (url) => {
                                            
                                        };
                                    """.trimIndent().byteInputStream(),
                                )
                            }
                        }
                    }
                    return super.shouldInterceptRequest(wv, req)
                }

                override fun onReceivedError(
                    p0: WebView?,
                    p1: WebResourceRequest?,
                    p2: WebResourceError?
                ) {
                    Log.d(
                        "web-multi-view",
                        "webView error ${p1?.url} ${p2?.errorCode} ${p2?.description}"
                    )
                }

                override fun onReceivedHttpError(
                    p0: WebView?,
                    p1: WebResourceRequest?,
                    p2: WebResourceResponse?
                ) {
                    Log.d("web-multi-view", "webview error http ${p1?.url} ${p2?.statusCode}")
                }
            }
        }

        val webChromeClient = remember {
            object : WebChromeClient() {
                override fun onProgressChanged(wv: WebView?, newProgress: Int) {
                    onProgressChangedPublisher.onNext(
                        WebViewProgressChangedEvent(key, wv, newProgress)
                    )
                    super.onProgressChanged(wv, newProgress)
                }

                override fun onConsoleMessage(p0: ConsoleMessage?): Boolean {
                    Log.d(
                        "web-multi-view",
                        "WebViewConsole:[${p0?.messageLevel()}] [${p0?.sourceId()},${p0?.lineNumber()}] ${p0?.message()}"
                    )
                    return true
                }

                override fun onCreateWindow(
                    wv: WebView?,
                    isDialog: Boolean,
                    isUserGesture: Boolean,
                    resultMsg: Message?
                ): Boolean {
                    (resultMsg?.obj as? WebView.WebViewTransport)?.webView =
                        webView // 唯一窗口，多窗口需要创建。
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
                    Log.d("web-multi-view", "onShowFileChooser: ${acceptType}")
                    return true
                }
            }
        }


        Column(
            verticalArrangement=Arrangement.Top,
            horizontalAlignment=Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
        ) {
            AndroidView(
                factory =
                {
                    Log.d("web-multi-view", "android view factory start")
                    // 路由是可以被重入的，重用 webView 需要确保其 parent 唯一。路由重入的时候清除上一个 AndroidView 挂载的 webView
                    webView.apply {
                        if (parent != null) {
                            val p = (parent as ViewGroup)
                            // 以下 2 种方式都可行，感觉用 id 的可能出问题会小些。

                            // 第一种
//                            val i = p.indexOfChild(this)
//                            val n = WebView(it)
//                            p.removeView(this)
//                            p.addView(n, i)
//                            Log.d("web-multi-view", "android view factory remove index: $i")

                            // 第二种
                            val n = WebView(it)
                            n.id = id
                            p.removeView(this)
                            p.addView(n)
                            Log.d("web-multi-view", "android view factory remove ${n.id} $id")
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
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

            longClickImage?.let { event ->
                Popup(
                    popupPositionProvider = object : PopupPositionProvider {
                        override fun calculatePosition(
                            anchorBounds: IntRect,
                            windowSize: IntSize,
                            layoutDirection: LayoutDirection,
                            popupContentSize: IntSize
                        ): IntOffset {
                            val x = anchorBounds.left
                            val y = anchorBounds.bottom
                            return IntOffset(x, y)
                        }
                    },
                    properties = PopupProperties(
                        focusable = true,
                        dismissOnBackPress = true,
                        dismissOnClickOutside = true,
                    ),
                    onDismissRequest = { longClickImage = null },
                ) {
                    Column(
                        verticalArrangement=Arrangement.Top,
                        horizontalAlignment=Alignment.Start,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Color.White,
                                RoundedCornerShape(topStart = 14.sdp, topEnd = 14.sdp)
                            )
                            .padding(top = 14.sdp),
                    ) {
                        Text(
                            text = "下载图片",
                            color = Color(0xFF000000),
                            fontSize = 14.ssp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .clickable {
                                    webViewScope.download(event.url) {
                                        it?.let {
                                            context.savePicture(it.data, it.mimeType)
                                        }
                                    }
                                }
                        )
                    }
                }
            }
        }
    }
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