package com.example.app24

import androidx.compose.runtime.*
import kotlinx.coroutines.flow.MutableSharedFlow
import java.util.UUID

// 改类实现基本和 Rx 类似，区别是 collect 和 emit 都被 suspend 染色，需要额外的协程作用域，写起来会比较麻烦。

val LocalBubbleFlow = staticCompositionLocalOf {
    BubbleFlow("root")
}

data class BubbleFlowEvent(
    val sourceKey: String,
    val tag: String,
    val data: Any?,
)

class BubbleFlow (
    val key: String,
    val onBubbleUp: MutableSharedFlow<BubbleFlowEvent> = MutableSharedFlow(),
) {
    suspend fun bubbleUp(tag: String, data: Any?=null) {
        val event = BubbleFlowEvent(key, tag, data)
        onBubbleUp.emit(event)
    }
}



@Composable
fun BubbleFlowProvider(
    onBubbleUp: (BubbleFlowEvent) -> Boolean = { true },
    content: @Composable () -> Unit,
) {
    val parent = LocalBubbleFlow.current
    val here by remember {
        val key = UUID.randomUUID().toString()
        mutableStateOf(BubbleFlow(key))
    }


    LaunchedEffect(parent, here, onBubbleUp) {
        here.onBubbleUp.collect {
            if (onBubbleUp(it)) {
                parent.onBubbleUp.emit(it)
            }
        }
    }

    CompositionLocalProvider(
        LocalBubbleFlow provides here
    ) {
        content()
    }
}
