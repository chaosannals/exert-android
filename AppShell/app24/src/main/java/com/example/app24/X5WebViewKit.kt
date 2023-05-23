package com.example.app24

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Build
import android.os.Message
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
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
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.request.get
import io.ktor.client.statement.readBytes
import io.ktor.http.contentType
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.format.DateTimeFormatter
import kotlin.coroutines.EmptyCoroutineContext

object X5WebViewKit: QbSdk.PreInitCallback {
    private val IMAGE_MIME_PATTERN = Regex(".*?(image|png|jpeg|jpg).*?")

    val httpClient by lazy {
        HttpClient(Android) {}
    }
    val httpScope by lazy {
        CoroutineScope(EmptyCoroutineContext)
    }

    val jssdkName = "jssdk"
    val jssdkScheme = "appsdk"

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

    data class WebViewLongClickImageEvent(
        val url: String?,
    )

    val onLoadedPublisher: PublishSubject<WebViewPageStartedEvent> = PublishSubject.create()
    val onShowFileChooserPublisher: PublishSubject<WebViewOpenFileChooserEvent> = PublishSubject.create()
    val onLongClickImagePublisher: PublishSubject<WebViewLongClickImageEvent> = PublishSubject.create()
    val isInited: BehaviorSubject<Boolean> = BehaviorSubject.create()
    val lastUrl: BehaviorSubject<String> = BehaviorSubject.create()
    val progress: BehaviorSubject<Int> = BehaviorSubject.create()
    val webView: BehaviorSubject<WebView> = BehaviorSubject.create()

    fun WebView.launchResolve(uuid: String, result: String) {
        post {
            evaluateJavascript("""
                console.log('launchMap resolve', JSON.stringify(${jssdkName}.launchMap));
                var resolve = ${jssdkName}.launchMap['$uuid'].resolve;
                resolve($result);
                delete ${jssdkName}.launchMap['$uuid'];
            """.trimIndent()) {}
        }
    }
    fun WebView.launchReject(uuid: String, error: String) {
        post {
            evaluateJavascript("""
                console.log('launchMap reject', JSON.stringify(${jssdkName}.launchMap));
                var reject = ${jssdkName}.launchMap['$uuid'].reject;
                reject(Error('$error'));
                delete ${jssdkName}.launchMap['$uuid'];
            """.trimIndent()) {}
        }
    }

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

    fun loadUrlWithClear(url: String) {
        loadUrl(url)
        webView.value?.run {
            clearHistory()
            logHistory()
        }
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

    fun View.printTree(deep: Int=0) {
        Log.d("app24", "webView Tap [$deep](${id}) ${javaClass.name} ${width} ${height}")
        (this as? ViewGroup)?.let {
            for (child in it.children) {
                child.printTree(deep + 1)
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    fun Context.initX5() {
        val wv = WebView(this)
        wv.setOnLongClickListener {
            val r = wv.hitTestResult
            Log.d("app24", "webView Tap Hit: ${r.extra}")
            val rt = r.type
            if (rt == WebView.HitTestResult.IMAGE_TYPE || rt == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
                // 只有加载了 x5 内核才能获取， x5 内核没有 x86 支持。
//                val x5r = wv.x5HitTestResult
//                Log.d("app24", "webView Tap x5 Hit: ${x5r.hitTestPoint}")
                onLongClickImagePublisher.onNext(WebViewLongClickImageEvent(r.extra))
            }
            true
        }
        wv.setDownloadListener { url, userAgent, s3, mimeType, contentLength ->
            Log.d("app24", "webView Tap Download: s: $url, s2: $userAgent s3: $s3 s4: $mimeType, l: $contentLength")
            if (mimeType.startsWith("video")) {
                downloadVideo(url, mimeType)
            }
        }
//        wv.setOnTouchListener { view, motionEvent ->
//            view.printTree()
//            Log.d("app24", "webView Tap ${motionEvent.x} ${motionEvent.y}")
//            false
//        }
        webView.onNext(wv)

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

    fun Context.downloadVideo(url: String, mimeType: String) {
        httpScope.launch(Dispatchers.IO) {
            val r = httpClient.get(url)
            if (r.status.value in 200..299) {
                val videoData = r.readBytes()
                contentResolver.saveVideo(videoData, mimeType)
            }
        }
    }

    fun Context.downloadPicture(url: String) {
        httpScope.launch(Dispatchers.IO) {
            val r = httpClient.get(url)
            if (r.status.value in 200..299) {
                val imageData = r.readBytes()
                val mimeType = r.contentType()?.toString() ?: "image/jpeg"
                //
                Log.d("app24", "download Picture mime: $mimeType")
                contentResolver.savePicture(imageData, mimeType)
            }
        }
    }

    fun ContentResolver.savePicture(
        data: ByteArray,
        mime: String,
    ) : Uri? {
        val now = OffsetDateTime.now()
        val name = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss-SSS")
            .format(now)
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, mime)
            put(MediaStore.MediaColumns.DATE_MODIFIED, now.toEpochSecond())
            put(MediaStore.MediaColumns.SIZE, data.size)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                put(MediaStore.MediaColumns.IS_DOWNLOAD, true)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/App24")// 名字只能是 [DCIM, Movies, Pictures] 开头
            }
        }

        val furl = insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )

        return furl?.apply {
            openOutputStream(this)?.use {
                it.write(data)
            }
        }
    }

    fun ContentResolver.saveVideo(
        data: ByteArray,
        mime: String,
    ) : Uri? {
        val now = System.currentTimeMillis()
        val name = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS")
            .format(now)
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, mime)
            put(MediaStore.MediaColumns.DATE_MODIFIED, now / 1000)
            put(MediaStore.MediaColumns.SIZE, data.size)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                put(MediaStore.MediaColumns.IS_DOWNLOAD, true)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.Video.Media.RELATIVE_PATH, "Movies/App24") // 名字只能是 [DCIM, Movies, Pictures] 开头
            }
        }

        val furl = insert(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )

        return furl?.apply {
            openOutputStream(this)?.use {
                it.write(data)
            }
        }
    }

}