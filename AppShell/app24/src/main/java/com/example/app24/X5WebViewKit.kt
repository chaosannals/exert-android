package com.example.app24

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Message
import android.util.Log
import com.tencent.smtt.export.external.TbsCoreSettings
import com.tencent.smtt.export.external.interfaces.ConsoleMessage
import com.tencent.smtt.export.external.interfaces.SslError
import com.tencent.smtt.export.external.interfaces.SslErrorHandler
import com.tencent.smtt.export.external.interfaces.WebResourceError
import com.tencent.smtt.export.external.interfaces.WebResourceRequest
import com.tencent.smtt.export.external.interfaces.WebResourceResponse
import com.tencent.smtt.sdk.QbSdk
import com.tencent.smtt.sdk.ValueCallback
import com.tencent.smtt.sdk.WebChromeClient
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject

object X5WebViewKit: QbSdk.PreInitCallback {
    private val IMAGE_MIME_PATTERN = Regex(".*?(image|png|jpeg|jpg).*?")

    val jssdkName = "testjssdk"

    data class WebViewPageStartedEvent(
        val p0: WebView?,
        val p1: String?,
        val p2: Bitmap?
    )

    data class WebViewOpenFileChooserEvent(
        val mWebView: WebView,
        val filePathCallback: ValueCallback<Array<Uri>>,
        val acceptType: String,
    )

    val onLoadedPublisher: PublishSubject<WebViewPageStartedEvent> = PublishSubject.create()
    val onShowFileChooserPublisher: PublishSubject<WebViewOpenFileChooserEvent> = PublishSubject.create()
    val isInited: BehaviorSubject<Boolean> = BehaviorSubject.create()
    val lastUrl: BehaviorSubject<String> = BehaviorSubject.create()
    val progress: BehaviorSubject<Int> = BehaviorSubject.create()
    val webView: BehaviorSubject<WebView> = BehaviorSubject.create()

    fun WebView.logHistory() {
        val bfl = copyBackForwardList()
        Log.d("app24", "WebView History Size: ${bfl.size}")
        Log.d("app24", "WebView History Current: ${bfl.currentItem?.url}")
        for (i in 0 until bfl.size) {
            Log.d("app24", "WebView History: ${bfl.getItemAtIndex(i)?.url}")
        }
    }

    fun loadUrl(url: String) {
        webView.value?.logHistory()
        lastUrl.onNext(url)
    }


    fun Context.reloadUrl(url: String) {
        val now = WebView(this)
        loadUrl(url)
        webView.onNext(now)
        now.logHistory()
    }

    val webViewClient = object: WebViewClient() {
        override fun onReceivedSslError(
            wv: WebView?,
            handler: SslErrorHandler?,
            error: SslError?
        ) {
            handler?.proceed()
        }

        override fun onPageStarted(p0: WebView?, p1: String?, p2: Bitmap?) {
            super.onPageStarted(p0, p1, p2)
            onLoadedPublisher.onNext(WebViewPageStartedEvent(p0, p1, p2))
        }

        override fun shouldInterceptRequest(
            wv: WebView?,
            req: WebResourceRequest?
        ): WebResourceResponse? {
            // 生成返回 JSSDK
            if (req?.url?.scheme == "testjssdk") {
                return WebResourceResponse(
                    "application/js",
                    "utf-8",
                    200,
                    "Ok",
                    mapOf (
                        "Cache-Control" to "no-cache",
                    ),
                    """
                        ${jssdkName}.taskQueue = {};
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

    val webChromeClient = object: WebChromeClient() {
        override fun onProgressChanged(wv: WebView?, newProgress: Int) {
            progress.onNext(newProgress)
            super.onProgressChanged(wv, newProgress)
        }

        override fun onConsoleMessage(p0: ConsoleMessage?): Boolean {
            Log.d("app24", "WebViewConsole:[${p0?.messageLevel()}] [${p0?.sourceId()},${p0?.lineNumber()}] ${p0?.message()}")
            return true
        }

        override fun onCreateWindow(wv: WebView?, isDialog: Boolean, isUserGesture: Boolean, resultMsg: Message?): Boolean {
            (resultMsg?.obj as? WebView.WebViewTransport)?.webView = webView.value!! // 唯一窗口，多窗口需要创建。
            resultMsg?.sendToTarget()
            return true
        }

        override fun onShowFileChooser(
            mWebView: WebView,
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
                    mWebView, filePathCallback, acceptType
                )
            )
            Log.d("app24", "onShowFileChooser: ${acceptType}")
            return true
        }
    }

    fun Context.initX5() {
        webView.onNext(WebView(this))
        val tbss = mapOf(
            TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER to true,
            TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE to true,
        )
        QbSdk.initTbsSettings(tbss)
        QbSdk.setDownloadWithoutWifi(true)
        // QbSdk.clearAllWebViewCache(this, true)
        QbSdk.initX5Environment(this, this@X5WebViewKit)
    }

    override fun onCoreInitFinished() {

    }

    override fun onViewInitFinished(p0: Boolean) {

    }
}