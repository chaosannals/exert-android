package com.example.app24

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.UUID

// 此实现使用 Rx 分发，bubbleUp 只发起第一层，所以线程安全。
// bubbleUp 是没有 for 循环的，执行也是很快。

val LocalBubbleRx = staticCompositionLocalOf {
    BubbleRx("root")
}

data class BubbleRxEvent(
    val sourceKey: String,
    val tag: String,
    val data: Any?,
)

class BubbleRx (
    val key: String,
    val onBubbleUp: PublishSubject<BubbleRxEvent> = PublishSubject.create(),
) {
    fun bubbleUp(tag: String, data: Any?=null) {
        val event = BubbleRxEvent(key, tag, data)
        onBubbleUp.onNext(event)
    }
}

@Composable
fun BubbleRxProvider(
    onBubbleUp: (BubbleRxEvent) -> Boolean = { true },
    content: @Composable () -> Unit,
) {
    val parent = LocalBubbleRx.current
    val here by remember {
        val key = UUID.randomUUID().toString()
        mutableStateOf(BubbleRx(key))
    }

    DisposableEffect(parent, here, onBubbleUp) {
        val hereDisposable = here.onBubbleUp.subscribe {
            if (onBubbleUp(it)) {
                parent.onBubbleUp.onNext(it)
            }
        }
        onDispose {
            hereDisposable.dispose()
        }
    }

    CompositionLocalProvider(
        LocalBubbleRx provides here
    ) {
        content()
    }
}