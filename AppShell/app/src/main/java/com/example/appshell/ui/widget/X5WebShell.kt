package com.example.appshell.ui.widget

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Message
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.appshell.LocalTotalStatus
import com.example.appshell.db.WebViewConf
import com.example.appshell.js.KjsObject
import com.example.appshell.ui.ensurePermit
import com.tencent.smtt.export.external.TbsCoreSettings
import com.tencent.smtt.export.external.interfaces.*
import com.tencent.smtt.sdk.*
import java.io.ByteArrayInputStream

@Composable
fun X5WebShell(
    conf: WebViewConf?,
    onLoaded: ((WebView)->Unit)? = null, // 此处执行的 js 无法确保每个页面都有执行，会被重定向清掉状态。
    onPageStarted: ((WebView)->Unit)? = null, // 每个页面开始。
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val dispatcher = LocalOnBackPressedDispatcherOwner.current
    val totalStatus = LocalTotalStatus.current

    (context as? Activity)?.let {
        ensurePermit(it, Manifest.permission.INTERNET)
        ensurePermit(it, Manifest.permission.ACCESS_NETWORK_STATE)
    }

    var progress by remember {
        mutableStateOf(0)
    }
    var webview: WebView? by remember {
        mutableStateOf(null)
    }

    val webviewClient = object: WebViewClient() {
        override fun onReceivedSslError(
            wv: WebView?,
            handler: SslErrorHandler?,
            error: SslError?
        ) {
            handler?.proceed()
//            super.onReceivedSslError(p0, p1, p2)
        }

        override fun onPageStarted(p0: WebView?, p1: String?, p2: Bitmap?) {
            super.onPageStarted(p0, p1, p2)
            p0?.let { onPageStarted?.invoke(it) }
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

        override fun shouldInterceptRequest(
            p0: WebView?,
            p1: WebResourceRequest?
        ): WebResourceResponse? {
            val path = p1?.url?.path ?: ""

            // <script> 是同步加载，可以强制页面初始化加载，达到初始化确定先后的目的。
            // 钉钉 微信 公众号都使用此类方式，要求加载各自的 JSSDK 文件。

            if (path == "/test.js") {
                return WebResourceResponse(
                    "application/js",
                    "utf-8",
                    200,
                    "Ok",
                    mapOf (
                        "Cache-Control" to "no-cache",
                    ),
                    """
                        console.log('tttt', 'i'm test.js');
                    """.trimIndent().byteInputStream(),
                )
            }
            return super.shouldInterceptRequest(p0, p1)
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
            progress = newProgress
            super.onProgressChanged(wv, newProgress)
        }

        override fun onConsoleMessage(p0: ConsoleMessage?): Boolean {
            Log.d("webview x5", "[${p0?.messageLevel()}] ${p0?.sourceId()}, ${p0?.lineNumber()} ${p0?.message()}")
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

    val onBack = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (webview != null && webview!!.canGoBack()) {
                    webview!!.goBack()
                } else {
                    //navController.backQueue.clear()
                    if (totalStatus.router.backQueue.isEmpty()) {
                        // 此类 onBackPressed 的操作会导致无限递归
                        // 因为这类 onBackPressed 函数就是调用了监听器，监听器再调用 onBackPressed 导致无限递归。
                        //(context as? Activity)?.onBackPressed()

                        (context as? Activity)?.finish()
                    } else {
                        totalStatus.router.popBackStack()
                    }
                }
                //Toast.makeText(context, "back", Toast.LENGTH_SHORT).show()
            }
        }
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    webview?.onResume()
                }
                Lifecycle.Event.ON_PAUSE -> {
                    webview?.onPause()
                }
                Lifecycle.Event.ON_DESTROY -> {
                    webview?.destroy()
                }
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        dispatcher?.onBackPressedDispatcher?.addCallback(onBack)

        onDispose {
            onBack.remove()
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val kjso by remember {
        mutableStateOf(
            KjsObject(
                OnToast = {
                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                },
                OnToggleTabbarVisible = {
                    Toast.makeText(context, "toggle tabbar $it", Toast.LENGTH_SHORT).show()
                },
                OnInvalidToken = {
                    Toast.makeText(context, "invalid Token", Toast.LENGTH_SHORT).show()
                }
            )
        )
    }

    AndroidView(
        factory = { WebView(it) },
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
            //javaScriptCanOpenWindowsAutomatically = true

            // 支持多窗口 onCreateWindow
            setSupportMultipleWindows(true)


            //不加载缓存内容
            //cacheMode = WebSettings.LOAD_NO_CACHE

            // 页面报错继续
            domStorageEnabled = true

            // 关闭同步加载
            blockNetworkImage = false
            blockNetworkLoads = false

            // http https 混合内容开启
            mixedContentMode = android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW

            Log.d("webview x5", userAgentString)
        }

        conf?.let {c ->
            // 此种可以确保每个页面最先注入，该对象的接口只能是函数。
            it.addJavascriptInterface(kjso, c.valName)
            it.loadUrl(c.startUrl)
        }
        onLoaded?.invoke(it)
        webview = it
    }
}

@Preview
@Composable
fun X5WebShellPreview() {
    val conf = WebViewConf(
        id = 1,
        startUrl = "https://m.bilibili.com",
        valName = "app"
    )
    DesignPreview {
        X5WebShell(conf)
    }
}

fun Context.initX5WebShell() {
    val tbss = mapOf(
        TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER to true,
        TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE to true,
    )
    QbSdk.initTbsSettings(tbss)
    QbSdk.setDownloadWithoutWifi(true)
    QbSdk.disableAutoCreateX5Webview()
    QbSdk.setTbsListener(object: TbsListener {
        override fun onDownloadFinish(p0: Int) {
            Log.d("x5webview", "onDownloadFinish -->下载X5内核完成：$p0")
        }

        override fun onInstallFinish(p0: Int) {
            Log.d("x5webview", "onInstallFinish -->安装X5内核进度：$p0")
        }

        override fun onDownloadProgress(p0: Int) {
            Log.d("x5webview", "onDownloadProgress -->下载X5内核进度：$p0")
        }
    })

    // 初始化 X5
    QbSdk.initX5Environment(this, object: QbSdk.PreInitCallback {
        override fun onCoreInitFinished() {
            Log.d("x5webview", "onCoreInitFinished")
        }

        override fun onViewInitFinished(p0: Boolean) {
            Log.d("x5webview", "onViewInitFinished $p0")
            if (!p0) {
                TbsDownloader.startDownload(this@initX5WebShell)
            }
        }
    })
}