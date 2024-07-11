package com.example.calendardemo.ui.page.web

import android.webkit.WebView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.calendardemo.ui.widget.GoogleWebView
import com.example.calendardemo.ui.widget.WebContent
import com.example.calendardemo.ui.widget.WebViewNavigator
import com.example.calendardemo.ui.widget.WebViewState
import com.example.calendardemo.ui.widget.rememberWebViewNavigator
import com.example.calendardemo.ui.widget.rememberWebViewState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext

val googleWebView = MutableStateFlow<WebView?>(null)
val googleWebViewFlow = googleWebView.asStateFlow()

private val googleWebViewState = MutableStateFlow(WebViewState(WebContent.NavigatorOnly))
private val googleWebViewStateFlow = googleWebViewState.asStateFlow()
private val googleWebViewNavigator = MutableStateFlow(WebViewNavigator(CoroutineScope(Dispatchers.IO)))

@Composable
fun GoogleWebViewPage() {
    val state by googleWebViewStateFlow.collectAsState()
    val navigator by googleWebViewNavigator.collectAsState()
    val webView by googleWebViewFlow.collectAsState()

    Column {
        Row {
            Button(onClick = {
                webView?.settings?.userAgentString = "UA TTTT"
            }) {
                Text(text = "UA change")
            }
            Text(text = webView?.settings?.userAgentString ?: "None")
        }
        GoogleWebView(
            state = state,
            navigator = navigator,
            modifier = Modifier
                .fillMaxSize(),
            factory =
            {
                webView ?: WebView(it).also {
                    googleWebView.value = it
                    it.loadUrl("https://m.bilibili.com")
                }
            },
        )
    }
}

@Preview
@Composable
fun GoogleWebViewPagePreview() {
    GoogleWebViewPage()
}