package com.example.jcmdemo.ui.page

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Message
import android.util.Log
import android.webkit.JavascriptInterface
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.compose.rememberNavController
import com.example.jcmdemo.LocalNavController
import com.example.jcmdemo.ui.sdp
import com.tencent.smtt.export.external.interfaces.*
import com.tencent.smtt.sdk.WebChromeClient
import com.tencent.smtt.sdk.WebSettings
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient
import kotlinx.serialization.json.Json
import kotlinx.serialization.Serializable

@Serializable
data class WebViewX5MapPosition (
    val text: String = "tttttt",
    val latitude: Float = 23.4502343750f,
    val longitude: Float = 116.7753624132f,
)

@Composable
fun WebViewX5Map() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    
    var tip by remember {
        mutableStateOf("1111")
    }

    var webview by remember {
        val it = WebView(context)

        mutableStateOf(it)
    }
    val webviewClient = object: WebViewClient() {
        override fun onReceivedSslError(
            wv: WebView?,
            handler: SslErrorHandler?,
            error: SslError?
        ) {
            handler?.proceed()
        }

        override fun shouldOverrideUrlLoading(wv: WebView?, url: String?): Boolean {
            if(null == url) return false
            try {
                if (!url.startsWith("http://")
                    && !url.startsWith("https://")) {
                    //处理非http和https开头的链接地址
                    Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        wv?.context?.applicationContext?.startActivity(this)
                    }
                    return true
                }
            }catch (e:Exception){
                Log.e("webview","url: ${url} ${e}")
                //没有安装和找到能打开(「xxxx://openlink.cc....」、「weixin://xxxxx」等)协议的应用
                return true
            }
            return super.shouldOverrideUrlLoading(wv, url)
        }

        override fun onReceivedError(p0: WebView?, p1: WebResourceRequest?, p2: WebResourceError?) {
            Log.e("webview x5", "${p1}  ${p2}")
//            super.onReceivedError(p0, p1, p2)
        }

        override fun onReceivedHttpError(
            p0: WebView?,
            p1: WebResourceRequest?,
            p2: WebResourceResponse?
        ) {
            Log.e("webview x5", "${p1}  ${p2}")
//            super.onReceivedHttpError(p0, p1, p2)
        }
    }
    val webchromeClient = object: WebChromeClient() {
        override fun onProgressChanged(wv: WebView?, newProgress: Int) {
            super.onProgressChanged(wv, newProgress)
        }

        override fun onConsoleMessage(p0: ConsoleMessage?): Boolean {
            Log.d("consolemessage", p0!!.message())
            return true
        }

        override fun onCreateWindow(wv: WebView?, isDialog: Boolean, isUserGesture: Boolean, resultMsg: Message?): Boolean {

            // TODO 新建一个新窗口 之后要给新窗口安排 Webview 的显示
            // resultMsg 报错的话就不要管了，直接新窗口造一个无关的。
            (resultMsg?.obj as? WebView.WebViewTransport)?.webView = WebView(context)
            resultMsg?.sendToTarget()
//            return super.onCreateWindow(p0, p1, p2, p3)
            return true
        }
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
        Text(
            text = tip,
            modifier = Modifier.clickable {
                webview.evaluateJavascript("javascript:addMark(116.77536241,23.45023437)") {

                }
            }
        )
        AndroidView(
            factory = { WebView(it) },
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.0f)
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

                // 支持多窗口 onCreateWindow
                setSupportMultipleWindows(true)


                //不加载缓存内容
                cacheMode = WebSettings.LOAD_NO_CACHE

                // 页面报错继续
                domStorageEnabled = true

                // 关闭同步加载
                blockNetworkImage = false
                blockNetworkLoads = false

                // http https 混合内容开启
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mixedContentMode = android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                }

                Log.d("webview x5", userAgentString)
            }
            it.addJavascriptInterface(object
            {
                @JavascriptInterface
                fun setTipMap(t: String) {
                    tip = t
                }

                @JavascriptInterface
                fun getPosition(): String {
                    val p = WebViewX5MapPosition()
                    return Json{
                        encodeDefaults = true
                        explicitNulls = false
                    }.encodeToString(WebViewX5MapPosition.serializer(), p)
                }
            }, "wvx5map")
            webview = it
            webview.loadUrl("file:///android_asset/webviewx5map/index.html")
        }
    }
}

@Preview
@Composable
fun WebViewX5MapPreview() {
    CompositionLocalProvider(
        LocalNavController provides rememberNavController(),
    ) {
        WebViewX5Map()
    }
}