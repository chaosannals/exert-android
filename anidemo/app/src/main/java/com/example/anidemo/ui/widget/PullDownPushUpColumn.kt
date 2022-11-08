package com.example.anidemo.ui.widget

import android.util.Log
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.abs

fun hinderScroll(it: Float, sa: Float, mv: Float, lv: Float) : Float {
    return (it * 0.4f * (mv - abs(sa - lv)) / mv)
}

val animationSpec = tween<Float>(
    durationMillis = 144,
    easing = FastOutLinearInEasing,
)

@Composable
fun PullDownPushUpColumn(
    modifier: Modifier = Modifier,
    maxHeight: Float = 200f,
    topContent: (@Composable (Float) -> Unit)? = null,
    bottomContent: (@Composable (Float) -> Unit)? = null,
    content: (@Composable (Float) -> Unit)? = null,
) {
    var contentHeight by remember {
        mutableStateOf(0)
    }
    var contentSurplusHeight by remember {
        mutableStateOf(0)
    }

    var scrollAll by remember {
        mutableStateOf(0f)
    }

    val scrollState = rememberScrollableState {
        val sv = when {
            scrollAll > 0f -> hinderScroll(it , scrollAll, maxHeight, 0f)
            scrollAll < contentSurplusHeight -> hinderScroll(it , scrollAll, maxHeight, contentSurplusHeight.toFloat())
            else -> it
        }
        scrollAll += sv
        sv
    }

    LaunchedEffect(scrollState.isScrollInProgress) {
        if (!scrollState.isScrollInProgress) {
            when {
                scrollAll > 0f -> {
                    Log.d("pulldownpushup", "sa > 0f: ${scrollAll}")
                    scrollAll = 0f
                    scrollState.animateScrollBy(0f, animationSpec)
                }
                scrollAll < contentSurplusHeight -> {
                    Log.d("pulldownpushup", "sa < csh: ${scrollAll} | ${contentSurplusHeight}")
                    scrollAll = contentSurplusHeight.toFloat()
                    scrollState.animateScrollBy(scrollAll, animationSpec)
                }
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
        Layout(
            modifier = Modifier.border(1.dp, Color.Cyan),
            content = { content?.invoke(scrollAll) },
        ) { ms, cs ->
            val ps = ms.map {
                it.measure(cs)
            }

            val sai = scrollAll.toInt()
            layout(cs.maxWidth, cs.maxHeight) {
                var heightSum = 0
                for (p in ps) {
                    val x = 0
                    val y = heightSum + sai
                    heightSum += p.height
                    p.place(x, y)
                }
                contentHeight = heightSum
                contentSurplusHeight = if (cs.maxHeight > contentHeight) 0 else cs.maxHeight - contentHeight
                Log.d("pulldownpushup", "cmh: ${cs.maxHeight} | ch: ${contentHeight} | csh: ${contentSurplusHeight}")
            }
        }
    }
}

@Preview(widthDp = 375, heightDp = 668)
@Composable
fun PullDowPushUpColumnPreview() {
    PullDownPushUpColumn() {
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