package com.example.calendardemo.ui.widget

import android.webkit.WebView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
//import org.x

// 官网关了无法官网下载 aar ，Github 上的项目没有 aar 。
@Composable
fun XWalkWebView() {
    val context = LocalContext.current
    val rawWebView by remember(context) {

        mutableStateOf(WebView(context))
    }
}