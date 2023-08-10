package com.example.hlitdemo.ui.page

import android.webkit.WebView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun XWebViewPage() {
    val context = LocalContext.current
    val isInPreview = LocalInspectionMode.current
    val webView: WebView? by remember(context) {
        derivedStateOf {
            if (isInPreview) {
                WebView(context)
            } else {
                null
            }
        }
    }

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxSize()
    ) {
        if (!isInPreview) {
            AndroidView(
                factory = {
                    webView!!
                },
            ) {

            }
        }
    }
}

@Preview
@Composable
fun XWebViewPagePreview() {
    XWebViewPage()
}