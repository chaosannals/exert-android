package com.example.layoutdemo.ui.widget

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.abs

///
/// 此种方式的实现（使用在滚动列内）和使用横向滚动条的实现方式相同，都会有误触滑动的问题。
/// 而且还比 横向滚动多了一个 key 的参数。需要确保 key 的唯一。更麻烦了。
@Composable
fun DrawerButton(
    key: Any?,
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.Center,
    sideContentAlignment: Alignment = Alignment.Center,
    sideContent: @Composable BoxScope.() -> Unit = {},
    content: @Composable BoxScope.() -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val xOffset = remember {
        Animatable(0f)
    }
    var mainWidth by remember {
        mutableIntStateOf(0)
    }
    var sideWidth by remember {
        mutableIntStateOf(0)
    }
    val right by remember(mainWidth, sideWidth) {
        derivedStateOf {
            mainWidth + sideWidth
        }
    }
    var startOffset by remember {
        mutableFloatStateOf(0f)
    }

    Layout(
        modifier = modifier
            .pointerInput(key) {
                detectHorizontalDragGestures(
                    onDragStart = {
                        startOffset = xOffset.value
                    },
                    onDragEnd = {
                        val diff = startOffset - xOffset.value
                        coroutineScope.launch {
                            val target = if (diff < 0) {
                                0f
                            } else {
                                -mainWidth.toFloat()
                            }
                            xOffset.animateTo(target)
                        }
                    }
                ) { change, xAmount ->
                    change.consume()
                    coroutineScope.launch {
                        val x = xOffset.value + xAmount
                        if (x < 0 && abs(x) < right) {
                            xOffset.snapTo(x)
                        }
                    }
                }
            },
        content = {
            Box(
                contentAlignment = contentAlignment,
                modifier = Modifier
                    .layoutId("main")
                    .fillMaxSize()
            ) {
                content()
            }
            Box(
                contentAlignment = sideContentAlignment,
                modifier = Modifier
                    .layoutId("side")
                    .fillMaxHeight()
                    .wrapContentWidth()
            ) {
                sideContent()
            }
        }
    ) { ms, cs ->
        val main = ms.first { it.layoutId == "main" }.measure(cs)
        val side = ms.first { it.layoutId == "side" }.measure(cs)
        mainWidth = main.width
        sideWidth = side.width

        layout(cs.maxWidth, cs.maxHeight) {
            val x = xOffset.value.toInt()
            main.place(x, 0, 0f)
            side.place(x + cs.maxWidth, 0, 0f)
        }
    }
}

@Preview
@Composable
fun DrawerButtonPreview() {
    val m = remember {
        Modifier
            .fillMaxWidth()
            .height(140.dp)
    }
    val numbers = remember {
        (0..100).toMutableList()
    }

    LazyColumn {
        item {
            DrawerButton(
                key = "a",
                modifier = m,
            ) {
                Text("2")
            }
        }
        itemsIndexed(numbers) { i, _ ->
            DrawerButton(
                "$i",
                modifier = m,
                sideContent = {
                    Box(
                        modifier = Modifier
                            .width(100.dp)
                    ) {
                        Text("[$i]")
                    }
                }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .border(1.dp, Color.Black)
                ) {
                    Text("$i")
                }
            }
        }
    }
}