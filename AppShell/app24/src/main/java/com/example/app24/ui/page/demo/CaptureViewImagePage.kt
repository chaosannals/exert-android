package com.example.app24.ui.page.demo

import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.Log
import android.view.ViewTreeObserver
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import com.example.app24.ui.DesignPreview
import com.example.app24.ui.sdp


@Composable
fun CaptureViewImagePage() {
    var bitmap: Bitmap? by remember {
        mutableStateOf(null)
    }

    Column (
        modifier = Modifier
            .statusBarsPadding()
            .border(1.sdp, Color.Black),
    ) {
        Box(
            modifier = Modifier
                .border(1.sdp, Color.Blue)
        ) {
            // 整个 Compose 只是一个 xml View，LocalView.current 获取到的。
            // 此处获得时，如果外围没有被多重 AndroidView 和 ComposeView 嵌套，那么相当于截屏。
            val v = LocalView.current

            val drawListener = remember {
                ViewTreeObserver.OnDrawListener {
                    val b = Bitmap.createBitmap(v.width, v.height, Bitmap.Config.ARGB_8888)
                    val canvas = Canvas(b)
                    v.draw(canvas)
                    bitmap = b
                    Log.d("app24", "CaptureViewImagePage onDraw")
                }
            }

            LaunchedEffect(bitmap) {
                if (bitmap != null) {
                    v.viewTreeObserver.removeOnDrawListener(drawListener)
                }
            }

            DisposableEffect(v) {
                Log.d("app24", "CaptureViewImagePage DisposableEffect")
                v.viewTreeObserver.addOnDrawListener(drawListener)
                onDispose {
                    if (bitmap == null) {
                        v.viewTreeObserver.removeOnDrawListener(drawListener)
                    }
                }
            }

            Text("文字")
        }

        Text(
            "bitmap: $bitmap",
            modifier = Modifier
                .border(1.sdp, Color.Green)
        )

        AsyncImage(
            model = bitmap,
            contentDescription = "截图",
            modifier = Modifier
                .border(1.sdp, Color.Red)
        )
    }
}

@Preview
@Composable
fun CaptureViewImagePagePreview() {
    DesignPreview {
        CaptureViewImagePage()
    }
}