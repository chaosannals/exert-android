package com.example.appimop

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.tencent.smtt.sdk.ValueCallback
import com.tencent.smtt.sdk.WebView
import io.reactivex.rxjava3.subjects.PublishSubject

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
    val url: String?,
)

data class WebViewProgressChangedEvent(
    val key: String,
    val webView: WebView?,
    val value: Int,
)

object X5WebMultiViewKit {
    private val viewMap: MutableMap<String, WebView> = mutableMapOf()
    val onLoadUrlPublisher: PublishSubject<LoadUrlEvent> = PublishSubject.create()
    val onShowFileChooserPublisher: PublishSubject<WebViewOpenFileChooserEvent> = PublishSubject.create()
    val onLongClickImagePublisher: PublishSubject<WebViewLongClickImageEvent> = PublishSubject.create()
    val onProgressChangedPublisher: PublishSubject<WebViewProgressChangedEvent> = PublishSubject.create()

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
            viewMap[key] = WebView(this)
        }
        return viewMap[key]!!
    }

    fun WebView.canGoBack2(): Boolean {
        val bfl = copyBackForwardList()
        return bfl.size > 1
    }
}