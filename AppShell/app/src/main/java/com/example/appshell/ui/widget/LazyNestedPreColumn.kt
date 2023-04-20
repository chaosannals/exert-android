package com.example.appshell.ui.widget

import android.util.Log
import androidx.compose.animation.core.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import com.example.appshell.LocalTotalStatus
import com.example.appshell.ui.px2dp
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.max

private val animateSpec = spring(
    dampingRatio = Spring.DampingRatioHighBouncy,
    stiffness = Spring.StiffnessMedium,
    visibilityThreshold = 1f,
)

@Composable
fun LazyNestedPreColumn(
    modifier: Modifier = Modifier,
    content: LazyListScope.() -> Unit
) {
    val totalStatus = LocalTotalStatus.current
    val density = LocalDensity.current
    val spaceMaxHeight = 84.dp
    val spaceMaxHeightPx = with(density) { spaceMaxHeight.roundToPx().toFloat() }
    val coroutineScope = rememberCoroutineScope()
    val status = rememberLazyListState()

    val topPx = remember {
        Animatable(
            initialValue = 0f,
            visibilityThreshold = 1f,
        )
    }
    val bottomPx = remember {
        Animatable(
            initialValue = 0f,
            visibilityThreshold = 1f,
        )
    }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                val delta = available.y
                val topOffset = topPx.value + delta
                val bottomOffset = bottomPx.value + delta
                coroutineScope.launch {
                    topPx.snapTo(topOffset.coerceIn(0f, spaceMaxHeightPx))
                    bottomPx.snapTo(bottomOffset.coerceIn(-spaceMaxHeightPx, 0f))
                }
//                Log.d("lazy-nested-pre-column", "delta: ${delta} no: ${topOffset} top: {$topPx} bottom: {$bottomPx}")
                return Offset.Zero
            }

            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                val r = super.onPostScroll(consumed, available, source)
//                Log.d("lazy-nested-pre-column", "post r: ${r}")
                return r
            }

            // 结束开始
            override suspend fun onPreFling(available: Velocity): Velocity {
//                Log.d("lazy-nested-pre-column", "pre fling")
                // pre fling 和 post fling 之间可能会有 post pre scroll
                return super.onPreFling(available)
            }

            // 结束
            override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
//                Log.d("lazy-nested-pre-column", "post fling")
                topPx.animateTo(0f, animateSpec)
                bottomPx.animateTo(0f, animateSpec)
                return super.onPostFling(consumed, available)
            }
        }
    }

    // 类似 vue computed
    val index by remember {
        derivedStateOf {
            status.firstVisibleItemIndex
        }
    }


    LaunchedEffect(index) {
//        val h = status.layoutInfo.visibleItemsInfo.map { it.size }.reduce { a, b -> a + b }
//        val t = status.layoutInfo.visibleItemsInfo.map { it.index }.max()
        val c = status.layoutInfo.visibleItemsInfo.size
        val t = status.layoutInfo.totalItemsCount
        val v = (index.toFloat() / max(t - c,1)) * 100f
        Log.d("lazy-nested-pre-column", "index: ${index} ; c: ${c} ; t: ${t} ; ${v}%")
        totalStatus.scrollOffset.onNext(v)
    }

    Box(
        modifier = modifier
//            .fillMaxSize()
            .nestedScroll(nestedScrollConnection)
//            .draggable( // 使用 nestedScrollConnection 时无效
//                onDragStarted =
//                {
//                    Log.d("lazy-nested-pre-column", "drag start")
//                },
//                onDragStopped =
//                {
//                    Log.d("lazy-nested-pre-column", "drag stop")
//                },
//                state = rememberDraggableState {
//                    Log.d("lazy-nested-pre-column", "drag $it")
//                },
//                orientation = Orientation.Vertical,
//            )
    ) {
        LazyColumn(
            state = status,
            contentPadding = PaddingValues(
                top = abs(topPx.value).px2dp,
                bottom = abs(bottomPx.value).px2dp,
            ),
            modifier = Modifier
                .fillMaxSize(),
//                .draggable( // 无效
//                    onDragStarted =
//                    {
//                        Log.d("lazy-nested-pre-column", "drag start")
//                    },
//                    onDragStopped =
//                    {
//                        Log.d("lazy-nested-pre-column", "drag stop")
//                    },
//                    state = rememberDraggableState {
//                        Log.d("lazy-nested-pre-column", "drag $it")
//                    },
//                    orientation = Orientation.Vertical,
//                ),
            content = content,
        )
    }
}

@Preview
@Composable
fun LazyNestedPreColumnPreview() {
    DesignPreview {
        LazyNestedPreColumn {
            items(100) { index ->
                Text(
                    "I'm item $index",
                    modifier = Modifier
//                        .fillMaxWidth()
                        .padding(16.dp),
                )
            }
        }
    }
}