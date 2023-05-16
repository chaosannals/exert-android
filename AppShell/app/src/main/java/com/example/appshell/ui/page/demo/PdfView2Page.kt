package com.example.appshell.ui.page.demo

import android.util.AttributeSet
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
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
import com.joanzapata.pdfview.PDFView
// 这个预览不好，谷歌的安卓库一样，都是渲染成图片，没有选中文本等操作。而且兼容性不如谷歌的库

@Composable
fun PdfView2Page() {
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
            // 必须被挂到AndroidView后才能配置，因为上面提供了 null
            it.layoutParams.width = LayoutParams.MATCH_PARENT
            it.layoutParams.height = LayoutParams.MATCH_PARENT
//            it.fromAsset("xl2409.pdf") // 使用了这个 库 不支持的数据
//            it.fromAsset("bare.pdf")
//            it.fromAsset("text.pdf")
            it.fromAsset("text_image.pdf")
//            it.fromAsset("text_image_link.pdf") // 这个库不支持链接
//                .pages(0, 2, 1)
                .defaultPage(1)
                .swipeVertical(true)
                .showMinimap(true)
                .enableSwipe(true)
                .load()
        }
    }
}
