package com.example.calendardemo.ui.widget

import android.webkit.WebView
import android.widget.FrameLayout.LayoutParams
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun MyWebView(
    modifier: Modifier=Modifier
) {
    var rawWebView: WebView? by remember {
        mutableStateOf(null)
    }
    var isCanGoBack: Boolean by remember(rawWebView) {
        mutableStateOf(false)
    }
    BackHandler(isCanGoBack) {
        rawWebView?.goBack()
    }

    BoxWithConstraints(modifier) {
        AndroidView(
            modifier=modifier,
            factory = {
                WebView(it).apply {
                    layoutParams = LayoutParams(
                        if (constraints.hasFixedWidth) LayoutParams.MATCH_PARENT else LayoutParams.WRAP_CONTENT,
                        if (constraints.hasFixedHeight) LayoutParams.MATCH_PARENT else LayoutParams.WRAP_CONTENT,
                    )
                }
            },
        ) {

        }
    }
}