package com.example.anidemo.ui.widget

import android.util.Log
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.DragStartHelper

// 第一版内部滚轴由原件自己支持会有冲突。
@Composable
fun PullDownBox(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    maxHeightDp: Float = 100f,
    scrollListener: ((Float) -> Unit)? = null,
    finishedListener: ((Float) -> Unit)? = null,
    topContent: (@Composable (Float) -> Unit)? = null,
    content: (@Composable (Float) -> Unit)? = null,
) {
    var scrollOffset by remember {
        mutableStateOf(0f)
    }
    var scrollAll by remember {
        mutableStateOf(0f)
    }
    val scrollState = rememberScrollableState() {
        val sv = it * 0.4f * (maxHeightDp - scrollAll) / maxHeightDp
        Log.d("anidemo", "delta: ${it} ${sv}")
        scrollAll += sv
        scrollOffset = if (scrollAll > maxHeightDp) maxHeightDp else scrollAll
        scrollListener?.invoke(scrollOffset)
        sv
    }
    val scrollAniOffset by animateFloatAsState(
        targetValue = scrollOffset,
        animationSpec = tween(
            durationMillis = 144,
            easing = FastOutLinearInEasing,
        ),
    )

    LaunchedEffect(scrollState.isScrollInProgress) {
        Log.d("anidemo", "scroll state change: ${scrollState.isScrollInProgress}")
        if (!scrollState.isScrollInProgress) {
            finishedListener?.invoke(scrollOffset)
            scrollState.scrollBy(0f)
            scrollOffset = 0f
            scrollAll = 0f
            scrollListener?.invoke(scrollOffset)
        }
    }

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .scrollable(
                state = scrollState,
                orientation = Orientation.Vertical,
                enabled = enabled,
            ),
    ) {
        val downHeight = if (scrollState.isScrollInProgress) scrollOffset else scrollAniOffset
        Box (
            modifier = Modifier
                .fillMaxWidth()
                .height(downHeight.dp)
        ){
            topContent?.invoke(scrollOffset)
        }
        content?.invoke(scrollOffset)
    }
}

@Preview
@Composable
fun PullDownBoxPreview() {
    PullDownBox()
}