package com.example.jcmdemo.ui.widget

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun SinSpray2() {
    var tstate by remember { mutableStateOf(false) }
    val color by animateColorAsState(
        targetValue = if (tstate) Color.Blue else Color(0xFF4499FF),
//        animationSpec = tween(
//            durationMillis = 300,
//            easing = FastOutLinearInEasing,
//        ),
        animationSpec = repeatable(
            iterations = 4,
//            animation = tween(
//                durationMillis = 300,
//                easing = FastOutLinearInEasing,
//            ),
            animation = keyframes {
                durationMillis = 4000
                Color.Blue at 0 with LinearEasing
                Color.Red at 1000 with FastOutLinearInEasing
                Color.Yellow at 2000 with FastOutSlowInEasing
                Color.Cyan at 3000 with FastOutSlowInEasing
            },
            repeatMode = RepeatMode.Reverse,
        ),
    )
    val startx by animateFloatAsState(
        targetValue = if (tstate) 0f else -200f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 400,
                easing = FastOutLinearInEasing,
            ),
        ),
    )

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
            .background(
                color = Color.Cyan,
            )
            .clickable {
                tstate = !tstate
            },
    ) {
        val p = Path()
        p.moveTo(startx, 600f)
        p.lineTo(startx, 400f)
        for (i in 1..16) {
            val x = startx + 100f * i
            val mx = x - 50f
            val y = if (i % 2 != 0) { 440f } else { 360f }
            p.quadraticBezierTo(mx, y, x, 400f)
        }
        p.lineTo(startx + 1600f, 600f)
        p.close()

        drawPath(
            path = p,
            color = color,
            style = Fill,
        )

        drawRect(
            color = Color.Red,
            topLeft = Offset.Zero,
            size = Size(100f, 100f)
        )
    }
}

@Preview
@Composable
fun SinSpray2Preview() {
    SinSpray2()
}