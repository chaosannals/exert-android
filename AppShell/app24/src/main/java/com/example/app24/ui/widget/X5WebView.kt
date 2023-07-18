package com.example.app24.ui.widget

import android.app.Activity
import android.util.Log
import android.view.ViewGroup
import android.webkit.WebSettings
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties
import com.example.app24.X5WebViewKit
import com.example.app24.X5WebViewKit.downloadPicture
import com.example.app24.X5WebViewKit.jssdkName
import com.example.app24.X5WebViewKit.launchReject
import com.example.app24.X5WebViewKit.launchResolve
import com.example.app24.js.JsSdk
import com.example.app24.ui.LocalNavController
import com.example.app24.ui.sdp
import com.tencent.smtt.sdk.WebView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun X5WebView(
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val navController = LocalNavController.current
    val scope = rememberCoroutineScope()
    val webView by X5WebViewKit.webView.subscribeAsState(initial = X5WebViewKit.webView.value!!)
    val lastUrl by X5WebViewKit.lastUrl.subscribeAsState(initial = null)
//    val longClickImage by X5WebViewKit.onLongClickImagePublisher.subscribeAsState(initial = null)
    var longClickImage by remember {
        mutableStateOf<X5WebViewKit.WebViewLongClickImageEvent?>(null)
    }
    var isShowBottomMenu by remember { mutableStateOf(false) }
    
    BackHandler(true) {
        Log.d("app24", "webView Compose Dispose onBack Handler")
        if (webView.canGoBack()) {
            webView.goBack()
        } else if (navController.backQueue.isEmpty()) {
            (context as? Activity)?.finish()
        } else {
            navController.popBackStack()
        }
    }

    // longClickImage 联动有问题，改用 DisposableEffect
//    LaunchedEffect(longClickImage) {
//        isShowBottomMenu = longClickImage != null
//        Log.d("app24", "longClickImage change")
//    }

    DisposableEffect(Unit) {
        Log.d("app24", "webView Disposable Start")
        val longClickImageDisposable = X5WebViewKit.onLongClickImagePublisher.subscribe {
            isShowBottomMenu = longClickImage != null
            longClickImage = it
            Log.d("app24", "webView Disposable longClick call.")
        }
        onDispose {
            longClickImageDisposable.dispose()
            Log.d("app24", "webView Disposable dispose")
        }
    }

    LaunchedEffect(webView, lastUrl) {
        Log.d("app24", "webView Compose lastUrl: ${lastUrl}")
        lastUrl?.let {
            webView.loadUrl(it)
        }
    }

    Log.d("app24", "webView Compose")

    val jsSdk = remember {
        JsSdk(
            onSyncCall =
            { uuid, param ->
                webView.launchResolve(uuid, "null")
            },
            onAsyncCall =
            { uuid, param ->
                scope.launch(Dispatchers.IO) {
                    delay(10000)
                    webView.launchResolve(uuid, "")
                }
            },
            onResultCall =
            { uuid, param ->
                webView.launchResolve(uuid, """
                    {
                        v1: 123,
                        v2: "12312"
                    }
                """.trimIndent())
            },
            onSyncFail =
            {  uuid, param ->
                webView.launchReject(uuid, "同步执行错误")
            },
            onAsyncFail =
            {  uuid, param ->
                scope.launch(Dispatchers.IO) {
                    delay(10000)
                    webView.launchReject(uuid, "异步执行错误")
                }
            },
            onResultFail =
            {  uuid, param ->
                webView.launchReject(uuid, "带结果执行错误")
            }
        )
    }

    Box(
        modifier = modifier,
    ) {
        key(webView) { // 不加 key 不会动态调用 factory ,这特性不错，奇怪，之前的问题是动不动就调用 factory .
            AndroidView(
                factory =
                {
                    Log.d("app24", "webView Compose factory: ${System.identityHashCode(webView)}")
                    webView.apply {
                        if (parent != null) {
                            val p = (parent as ViewGroup)
                            val n = WebView(it)
                            n.id = id
                            p.removeView(this)
                            p.addView(n)
                            Log.d("app24", "android view factory remove ${n.id} $id")
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxSize()
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
                it.addJavascriptInterface(jsSdk, jssdkName)

                X5WebViewKit.isInited.onNext(true)
            }
        }

        // 底部菜单
        if (isShowBottomMenu) {
            Popup(
                onDismissRequest =
                {
                    isShowBottomMenu = false
                },
                popupPositionProvider = object : PopupPositionProvider {
                    override fun calculatePosition(
                        anchorBounds: IntRect,
                        windowSize: IntSize,
                        layoutDirection: LayoutDirection,
                        popupContentSize: IntSize
                    ): IntOffset {
                        val x = 0
                        val y = anchorBounds.height - popupContentSize.height
                        return IntOffset(x, y)
                    }
                },
                properties = PopupProperties(
                    focusable = true,
                ),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Color.White,
                            RoundedCornerShape(topEnd = 10.sdp, topStart = 10.sdp)
                        )
                        .border(
                            1.sdp,
                            Color.Cyan,
                            RoundedCornerShape(topEnd = 10.sdp, topStart = 10.sdp)
                        )
                        .padding(10.sdp)
                ) {
                    Text(
                        text = "下载图片",
                        modifier = Modifier
                            .clickable {
                                longClickImage?.url?.let {
                                    context.downloadPicture(it)
                                }
                                isShowBottomMenu = false
                            }
                    )
                }
            }
        }
    }
}