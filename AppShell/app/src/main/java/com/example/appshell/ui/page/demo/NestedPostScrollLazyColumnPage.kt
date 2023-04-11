package com.example.appshell.ui.page.demo

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollDispatcher
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.appshell.ui.widget.DesignPreview
import kotlin.math.roundToInt

@Composable
fun NestedPostScrollLazyColumnPage() {
    val basicState = remember { mutableStateOf(0f) }
    val minBound = -100f
    val maxBound = 100f

    val onNewDelta: (Float) -> Float = { delta ->
        val oldState = basicState.value
        val newState = (basicState.value + delta).coerceIn(minBound, maxBound)
        basicState.value = newState
        newState - oldState
    }

    // 调度器，手工完成调度过程，即不触发 onPreScroll 和 onPostScroll ，过程由调度器调用时触发
    // 子项不可以带滚动。
    val nestedScrollDispatcher = remember { NestedScrollDispatcher() }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            // 滚动起效后
            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                val vertical = available.y
                val weConsumed = onNewDelta(vertical)

                Log.d("nested-post-scroll-lazy-column", "post: ${vertical}")
                return Offset(x = 0f, y = weConsumed)
            }
        }
    }

    Box(
        modifier = Modifier
            .size(100.dp)
            .background(Color.LightGray)
            // 指定嵌套滚动条
            .nestedScroll(
                // 这两个互斥
                // 当子项带滚动时 nestedScrollConnection 起效，
                // 当子项没有滚动时 nestedScrollDispatcher 起效。
                connection = nestedScrollConnection,
                dispatcher = nestedScrollDispatcher,
            )
            .draggable(
                orientation = Orientation.Vertical,
                onDragStarted =
                {
                    Log.d("nested-post-scroll-lazy-column", "drag start")
                },
                onDragStopped =
                {
                    Log.d("nested-post-scroll-lazy-column", "drag stop")
                },
                state = rememberDraggableState { delta ->
                    Log.d("nested-post-scroll-lazy-column", "drag: ${delta}")

                    // 在拖拉的时候手动调度 但不会触发 onPreScroll
                    val parentsConsumed = nestedScrollDispatcher.dispatchPreScroll(
                        available = Offset(x = 0f, y = delta),
                        source = NestedScrollSource.Drag
                    )

                    // 进行定制
                    val adjustedAvailable = delta - parentsConsumed.y
                    val weConsumed = onNewDelta(adjustedAvailable)
                    val totalConsumed = Offset(x = 0f, y = weConsumed) + parentsConsumed
                    val left = adjustedAvailable - weConsumed

                    // 手动调度 但不会触发 onPostScroll
                    nestedScrollDispatcher.dispatchPostScroll(
                        consumed = totalConsumed,
                        available = Offset(x = 0f, y = left),
                        source = NestedScrollSource.Drag
                    )
                    // we won't dispatch pre/post fling events as we have no flinging here, but the
                    // idea is very similar:
                    // 1. dispatch pre fling, asking parents to pre consume
                    // 2. fling (while dispatching scroll events like above for any fling tick)
                    // 3. dispatch post fling, allowing parent to react to velocity left
                }
            )
    ) {
//        Text(
//            "State: ${basicState.value.roundToInt()}",
//            modifier = Modifier
//                .align(Alignment.Center)
//
//        )
        LazyColumn(
            userScrollEnabled=false, // 只要关闭滚动才能被父级的 nested 调度器接收。
        ) {
            items(100) {
                Text(
                    "State ${it}: ${basicState.value.roundToInt()}",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Preview
@Composable
fun NestedPostScrollLazyColumnPagePreview() {
    DesignPreview {
        NestedPostScrollLazyColumnPage()
    }
}