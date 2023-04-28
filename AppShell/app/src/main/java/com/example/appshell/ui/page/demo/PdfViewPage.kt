package com.example.appshell.ui.page.demo

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.appshell.ui.widget.DesignPreview
import com.example.appshell.ui.widget.PdfViewer
import java.io.File

@Composable
fun PdfViewPage() {
    val context = LocalContext.current
    val cache = File("${context.cacheDir}/ffffff")

    // 只有 webview 可以使用 file:///android_asset/file_name 形式
    context.assets.open("xl2409.pdf").use {input ->
        context.contentResolver.openOutputStream(Uri.fromFile(cache))?.use {
            it.write(input.readBytes())
        }
    }
    Column() {
        PdfViewer(cache)
    }
}

@Preview
@Composable
fun PdfViewPagePreview() {
    DesignPreview {
        PdfViewPage()
    }
}