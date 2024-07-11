package com.example.calendardemo.ui.page.web

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

val myWebView = MutableStateFlow<WebView?>(null)
val myWebViewState = MutableStateFlow(MyWebViewState())

@Composable
fun MyWebViewPage() {
    val webView by myWebView.collectAsState()
    val state by myWebViewState.collectAsState()

    Column {
        Row {
            Button(onClick = {
                webView?.settings?.userAgentString = "UA TTTT"
            }) {
                Text(text = "UA change")
            }
            Text(text = webView?.settings?.userAgentString ?: "None")
        }
        MyWebView(
            state=state,
            modifier = Modifier.fillMaxSize(),
            factory = {
                webView ?: WebView(it).apply {
                    webViewClient = MyWebViewClient {
                        myWebViewState.value = state.copy(canGoBack = it)
                    }
                    webChromeClient = MyWebChromeClient()

                    myWebView.value = this
                    loadUrl("https://m.bilibili.com")
                }
            }
        )
    }
}

@Preview
@Composable
fun MyWebViewPagePreview() {
    MyWebViewPage()
}