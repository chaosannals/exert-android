package com.example.anidemo.ui.widget

import android.util.Log
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.anidemo.ui.px2dp

// 第2版内部原件不使用滚动，一律由该组件支持滚动。
@Composable
fun PullDownBox2(
    modifier: Modifier = Modifier,
    maxHeightDp: Float = 100f,
    topContent: (@Composable (Float) -> Unit)? = null,
    bottomContent: (@Composable (Float) -> Unit)? = null,
    content: (@Composable (Float) -> Unit)? = null,
) {
    var contentTop by remember {
        mutableStateOf(0)
    }
    var contentHeight by remember {
        mutableStateOf(0)
    }
    var contentBottom by remember {
        mutableStateOf(0)
    }

    var scrollOffset by remember {
        mutableStateOf(0f)
    }
    var scrollAll by remember {
        mutableStateOf(0f)
    }
    val scrollState = rememberScrollableState() {
        val sv = when {
            scrollAll > 0f -> (it * 0.4f * (maxHeightDp - scrollAll) / maxHeightDp)
            else -> it
        }
        //val sv = it
        Log.d("anidemo", "delta: ${it} ${sv}  h: ${contentHeight}  a: ${scrollAll}")

        scrollAll = scrollAll + sv
        scrollOffset = when {
            scrollAll > maxHeightDp -> maxHeightDp
            else -> scrollAll
        }
        contentTop = when {
            scrollAll > 0f -> 0
            scrollAll < contentBottom -> {
                if (contentBottom < 0f) {
                    scrollAll = contentBottom.toFloat()
                    contentBottom
                } else {
                    scrollAll = 0f
                    0
                }
            }
            else -> scrollAll.toInt()
        }
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
        // 下拉回弹
        if (!scrollState.isScrollInProgress) {
            if (scrollAll > 0f) {
                scrollState.scrollBy(0f)
                scrollOffset = 0f
                scrollAll = 0f
            }
        }
    }

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
        modifier = modifier
            .scrollable(
                state = scrollState,
                orientation = Orientation.Vertical,
            ),
    ) {
        val downHeight = if (scrollState.isScrollInProgress) scrollOffset else scrollAniOffset

        // 顶部拖拽框
        Box (
            modifier = Modifier
                .fillMaxWidth()
                .height(downHeight.dp)
        ){
            topContent?.invoke(scrollOffset)
        }

        // 内容
        Layout(
            content = { content?.invoke(scrollOffset) },
            modifier = Modifier
                .background(Color.Blue)
                .border(1.dp, Color.Cyan)
        ) { ms, constraints ->
            val placebles = ms.map { m ->
                m.measure(constraints)
            }

            layout(constraints.maxWidth, constraints.maxHeight) {
                var allHeight = 0
                for (p in placebles) {
                    val x = (constraints.maxWidth - p.width) / 2
                    val y = allHeight + contentTop
                    allHeight += p.height
                    // Log.d("anidemo", "child w: ${p.width} h: ${p.height} a: ${allHeight}  t: ${contentTop}")
                    // p.placeRelative((constraints.maxWidth - p.width) / 2, allHeight)
                    p.place(x, y)
                }
                contentHeight = allHeight
                contentBottom = constraints.maxHeight - contentHeight
            }
        }

        // 底部拖拽框
        Box (
            modifier = Modifier
                .fillMaxWidth()
                .height(0.dp)
        ){
            bottomContent?.invoke(scrollOffset)
        }
    }
}

@Preview(widthDp = 375, heightDp = 668)
@Composable
fun PullDownBox2Preview() {
    Column(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        PullDownBox2(
            modifier = Modifier
                .fillMaxSize(),
            topContent = {
                Box(
                    modifier = Modifier
                        .background(Color.Red)
                        .fillMaxSize(),
                ) {

                }
            }
        ) {
            Text(
                text = "一些内容",
                modifier = Modifier
                    .border(1.dp, Color.Cyan)
            )
            for (i in 1..10) {
                Box(
                    modifier = Modifier
                        .background(Color.Green)
                        .border(1.dp, Color.Cyan)
                        .size((i * 10).dp, (i * 100).dp),
                ) {
                    Text(text = i.toString())
                }
            }
            Text(
                text = "底下一些内容",
                modifier = Modifier
                    .border(1.dp, Color.Cyan)
            )
        }
    }
}