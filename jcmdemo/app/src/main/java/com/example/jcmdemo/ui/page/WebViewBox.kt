package com.example.jcmdemo.ui.page

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.net.http.SslError
import android.os.Build
import android.util.Log
import android.webkit.*
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.jcmdemo.ui.sdp
import com.example.jcmdemo.ui.writeLog

@Composable
fun WebViewBox() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var text by remember {
        mutableStateOf("https://m.bilibili.com")
    }
    var progress by remember {
        mutableStateOf(0)
    }

    val webviewClient = object: WebViewClient() {
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            Log.d("webview","url: ${url}")
            super.onPageStarted(view, url, favicon)
        }
        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
        }
        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            Log.d("webview","url: ${request?.url}")
            if(null == request?.url) return false
            val showOverrideUrl = request.url.toString()
            try {
                if (!showOverrideUrl.startsWith("http://")
                    && !showOverrideUrl.startsWith("https://")) {
                    //处理非http和https开头的链接地址
                    Intent(Intent.ACTION_VIEW, Uri.parse(showOverrideUrl)).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        view?.context?.applicationContext?.startActivity(this)
                    }
                    return true
                }
            }catch (e:Exception){
                Log.e("webview","url: ${request?.url} ${e}")
                //没有安装和找到能打开(「xxxx://openlink.cc....」、「weixin://xxxxx」等)协议的应用
                return true
            }
            return super.shouldOverrideUrlLoading(view, request)
        }

        override fun onReceivedSslError(
            view: WebView?,
            handler: SslErrorHandler?,
            error: SslError?
        ) {
            handler?.proceed()
            //super.onReceivedSslError(view, handler, error)
        }

        override fun onReceivedError(
            view: WebView?,
            request: WebResourceRequest?,
            error: WebResourceError?
        ) {
            Log.d("webview", "${error}")
            // super.onReceivedError(view, request, error)
        }

        override fun shouldInterceptRequest(
            view: WebView?,
            request: WebResourceRequest?
        ): WebResourceResponse? {
//            Log.d("webview" ,"request url: ${request?.url}")
            return super.shouldInterceptRequest(view, request)
//            request?.url?.let {
//                return WebResourceResponse(
//                    getMimeType(it.toString()),
//                    "UTF-8",
//
//                )
//            }
        }


        fun getMimeType(url: String) : String? {
            val ext = MimeTypeMap.getFileExtensionFromUrl(url)
            return when (ext) {
                "js" -> "text/javascript"
                "html" -> "text/html"
                "woff" -> "application/font-woff"
                "woff2" -> "application/font-woff2"
                "tff" -> "application/x-font-ttf"
                "eot" -> "application/vnd.ms-fontobject"
                "svg" -> "image/svg+xml"
                else -> MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext)
            }
        }
    }
    val webchromeClient = object:  WebChromeClient() {
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            //回调网页内容加载进度
            progress = newProgress
            super.onProgressChanged(view, newProgress)
        }
    }

    val webview by remember {
        val it = WebView(context)
        mutableStateOf(it)
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    webview.onResume()
                }
                Lifecycle.Event.ON_PAUSE -> {
                    webview.onPause()
                }
                Lifecycle.Event.ON_DESTROY -> {
                    webview.destroy()
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 56.dp)
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
                                if (text.isNotEmpty()) {
                                    webview.loadUrl(text)
                                }
                            },
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
        Box (
            contentAlignment = Alignment.TopStart,
            modifier = Modifier
                .height(1.sdp)
                .fillMaxWidth()
        ){
            Spacer(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth((progress / 100).toFloat())
                    .background(Color.Blue)
            )
        }



        AndroidView(
            factory = { webview },
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

                // 页面报错继续
                domStorageEnabled = true

                // 关闭同步加载
                blockNetworkImage = false
                blockNetworkLoads = false

                // http https 混合内容开启
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                }

                Log.d("webview", userAgentString)
            }
        }
    }
}

@Preview(widthDp = 375, heightDp = 668)
@Composable
fun WebViewBoxPreview() {
    WebViewBox()
}