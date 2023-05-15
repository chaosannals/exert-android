package com.example.appshell.ui.page.demo

import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.example.appshell.ui.sdp
import com.example.appshell.ui.widget.DesignPreview

// 库改了组名，但是代码的名字空间还是原来的。
import com.github.barteksc.pdfviewer.PDFView

@Composable
fun PdfView3Page() {
    val context = LocalContext.current
    val view = remember {
        // 此处 xml 属性设置了 null ，不能在此处去设置布局。
        PDFView(context, null)
    }

    Column(
        modifier = Modifier
            .statusBarsPadding()
    ) {
        AndroidView(
            factory = { view },
            modifier = Modifier
                .border(1.sdp, Color.Cyan)
        ) {
            // 必须被挂到AndroidView后才能配置
            it.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            it.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            it.fromAsset("text_image_link.pdf")
                .enableAnnotationRendering(true)
                .enableAntialiasing(true)
                .enableDoubletap(true)
                .enableSwipe(true)
                .linkHandler {
                    Toast.makeText(context, "点击了：${it.link.uri}", Toast.LENGTH_SHORT).show()
                }
                .load()
        }
    }

}

@Preview
@Composable
fun PdfView3PagePreview() {
    DesignPreview {
        PdfView3Page()
    }
}