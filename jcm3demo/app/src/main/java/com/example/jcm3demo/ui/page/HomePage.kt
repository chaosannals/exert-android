package com.example.jcm3demo.ui.page

import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import android.webkit.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver

@Composable
fun HomePage() {
    val context = LocalContext.current
    val webView = remember {
        WebView(context)
    }
    val lifecycleOwner by rememberUpdatedState(LocalLifecycleOwner.current)

    DisposableEffect(lifecycleOwner) {
        val lifecycle =lifecycleOwner.lifecycle
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
        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }

    Column (
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize(),
    ) {
        Row (
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Button(onClick = {
                webView.evaluateJavascript("someThing.doSomeThing('aaaa');") {
                    Log.d("homepage", "evalateJs: $it")
                }

//                webView.loadUrl("javascript:someThing.doSomeThing('bbbb');")
            }) {
                Text("doSomeThing")
            }
        }
        AndroidView(
            factory = {
                webView
            },
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            it.settings.setSupportZoom(true)

            it.settings.loadWithOverviewMode = true // 缩放到屏幕大小
            it.settings.useWideViewPort = true

//            it.settings

            it.settings.allowFileAccess = true // 允许访问文件
            it.settings.defaultTextEncodingName = "utf-8"
            it.settings.javaScriptEnabled = true // 启用 js
            it.settings.javaScriptCanOpenWindowsAutomatically = true
            it.settings.domStorageEnabled = true
            //it.settings

            // 地理位置，会导致弹出到别的浏览器，
//            it.settings.setGeolocationEnabled(true)

            // 缓存
            it.settings.cacheMode = WebSettings.LOAD_DEFAULT // 默认，最接近一般浏览器
//            it.settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK // 优先使用缓存，只要有缓存就不请求
//            it.settings.cacheMode = WebSettings.LOAD_NO_CACHE // 不使用缓存，CSS 丢失时
//            it.settings.cacheMode = WebSettings.LOAD_CACHE_ONLY // 只使用缓存


            it.webViewClient = object: WebViewClient() {
                // 其他事件，override 查看。

                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    Log.d("homepage", "webview page 开始加载")
                    val cm = CookieManager.getInstance()
                    cm.setAcceptCookie(true)
                    //
                    cm.setCookie(url, "key=12345678988888")
                    cm.setCookie(url, "keyOther=6576768AAAAA")
                    Log.d("homepage", "webview page set cookie")
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    Log.d("homepage", "webview page 完成加载")
                    val cm = CookieManager.getInstance()
                    val cookie = cm.getCookie(url)
                    Log.d("homepage", "cookie: $cookie")
                }

                // 链接跳转都会走这个方法
                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    try {
                        // 如果是自己的网站，其实没有这种特别的协议。
                        if (listOf("baiduboxapp", "baiduboxlite").contains(request?.url?.scheme)) {
                            val intent = Intent(Intent.ACTION_VIEW, request?.url)
                            startActivity(context, intent, null)
                            return true
                        }
                    } catch (e: Exception) {
                        return false
                    }
                    view?.loadUrl(request?.url.toString())
                    return true
                }
            }


            it.webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    super.onProgressChanged(view, newProgress)
                    Log.d("homepage", "进度：$newProgress")
                }
            }

//            it.loadUrl("https://bilibili.com")
            it.loadUrl("https://baidu.com")

            it.addJavascriptInterface(object {
                @JavascriptInterface
                fun doSomeThing(text: String) : String {
                    Log.d("homepage", "doSomeThing")
                    return "doSomeThing result $text."
                }
            }, "someThing")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePagePreview() {
    HomePage()
}