package com.example.jcmdemo.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.tooling.preview.Preview
import com.example.jcmdemo.ui.DesignPreview
import com.example.jcmdemo.ui.sdp

@Composable
fun Carousel2(
    modifier: Modifier = Modifier,
    content: @Composable ()-> Unit,
) {
    // rememberScrollState 没有 rememberScrollableState 适合控制布局
    // rememberScrollState 自身的实现上会影响到布局。
    val scrollState = rememberScrollState()

    Box(modifier = modifier) {
        Layout(
            content = content,
            modifier = Modifier
                .horizontalScroll(
                    scrollState,
//                flingBehavior={},
                ),
        ) { ms, constraints ->
            layout(constraints.maxWidth, constraints.maxHeight) {
                ms.forEachIndexed { i, m ->
                    val p = m.measure(constraints)
                    val x = i * constraints.maxWidth
                    p.place(x, 0, i.toFloat())
                }
            }
        }
    }
}

@Preview
@Composable
fun Carousel2Preview() {
    DesignPreview() {
        Carousel2(
            modifier = Modifier
                .size(234.sdp, 124.sdp),
        ) {
            Box(
                modifier= Modifier
                    .fillMaxSize()
                    .background(Color.Red)
            )
            Box(
                modifier= Modifier
                    .fillMaxSize()
                    .background(Color.Green)
            )
            Box(
                modifier= Modifier
                    .fillMaxSize()
                    .background(Color.Blue)
            )
            Box(
                modifier= Modifier
//                    .fillMaxSize()
                    .size(234.sdp, 124.sdp)
                    .background(Color.Yellow)
            )
        }
    }
}