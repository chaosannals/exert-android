package com.example.appshell.ui.widget

import android.util.Log
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollDispatcher
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.appshell.ui.px2dp

@Composable
fun LazyNestedColumn(
    content: LazyListScope.() -> Unit
) {
    val density = LocalDensity.current
    val spaceMaxHeight = 48.dp
    val spaceMaxHeightPx = with(density) { spaceMaxHeight.roundToPx().toFloat() }
    var topPx by remember { mutableStateOf(0f) }
    var bottomPx by remember { mutableStateOf(0f) }

    val nestedScrollDispatcher = remember { NestedScrollDispatcher() }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                val delta = available.y
                val topOffset = topPx + delta
                val bottomOffset = bottomPx + delta
                topPx = topOffset.coerceIn(0f, spaceMaxHeightPx)
                bottomPx = bottomOffset.coerceIn(-spaceMaxHeightPx, 0f)

                //Log.d("lazy-nested-column", "delta: ${delta} no: ${topOffset} top: {$topPx} bottom: {$bottomPx}")
                return Offset.Zero
            }
        }
    }

    val draggableState = rememberDraggableState { delta ->
        Log.d("lazy-nested-column", "draging: $delta top: {$topPx} bottom: {$bottomPx}")

        val parentsConsumed = nestedScrollDispatcher.dispatchPreScroll(
            available = Offset(x = 0f, y = delta),
            source = NestedScrollSource.Drag
        )

        val adjustedAvailable = delta - parentsConsumed.y
        val totalConsumed = Offset(x = 0f, y = delta) + parentsConsumed
        val left = adjustedAvailable - delta

        nestedScrollDispatcher.dispatchPostScroll(
            consumed = totalConsumed,
            available = Offset(x = 0f, y = left),
            source = NestedScrollSource.Drag
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(
                connection =  nestedScrollConnection,
                dispatcher = nestedScrollDispatcher,
            ).draggable(
                orientation = Orientation.Vertical,
                // 没有触发
                onDragStarted =
                {
                    Log.d("lazy-nested-column", "drag start; top: {$topPx} bottom: {$bottomPx}")
                },

                // 没有触发
                onDragStopped =
                {
                    Log.d("lazy-nested-column", "drag stop; top: {$topPx} bottom: {$bottomPx}")
                    topPx = 0f
                    bottomPx = 0f
                },
                state = draggableState,
            ),
    ) {
        LazyColumn(
            contentPadding = PaddingValues(
                top = topPx.px2dp,
                bottom = (-bottomPx).px2dp,
            ),
            content = content,
        )
    }
}

@Preview
@Composable
fun LazyNestedColumnPreview() {
    DesignPreview {
        LazyNestedColumn {
            items(100) { index ->
                Text(
                    "I'm item $index",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                )
            }
        }
    }
}