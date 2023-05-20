package com.example.app24.ui.widget

import android.app.Activity
import android.util.Log
import android.webkit.WebSettings
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.PopupProperties
import com.example.app24.X5WebViewKit
import com.example.app24.ui.LocalNavController

@Composable
fun X5WebView(
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val navController = LocalNavController.current
    val webView by X5WebViewKit.webView.subscribeAsState(initial = X5WebViewKit.webView.value!!)
    val lastUrl by X5WebViewKit.lastUrl.subscribeAsState(initial = null)
    val longClickImage by X5WebViewKit.onLongClickImagePublisher.subscribeAsState(initial = null)
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

    LaunchedEffect(longClickImage) {
        isShowBottomMenu = longClickImage != null
    }

    LaunchedEffect(webView, lastUrl) {
        Log.d("app24", "webView Compose lastUrl: ${lastUrl}")
        lastUrl?.let {
            webView.loadUrl(it)
        }
    }

    Log.d("app24", "webView Compose")

    Box(
        modifier = modifier,
    ) {
        key(webView) { // 不加 key 不会动态调用 factory ,这特性不错，奇怪，之前的问题是动不动就调用 factory .
            AndroidView(
                factory =
                {
                    Log.d("app24", "webView Compose factory: ${System.identityHashCode(webView)}")
                    webView
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
                //        it.addJavascriptInterface(jsSdk, jssdkName)

                X5WebViewKit.isInited.onNext(true)
            }
        }

        // 底部菜单
        Column (
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ){
            DropdownMenu(
                expanded = isShowBottomMenu,
                onDismissRequest = { isShowBottomMenu = false },
                properties= PopupProperties(
                    focusable = true,
                ),
            ) {
                Text(text = "Url: ${longClickImage?.url}")
            }
        }
    }
}