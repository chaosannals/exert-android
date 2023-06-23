package com.example.appimop

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.tencent.smtt.export.external.TbsCoreSettings
import com.tencent.smtt.sdk.QbSdk
import com.tencent.smtt.sdk.ValueCallback
import com.tencent.smtt.sdk.WebView
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.readBytes
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

data class LoadUrlEvent(
    val key: String,
    val url: String,
)

data class WebViewOpenFileChooserEvent(
    val key: String,
    val webView: WebView,
    val filePathCallback: ValueCallback<Array<Uri>>,
    val acceptType: String,
)

data class WebViewLongClickImageEvent(
    val key: String,
    val url: String,
)

data class WebViewProgressChangedEvent(
    val key: String,
    val webView: WebView?,
    val value: Int,
)

data class WebViewDownloadVideoEvent(
    val key: String,
    val url: String,
    val mimeType: String,
)

data class WebViewDownloadVideoResult(
    val data: ByteArray,
    val mimeType: String,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as WebViewDownloadVideoResult

        if (!data.contentEquals(other.data)) return false
        if (mimeType != other.mimeType) return false

        return true
    }

    override fun hashCode(): Int {
        var result = data.contentHashCode()
        result = 31 * result + mimeType.hashCode()
        return result
    }
}

object X5WebMultiViewKit {
    private val viewMap: MutableMap<String, WebView> = mutableMapOf()
    private val httpClient by lazy {
        HttpClient(Android) {
            engine {
                connectTimeout = 4000
            }

            install(ContentNegotiation) {
                json(
                    Json {
                        prettyPrint = true
                        isLenient = true
                        ignoreUnknownKeys = true
                    },
                )
            }
        }
    }

    val onLoadUrlPublisher: PublishSubject<LoadUrlEvent> = PublishSubject.create()
    val onShowFileChooserPublisher: PublishSubject<WebViewOpenFileChooserEvent> = PublishSubject.create()
    val onLongClickImagePublisher: PublishSubject<WebViewLongClickImageEvent> = PublishSubject.create()
    val onDownloadVideoPublisher: PublishSubject<WebViewDownloadVideoEvent> = PublishSubject.create()
    val onProgressChangedPublisher: PublishSubject<WebViewProgressChangedEvent> = PublishSubject.create()

    fun Context.initX5() {
        // 允许远程调试
        WebView.setWebContentsDebuggingEnabled(true)

        QbSdk.initTbsSettings(mapOf(
            TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER to true,
            TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE to true,
        ))
        QbSdk.setDownloadWithoutWifi(true)
        // QbSdk.clearAllWebViewCache(this, true)
        QbSdk.initX5Environment(this, object: QbSdk.PreInitCallback {
            override fun onCoreInitFinished() {

            }

            override fun onViewInitFinished(p0: Boolean) {

            }
        })
    }

    //
    @Composable
    fun Context.rememberWebView(key: String): MutableState<WebView> {
        return remember(key) {
            mutableStateOf(ensureWebView(key))
        }
    }

    fun Context.ensureWebView(key: String): WebView {
        if (!viewMap.containsKey(key)) {
            Log.d("web-multi-view", "ensure new: $key")
            val webView = WebView(this)
            webView.setOnLongClickListener {
                val r = webView.hitTestResult
                val rt = r.type
                if (rt == WebView.HitTestResult.IMAGE_TYPE || rt == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
                    onLongClickImagePublisher.onNext(
                        WebViewLongClickImageEvent(key, r.extra)
                    )
                }
                true
            }
            webView.setDownloadListener { url, _, _, mimeType, _ ->
                if (mimeType.startsWith("video")) {
                    onDownloadVideoPublisher.onNext(
                        WebViewDownloadVideoEvent(key, url, mimeType)
                    )
                }
            }
            viewMap[key] = webView
        }
        Log.d("web-multi-view", "ensure: $key")
        return viewMap[key]!!
    }

    fun WebView.canGoBack2(): Boolean {
        val bfl = copyBackForwardList()
        return bfl.size > 1
    }

    fun CoroutineScope.download(url: String, action: (WebViewDownloadVideoResult?) -> Unit) {
        launch(Dispatchers.IO) {
            val response = httpClient.get(url) {

            }

            if (response.status.value in 200..299) {
                val videoData = response.readBytes()
                action(WebViewDownloadVideoResult(videoData, response.contentType()?.toString() ?: ""))
            }

            action(null)
        }
    }

    fun Context.saveVideo(
        data: ByteArray,
        mime: String,
    ) : Uri? {
        return contentResolver.saveVideo(data, mime)
    }

    @SuppressLint("SimpleDateFormat")
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
                put(MediaStore.Video.Media.RELATIVE_PATH, "Movies/YourDir")
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

    fun Context.savePicture(
        data: ByteArray,
        mime: String,
    ) : Uri? {
        return contentResolver.savePicture(data, mime)
    }

    @SuppressLint("SimpleDateFormat")
    fun ContentResolver.savePicture(
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
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/YourDir");
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
}