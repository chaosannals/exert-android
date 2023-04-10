package com.example.appshell.ui.widget

import android.util.Log
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.tooling.preview.Preview
import com.example.appshell.ui.sdp
import kotlin.math.abs

private val animationSpec = tween<Float>(
    durationMillis = 444,
    easing = FastOutLinearInEasing,
)

@Composable
fun LazyDragColumn(
    modifier: Modifier = Modifier,
    state: LazyListState? = null,
    content: LazyListScope.() -> Unit
) {
    var scrollAll by remember {
        mutableStateOf(0f)
    }
    var dragAnimating by remember {
        mutableStateOf(false)
    }
    val listState = rememberLazyListState()
    val scrollState = rememberScrollableState {
        Log.d("lazy-drag-column", "scroll ${scrollAll}")
        scrollAll += it
        it
    }

    LaunchedEffect(scrollState.isScrollInProgress) {
        Log.d("lazy-drag-column", "isScrollInProgress ${scrollState.isScrollInProgress} ${dragAnimating}")
        if (!scrollState.isScrollInProgress && !dragAnimating) {
            Log.d("lazy-drag-column", "dragAnimating isScrollInProgress")
            dragAnimating = true
        }
    }
    
    LaunchedEffect(dragAnimating) {
        if (dragAnimating) {
            Log.d("lazy-drag-column", "dragAnimating")
            val sy = when {
                scrollAll > 0f -> 0f - scrollAll
//                scrollAll < contentSurplusHeight -> contentSurplusHeight.toFloat() - scrollAll
                else -> 0f
            }
            if (abs(sy) > 0.1f) {
                scrollState.animateScrollBy(sy, animationSpec)
            }
            dragAnimating = false
        }
    }
    
    var isDragging by remember {
        mutableStateOf(false)
    }
    
    LaunchedEffect(key1 = listState.canScrollBackward, key2 = listState.canScrollForward) {
        Log.d("lazy-drag-column", "b: ${listState.canScrollBackward} f: ${listState.canScrollForward}")
        isDragging = !listState.canScrollBackward || !listState.canScrollForward
    }

    Layout(
        modifier = modifier
            .scrollable(
                state = scrollState,
                enabled = isDragging,
                orientation = Orientation.Vertical,
            ),
        content =
        {
            LazyColumn(
                state = listState,
                userScrollEnabled = !isDragging,
                content = content,
            )
        },
    ) {ms, cs ->
        val sa = scrollAll.toInt()
        layout (cs.maxWidth, cs.maxHeight){
            ms.forEach {
                val p = it.measure(cs)
                p.place(0, sa, 0f)
            }
        }
    }
}

@Preview
@Composable
fun LazyDragColumnPreview() {
    DesignPreview {
        LazyDragColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                Box (
                    modifier = Modifier
                        .size(300.sdp)
                        .background(Color.Red)
                )
            }
            items(100) {
                val color = when (it % 3) {
                    0 -> Color.Red
                    1 -> Color.Green
                    2-> Color.Blue
                    else -> Color.White
                }
                Box (
                    modifier = Modifier
                        .size((100 + it).sdp)
                        .background(color)
                )
            }
        }
    }
}