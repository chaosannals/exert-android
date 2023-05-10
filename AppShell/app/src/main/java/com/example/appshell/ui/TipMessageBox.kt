package com.example.appshell.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.zIndex
import com.example.appshell.ui.widget.DesignPreview
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.minus
import java.time.LocalDateTime
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

data class TipItem (
    val content: String,
    val color: Color,
    val duration: Duration,
)

val LocalTipQueue = staticCompositionLocalOf<PublishSubject<TipItem>> {
    error("No Tip Queue !!")
}

@Composable
fun rememberTipQueue(): PublishSubject<TipItem> {
    val r by remember {
        val ps = PublishSubject.create<TipItem>()
        mutableStateOf(ps)
    }
    return r
}

fun PublishSubject<TipItem>.tip(
    message: String,
    color: Color = Color.White,
    duration: Duration = 1.4.toDuration(DurationUnit.SECONDS),
) {
    onNext(
        TipItem(
            content = message,
            color = color,
            duration = duration,
        ),
    )
}

@Composable
fun TipMessageToast(
    item: TipItem,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .sizeIn(maxWidth = 345.sdp)
            .zIndex(99999f)
            .background(
                color = Color(0xA1000000),
                shape = RoundedCornerShape(4.sdp)
            )
            .padding(14.sdp, 14.sdp)
        ,
        contentAlignment = Alignment.Center,
    ) {
        Column(
            verticalArrangement=Arrangement.Center,
            horizontalAlignment=Alignment.CenterHorizontally,
        ) {
            Image(
                imageVector = Icons.Default.Info,
                contentDescription = "info",
                alpha = 0.4f,
                colorFilter = ColorFilter.tint(Color.White)
            )
            Text(
                text = item.content,
                color = item.color,
            )
        }
    }
}

// 1.x 为一种实现，（此方案需要独立的协程作用域，或使用 rememberUpdateState 传递 lambda 使得 lambda 不被取消）
// 2.x 为另一种实现，此方案没有 1 及时，因为复用了 LaunchedEffect 的协程作用域和条件调度（LaunchedEffect 的调度不及时）
@Composable
fun TipMessageBox(
    content: @Composable BoxScope.() -> Unit,
) {
    val queue = LocalTipQueue.current
    val items = remember {
        mutableStateListOf<TipItem>()
    }
    val itemsSize = remember {
        derivedStateOf {
            items.size
        }
    }
    // 1.1
//    val coroutineScope = rememberCoroutineScope()

    // 2.1
    var currentStart by remember {
        mutableStateOf(Clock.System.now())
    }

    var currentItem: TipItem? by remember {
        mutableStateOf(null)
    }

    // 1.2
//    suspend fun updateCurrent() {
//        if (currentItem == null && items.isNotEmpty()) {
//            Log.d("tip-message-page", "empty")
//            currentItem = items.removeFirst()
//            val now = currentItem
//            withContext(Dispatchers.IO) {
//                delay(currentItem!!.duration.toLong(DurationUnit.MILLISECONDS))
//                withContext(Dispatchers.Main) {
//                    if (now == currentItem) {
//                        currentItem = null
//                    }
//                    updateCurrent()
//                    Log.d("tip-message-page", "loop")
//                }
//            }
//        }
//    }

    // 1.3
//    LaunchedEffect(currentItem, items.size) {
//        Log.d("tip-message-page", "launch")
//        coroutineScope.launch { updateCurrent() }
//    }

    // 2.2 首次触发 items.size = 0 currentItem = null
    // Launched 条件触发会杀死协程重启一条，但是不是马上，所以 items.removeFirst 调用后会有一些伴生现象
    LaunchedEffect(itemsSize) {
        val now = Clock.System.now()
        if (currentItem == null) {
            Log.d("tip-message-box", "remove 1 （${items.size}）")
            currentStart = now
            currentItem = if (items.isNotEmpty()) items.removeFirst() else null
        } else {
            Log.d("tip-message-box", "start 2 ${currentItem == null} （${items.size}）")
            val d = now.minus(currentStart)
            val l = currentItem!!.duration - d
            Log.d("tip-message-box", "duration: （${l.toDouble(DurationUnit.SECONDS)}）")
            if (l.isNegative()) {
                Log.d("tip-message-box", "remove 2 （${items.size}）")
                currentStart = now
                currentItem = if (items.isNotEmpty()) items.removeFirst() else null
            }
            Log.d("tip-message-box", "start 3 ${currentItem == null} （${items.size}）")
            launch(Dispatchers.IO) {
                delay(l)
                Log.d("tip-message-box", "remove 3 （${items.size}）")
                currentStart = Clock.System.now()
                currentItem = if (items.isNotEmpty()) items.removeFirst() else null
            }
        }
        // 除了 start 和 remove 3 外其他分支都同步执行了 items.removeFirst，
        // 然而，执行了此句便证明了触发条件后协程不是立马关闭而是会执行一段时间。
        Log.d("tip-message-box", "final ${currentItem == null} （${items.size}）")
    }

    DisposableEffect(queue) {
        val subject = queue
            .subscribe { items.add(it) }
        onDispose {
            subject.dispose()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        currentItem?.let {
            TipMessageToast(
                it,
                modifier = Modifier
                    .align(Alignment.Center)
                    .clickable
                    {
                        currentItem = null
                    },
            )
        }
        content()
    }
}

@Preview
@Composable
fun TipMessageBoxPreview() {
    DesignPreview {
        TipMessageBox() {
            TipMessageToast(
                item = TipItem(
                    content = "提示",
                    color = Color.White,
                    duration = 1.4.toDuration(DurationUnit.SECONDS),
                ),
            )
        }
    }
}