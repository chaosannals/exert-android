package com.example.calendardemo.ui.page.web

import android.annotation.SuppressLint
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow


private val googleWebViewNavigator = MutableStateFlow(WebViewNavigator(CoroutineScope(Dispatchers.IO)))

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun GoogleWebViewSavePage(state: WebViewState) {
    val navigator by googleWebViewNavigator.collectAsState()

    Column {
        Row {
            Button(onClick = {
                state.webView?.settings?.userAgentString = "UA TTTT"
            }) {
                Text(text = "UA change")
            }
            Text(text = state.webView?.settings?.userAgentString ?: "None")
        }
        GoogleWebView(
            state = state,
            navigator = navigator,
            modifier = Modifier
                .fillMaxSize(),
            factory =
            {
                WebView(it).apply {
                    loadUrl("https://m.bilibili.com")
                }
            },
            onCreated = {
                it.settings.run {
                    javaScriptEnabled = true
                    javaScriptCanOpenWindowsAutomatically = true
                }
            }
        )
    }
}

@Preview
@Composable
fun GoogleWebViewSavePagePreview() {
    GoogleWebViewSavePage(WebViewState(WebContent.NavigatorOnly))
}