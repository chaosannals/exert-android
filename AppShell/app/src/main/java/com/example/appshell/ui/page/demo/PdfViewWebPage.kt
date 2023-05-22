package com.example.appshell.ui.page.demo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.appshell.db.WebViewConf
import com.example.appshell.ui.widget.DesignPreview
import com.example.appshell.ui.widget.X5WebShell

@Composable
fun PdfViewWebPage() {
    val context = LocalContext.current

    // 通过 pdf.js 在浏览器预览。
    val conf: WebViewConf by remember {
        mutableStateOf(WebViewConf(
            id = 1,
            startUrl = "file:///android_asset/index.html",
            valName = "app",
        ))
    }

    X5WebShell(conf) {

    }
}

@Preview
@Composable
fun PdfViewWebPagePreview() {
    DesignPreview {
        PdfViewWebPage()
    }
}