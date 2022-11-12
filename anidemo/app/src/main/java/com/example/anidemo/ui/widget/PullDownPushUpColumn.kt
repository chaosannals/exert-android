package com.example.anidemo.ui.widget

import android.util.Log
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.anidemo.ui.px2dp
import java.util.Calendar
import kotlin.math.abs

fun hinderScroll(it: Float, sa: Float, mv: Float, lv: Float) : Float {
    return (it * 0.4f * (mv - abs(sa - lv)) / mv)
}

val animationSpec = tween<Float>(
    durationMillis = 444,
    easing = FastOutLinearInEasing,
)

@Composable
fun PullDownPushUpColumn(
    modifier: Modifier = Modifier,
    maxHeight: Float = 200f,
    onPullDownFinal: (() -> Unit)? = null,
    onPullDown:((Float) -> Unit)? = null,
    onPushUpFinal: (() -> Unit)? = null,
    onPushUp:((Float) -> Unit)? = null,
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

    var pullPushLastAt: Long by remember {
        mutableStateOf(0L)
    }
    var pullPushAnimating by remember {
        mutableStateOf(false)
    }

    val scrollState = rememberScrollableState {
        Log.d("pulldownpushup", "scrolling: ${it}")

        val sv = when {
            !pullPushAnimating && scrollAll > 0f -> {
                if (pullPushLastAt == 0L) {
                    pullPushLastAt = Calendar.getInstance().timeInMillis
                }
                onPullDown?.let {
                    if (pullPushLastAt > 0L) {
                        it(scrollAll)
                    }
                }
                hinderScroll(it, scrollAll, maxHeight, 0f)
            }
            !pullPushAnimating && scrollAll < contentSurplusHeight -> {
                if (pullPushLastAt == 0L) {
                    pullPushLastAt = Calendar.getInstance().timeInMillis
                }
                onPushUp?.let {
                    if (pullPushLastAt > 0L) {
                        it(scrollAll)
                    }
                }
                hinderScroll(it, scrollAll, maxHeight, contentSurplusHeight.toFloat())
            }
            else -> it
        }
        scrollAll += sv
        sv
    }

    LaunchedEffect(scrollState.isScrollInProgress) {
        if (!scrollState.isScrollInProgress && !pullPushAnimating) {
            val now = Calendar.getInstance().timeInMillis
            val duration = now - pullPushLastAt
            Log.d("pulldownpushup", "now: ${now} duration: ${duration}")

            when {
                scrollAll > 0f -> {
                    Log.d("pulldownpushup", "sa > 0f: ${scrollAll}")
                    onPullDownFinal?.let {
                        Log.d("pulldownpushup", "pullDown: ${pullPushLastAt} duration: ${duration}")
                        if (duration > 1000L && pullPushLastAt > 0L) {
                            it()
                        }
                    }
                    pullPushAnimating = true
                }
                scrollAll < contentSurplusHeight -> {
                    Log.d("pulldownpushup", "sa < csh: ${scrollAll} | ${contentSurplusHeight}")
                    onPushUpFinal?.let {
                        Log.d("pulldownpushup", "pushUp: ${pullPushLastAt} duration: ${duration}")
                        if (duration > 1000L && pullPushLastAt > 0L) {
                            it()
                        }
                    }
                    pullPushAnimating = true
                }
            }
        }
        pullPushLastAt = 0L
    }

    LaunchedEffect(pullPushAnimating) {
        if (pullPushAnimating) {
            val sy = when {
                scrollAll > 0f -> 0f - scrollAll
                scrollAll < contentSurplusHeight -> contentSurplusHeight.toFloat() - scrollAll
                else -> 0f
            }
            if (abs(sy) > 0.1f) {
                Log.d("pulldownpushup", "start animation ${sy}")
                val r = scrollState.animateScrollBy(sy, animationSpec)
                Log.d("pulldownpushup", "end animation: ${r}")
            }
            pullPushAnimating = false
        }
    }

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
        modifier = modifier
            .scrollable(
                state = scrollState,
                enabled = !pullPushAnimating,
                orientation = Orientation.Vertical,
            ),
    ) {
        Layout(
            modifier = Modifier.border(1.dp, Color.Cyan),
            content = {
                Box(
                    modifier = Modifier
                        .layoutId("top")
                        .background(Color.Blue)
                        .fillMaxWidth()
                        .height(maxHeight.px2dp),
                ) {
                    topContent?.invoke(scrollAll)
                }
                content?.invoke(scrollAll)
                Box(
                    modifier = Modifier
                        .layoutId("bottom")
                        .background(Color.Red)
                        .fillMaxWidth()
                        .height(maxHeight.px2dp),
                ) {
                    bottomContent?.invoke(scrollAll)
                } },
        ) { ms, cs ->
            val sai = scrollAll.toInt()
            layout(cs.maxWidth, cs.maxHeight) {
                var heightSum = 0
                var tp :  Placeable? = null
                var bp :  Placeable? = null
                for (m in ms) {
                    val p = m.measure(cs)
                    val x = 0
                    when (m.layoutId) {
                        "top" -> tp = p
                        "bottom" -> bp = p
                        else -> {
                            val y = heightSum + sai
                            heightSum += p.height
                            p.place(x, y)
                        }
                    }
                }
                contentHeight = heightSum
                contentSurplusHeight = if (cs.maxHeight > contentHeight) 0 else cs.maxHeight - contentHeight
                Log.d("pulldownpushup", "cmh: ${cs.maxHeight} | ch: ${contentHeight} | csh: ${contentSurplusHeight}")
                tp?.run {
                    place(0, sai - maxHeight.toInt())
                }
                bp?.run {
                    place(0, sai + contentHeight)
                }
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