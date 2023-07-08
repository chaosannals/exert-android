package com.example.app24

import androidx.compose.runtime.*
import java.util.UUID

// 此实现在 bubbleUp 里面使用了循环去冒泡，此过程依赖调用的环境，存在线程安全问题。
// 如示例只是在 UI 中执行没有问题。复杂环境下 bubbleUp 大概率会在 IO 线程或者其他非 Main 线程执行就会有问题。
// 而且整个冒泡过程由 bubbleUp 同步执行，可能会出现卡顿。


val LocalBubble = staticCompositionLocalOf {
    Bubble("root")
}

data class BubbleEvent(
    val sourceKey: String,
    val tag: String,
    val data: Any?,
)

class Bubble (
    private val key: String,
    private val parent: Bubble?=null,
    val onBubbleUp: (BubbleEvent) -> Boolean = { true },
) {
    fun bubbleUp(tag: String, data: Any?=null) {
        val event = BubbleEvent(key, tag, data)
        var target: Bubble? = this
        while (target != null && target.onBubbleUp(event)) {
            target = target.parent
        }
    }
}

@Composable
fun BubbleProvider(
    onBubbleUp: (BubbleEvent) -> Boolean = { true },
    content: @Composable () -> Unit,
) {
    val parent = LocalBubble.current
    val here by remember(parent, onBubbleUp) {
        val key = UUID.randomUUID().toString()
        mutableStateOf(Bubble(key, parent, onBubbleUp))
    }

    CompositionLocalProvider(
        LocalBubble provides here
    ) {
        content()
    }
}