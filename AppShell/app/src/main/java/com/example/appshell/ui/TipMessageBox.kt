package com.example.appshell.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.zIndex
import com.example.appshell.ui.widget.DesignPreview
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
        Text(
            text = item.content,
            color = item.color,
        )
    }
}

@Composable
fun TipMessageBox(
    content: @Composable BoxScope.() -> Unit,
) {
    val queue = LocalTipQueue.current
    val items = remember {
        mutableStateListOf<TipItem>()
    }
    val coroutineScope = rememberCoroutineScope()

    var currentItem: TipItem? by remember {
        mutableStateOf(null)
    }

    suspend fun updateCurrent() {
        if (currentItem == null && items.isNotEmpty()) {
            Log.d("tip-message-page", "empty")
            currentItem = items.removeFirst()
            val now = currentItem
            withContext(Dispatchers.IO) {
                delay(currentItem!!.duration.toLong(DurationUnit.MILLISECONDS))
                withContext(Dispatchers.Main) {
                    if (now == currentItem) {
                        currentItem = null
                    }
                    updateCurrent()
                    Log.d("tip-message-page", "loop")
                }
            }
        }
    }

    LaunchedEffect(currentItem, items.size) {
        Log.d("tip-message-page", "launch")
        coroutineScope.launch { updateCurrent() }
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