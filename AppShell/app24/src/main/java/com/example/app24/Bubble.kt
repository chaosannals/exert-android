package com.example.app24

import androidx.compose.runtime.*
import java.util.UUID

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