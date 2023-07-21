package com.example.app24.ui.page.demo


import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.Log
import androidx.compose.ui.geometry.Rect
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.AsyncImage
import com.example.app24.ui.DesignPreview
import com.example.app24.ui.sdp
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class CaptureController(
    val onSave: (Bitmap) -> Unit,
) {
    val onCallCapture: PublishSubject<Unit> = PublishSubject.create()
    fun capture() {
        onCallCapture.onNext(Unit)
    }
}

@Composable
fun rememberCaptureController(onSave: (Bitmap) -> Unit): CaptureController {
    return remember {
        CaptureController(onSave)
    }
}

@Composable
fun CaptureView(
    controller: CaptureController,
    content: @Composable () -> Unit,
) {
    val context = LocalContext.current
    val frameLayout = remember(context) {
        FrameLayout(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
            )
        }
    }

    val composeView = remember(frameLayout) {
        ComposeView(context).apply {
            setContent {
                content()
            }
            frameLayout.addView(
                this,
                ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                )
            )
        }
    }
    var bounds: Rect? by remember {
        mutableStateOf(null)
    }

    // 不能使用 Rx ，因为是 onNext 是同步， Flow 的 emit 是异步。
    val onCaptureFinal: MutableSharedFlow<Bitmap> = remember {
        MutableSharedFlow()
    }
    val scope = rememberCoroutineScope()

    val drawListener = remember(composeView, bounds) {
        ViewTreeObserver.OnDrawListener {
            val bitmap = Bitmap.createBitmap(
                bounds!!.width.toInt(),
                bounds!!.height.toInt(),
                Bitmap.Config.ARGB_8888,
            )
            val canvas = Canvas(bitmap)
            composeView.draw(canvas)
            scope.launch {
                onCaptureFinal.emit(bitmap)
            }
        }
    }

    LaunchedEffect(controller, drawListener , onCaptureFinal) {
        onCaptureFinal.collect {
            Log.d("app24", "CaptureView onCaptureFinal")
            composeView.viewTreeObserver.removeOnDrawListener(drawListener)
            controller.onSave(it)
        }
    }

    DisposableEffect(controller, drawListener , onCaptureFinal) {
        Log.d("app24", "CaptureView DisposableEffect $bounds")
        val onCallCaptureDisposable = controller.onCallCapture.subscribe {
            Log.d("app24", "CaptureView onCallCapture")
            composeView.viewTreeObserver.addOnDrawListener(drawListener)
        }

        onDispose {
            Log.d("app24", "CaptureView DisposableEffect onDispose")
            onCallCaptureDisposable.dispose()
        }
    }

    AndroidView(
        factory =
        {
            frameLayout
        },
        modifier = Modifier
            .onGloballyPositioned {
                bounds = it.boundsInRoot()
                Log.d("app24", "CaptureView onGloballyPositioned $bounds")
            }
    )
}

@Composable
fun CaptureViewImage2Page() {
    var bitmap: Bitmap? by remember {
        mutableStateOf(null)
    }
    val controller = rememberCaptureController {
        bitmap = it
    }

    Column(
        modifier = Modifier
            .border(1.sdp, Color.Black)
    ) {
        CaptureView(controller) {
            Text(
                text="文本",
                modifier = Modifier
                    .border(1.sdp, Color.Green)
            )
        }
        Button(onClick = { controller.capture() }) {
            Text("截取")
        }
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
fun CaptureViewImage2PagePreview() {
    DesignPreview {
        CaptureViewImage2Page()
    }
}