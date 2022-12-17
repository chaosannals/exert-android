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
                    ScrollDragItem(Color.Red)
                    ScrollDragItem(Color.Green)
                    ScrollDragItem(Color.Blue)
                    ScrollDragItem(Color.Cyan)
                    ScrollDragItem(Color.Yellow)
                }
            ) { measurables, constraints ->
                val placeables = measurables.map { measurable ->
                    measurable.measure(constraints)
                }

                val w = constraints.maxWidth
                val c = placeables.size
                val allw = w * c
                val ox = offset.toInt() % allw
                val oi = ox.toFloat() / w
                val ci = ceil(oi).toInt()

                layout(constraints.maxWidth, constraints.maxHeight) {
                    if (c == 1) {
                        placeables[0].placeRelative(0, 0)
                    } else {
                        placeables.forEachIndexed { index, placeable ->
                            val pi = (index + ci + c) % c
                            val sx = ox - ci * w
                            val rx = (w * pi + sx)
                            placeable.placeRelative(x = rx, y = 0)
                        }
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