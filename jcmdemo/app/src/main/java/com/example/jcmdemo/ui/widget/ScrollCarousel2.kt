package com.example.jcmdemo.ui.widget

import android.widget.Toast
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.zIndex
import com.example.jcmdemo.ui.sdp
import kotlin.math.ceil
import kotlin.math.round

@Composable
fun ScrollCarousel2(
    width: Dp? = null,
    height: Dp? = null,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    var offset by remember {
        mutableStateOf(0f)
    }
    var count by remember {
        mutableStateOf(0)
    }
    var index by remember {
        mutableStateOf(0)
    }
    var step by remember {
        mutableStateOf(0f)
    }

    var s by remember {
        mutableStateOf(0)
    }

    val state = rememberScrollableState { delta ->
        offset += delta
        delta
    }
    val transition = updateTransition(
        targetState = state.isScrollInProgress,
        label = "offset-t",
    )
    val aniOffset by transition.animateFloat(
        transitionSpec = {
            tween(
                durationMillis = 444,
                easing = FastOutLinearInEasing,
            )
        },
        label = "offset-t",
    ) {
        if (it) {
            offset
        } else {
//            Toast.makeText(context, "${++s}", Toast.LENGTH_SHORT).show()
            round(offset  / step) * step
        }
    }

    val modifier by remember {
        var m = Modifier
            .scrollable(
                orientation = Orientation.Horizontal,
                state = state,
            )
        if (width != null) {
            m = m.width(width)
        } else {
            m = m.fillMaxWidth()
        }
        if (height != null) {
            m = m.height(height)
        } else {
            m = m.fillMaxHeight()
        }
        mutableStateOf(m)
    }

    Box(modifier = modifier) {
        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier
                .zIndex(2.0f)
                .fillMaxSize()
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(14.sdp)
                    .fillMaxWidth(),
            ) {
                for (i in 0 until count) {
                    val isMiddle = index == i
                    val w = if (isMiddle) 24.sdp else 14.sdp
                    val color = if (isMiddle) Color.Cyan else Color.White
                    Spacer(
                        modifier = Modifier
                            .padding(4.sdp)
                            .size(w, 14.sdp)
                            .background(
                                color = color,
                                shape = CircleShape,
                            )
                            .clickable {
                                offset = -i * step
                            },
                    )
                }
            }
        }
        Layout(
            content = content,
            modifier = Modifier
                .zIndex(1.0f)
        ) { measurables, constraints ->
            val placeables = measurables.map { measurable ->
                measurable.measure(constraints)
            }

            val ofs = if (state.isScrollInProgress) offset else aniOffset
            val w = constraints.maxWidth
            val c = placeables.size
            val aw = w * c
//            val ox = offset.toInt() % aw
//            val ox = aniOffset.toInt() % aw
            val ox = ofs.toInt() % aw
            val oi = ox.toFloat() / w
            val ci = ceil(oi).toInt()

//            val iox = (offset - w * 0.5f) % aw
//            val iox = (aniOffset - w * 0.5f) % aw
            val iox = (ofs - w * 0.5f) % aw
            val ici = ceil(iox / w).toInt()

            count = c
            index = (c - ici) % c
            step = w.toFloat()

            layout(constraints.maxWidth, constraints.maxHeight) {
                if (c == 1) {
                    placeables[0].placeRelative(0, 0)
                } else {
                    placeables.forEachIndexed { i, placeable ->
                        val pi = (i + ci + c) % c
                        val sx = ox - ci * w
                        val rx = (w * pi + sx)
                        placeable.placeRelative(x = rx, y = 0)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun ScrollCarousel2Preview() {
    ScrollCarousel2(
        height = 400.sdp,
    ) {
        ScrollDragItem(Color.Red)
        ScrollDragItem(Color.Green)
        ScrollDragItem(Color.Blue)
        ScrollDragItem(Color.Cyan)
        ScrollDragItem(Color.Yellow)
    }
}