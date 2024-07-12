package com.example.calendardemo.ui.page.web

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
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


private val googleWebViewState = MutableStateFlow(WebViewState(WebContent.NavigatorOnly))
private val googleWebViewNavigator = MutableStateFlow(WebViewNavigator(CoroutineScope(Dispatchers.IO)))

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun GoogleWebViewPage() {
    val state by googleWebViewState.collectAsState()
    val navigator by googleWebViewNavigator.collectAsState()


    DisposableEffect(Unit) {
        onDispose {
            // 实则 webView.saveState 后，webView.restoreState 大概率失败。
            googleWebViewState.value = WebViewState(WebContent.NavigatorOnly).apply {
                viewState = Bundle().apply {
                    state.webView?.saveState(this)
                }
                pageTitle = state.pageTitle
                lastLoadedUrl = state.lastLoadedUrl
            }
        }
    }

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
fun GoogleWebViewPagePreview() {
    GoogleWebViewPage()
}