package com.example.jcmdemo.ui.page

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
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
fun SinSpray() {
    var tstate by remember { mutableStateOf(false) }
    val transition = updateTransition(targetState = tstate, label = "aa")
    val color by transition.animateColor(label = "aa") {
        when (it) {
            false -> Color(0xFF4499FF)
            true -> Color.Blue
        }
    }
    val startx by transition.animateFloat(label = "aa") {
        when (it) {
            false -> -200f
            true -> 0f
        }
    }

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
//            style = Stroke(
//                width = 4f,
//                cap = StrokeCap.Round,
//                join = StrokeJoin.Round,
//            )
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
fun SinSprayPreview() {
    SinSpray()
}