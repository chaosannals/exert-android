package com.example.jcm3wv.ui.widget

import android.webkit.WebView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun JcWebView(
    webView: WebView,
    modifier: Modifier=Modifier
) {
    val inspectionMode = LocalInspectionMode.current
    
    if (inspectionMode) {
        Spacer(modifier = modifier.background(Color.Black))
    } else {
        AndroidView(
            factory = { webView },
            modifier = modifier,
        )
    }
}