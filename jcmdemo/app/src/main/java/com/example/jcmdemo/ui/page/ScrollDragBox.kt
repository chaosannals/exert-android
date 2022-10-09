package com.example.jcmdemo.ui.page

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.ceil
import kotlin.math.floor

@Composable
fun ScrollDragBoxItem(
    color: Color,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color),
    ) {

    }
}

@Composable
fun ScrollDragBox() {
    var offset by remember {
        mutableStateOf(0f)
    }
    val sstate = rememberScrollableState { delta ->
        offset += delta
        delta
    }
    Column() {
        Text(offset.toString())
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .scrollable(
                    orientation = Orientation.Horizontal,
                    state = sstate,
                ),
        ) {
            Layout(
                content = {
                    ScrollDragBoxItem(Color.Red)
                    ScrollDragBoxItem(Color.Green)
                    ScrollDragBoxItem(Color.Blue)
                    ScrollDragBoxItem(Color.Cyan)
                    ScrollDragBoxItem(Color.Yellow)
                }
            ) { measurables, constraints ->
                val placeables = measurables.map { measurable ->
                    measurable.measure(constraints)
                }

                val allw = constraints.maxWidth * (placeables.size - 1)

                layout(constraints.maxWidth, constraints.maxHeight) {
                    placeables.forEachIndexed { index, placeable ->
                        val ox = offset.toInt()
                        val oox = ox % allw
                        val sx = floor(oox.toFloat() / constraints.maxWidth).toInt()

                        val i = (index + sx) % placeables.size
                        val rx = (constraints.maxWidth * i + oox) % allw

                        placeable.placeRelative(x = rx , y = 0)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun ScrollDragBoxPreview() {
    ScrollDragBox()
}