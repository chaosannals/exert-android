package com.example.calendardemo.ui.page.web

import android.annotation.SuppressLint
import android.webkit.WebView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.calendardemo.ui.widget.MyWebChromeClient
import com.example.calendardemo.ui.widget.MyWebView
import com.example.calendardemo.ui.widget.MyWebViewClient
import com.example.calendardemo.ui.widget.MyWebViewState
import kotlinx.coroutines.flow.MutableStateFlow

val myWebViewState = MutableStateFlow(MyWebViewState())


// 在 history.go(-1) 无效的时候，location.reload 就会恢复可用。
// 折中方案：实测 location.reload();history.go(-1); 如果可以返回就不会刷新，如果出问题了，会刷新，下次可返回。
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun MyWebViewPage() {
    val state by myWebViewState.collectAsState()

    Column {
        Row {
            Button(onClick = {
                state.webView?.settings?.userAgentString = "UA TTTT"
            }) {
                Text(text = "UA change")
            }
            Text(text = state.webView?.settings?.userAgentString ?: "None")
        }
        MyWebView(
            state=state,
            modifier = Modifier.fillMaxSize(),
            factory = {
                state.copy(webView =
                    state.webView ?: WebView(it).apply {
                        settings.run {
                            javaScriptEnabled = true
                            javaScriptCanOpenWindowsAutomatically = true
                        }
                        webViewClient = MyWebViewClient {
                            myWebViewState.value = state.copy(canGoBack = it)
                        }
                        webChromeClient = MyWebChromeClient()

                        loadUrl("https://m.bilibili.com")
                    }
                ).also { myWebViewState.value = it }
            }
        )
    }
}

@Preview
@Composable
fun MyWebViewPagePreview() {
    MyWebViewPage()
}