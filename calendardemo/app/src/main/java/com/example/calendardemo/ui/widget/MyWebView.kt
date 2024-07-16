package com.example.calendardemo.ui.widget


import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout.LayoutParams
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

data class MyWebViewState(
    val webView: WebView?=null,
    val viewState: Bundle?=null,
    val canGoBack: Boolean = false,
)

class MyWebViewClient(
    val onCanGoBackChange: (Boolean) -> Unit,
) : WebViewClient() {
    override fun onPageStarted(view: WebView, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
    }

    override fun onPageFinished(view: WebView, url: String?) {
        super.onPageFinished(view, url)
    }

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        if (request?.url?.scheme == "bilibili") {
            return true
        }

        return super.shouldOverrideUrlLoading(view, request)
    }

    override fun doUpdateVisitedHistory(view: WebView, url: String?, isReload: Boolean) {
        super.doUpdateVisitedHistory(view, url, isReload)
        onCanGoBackChange(view.canGoBack())
    }

    override fun onReceivedError(
        view: WebView,
        request: WebResourceRequest?,
        error: WebResourceError?
    ) {
        super.onReceivedError(view, request, error)
    }
}

open class MyWebChromeClient : WebChromeClient() {
    override fun onReceivedTitle(view: WebView, title: String?) {
        super.onReceivedTitle(view, title)
    }

    override fun onReceivedIcon(view: WebView, icon: Bitmap?) {
        super.onReceivedIcon(view, icon)
    }

    override fun onProgressChanged(view: WebView, newProgress: Int) {
        super.onProgressChanged(view, newProgress)
    }
}


@Composable
fun MyWebView(
    state: MyWebViewState,
    modifier: Modifier=Modifier,
    factory: (Context) -> MyWebViewState = {
        MyWebViewState(WebView(it))
    },
) {
    BackHandler(state.canGoBack) {
        state.webView?.run {
            if (canGoBack()) {
                goBack()
            }
        }
    }

    BoxWithConstraints(modifier) {
        AndroidView(
            modifier=modifier,
            factory = {
                factory(it).run {
                    webView!!.apply {
                        if (parent != null) {
                            val p = parent as ViewGroup
                            val n = WebView(it)
                            n.id = id
                            p.removeView(this)
                            p.addView(n)
                        }
                        layoutParams = LayoutParams(
                            if (constraints.hasFixedWidth) LayoutParams.MATCH_PARENT else LayoutParams.WRAP_CONTENT,
                            if (constraints.hasFixedHeight) LayoutParams.MATCH_PARENT else LayoutParams.WRAP_CONTENT,
                        )
                    }
                }
            },
            onRelease = {
                //
            }
        ) {
            //
        }
    }
}