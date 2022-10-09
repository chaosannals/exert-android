package com.example.jcmdemo.ui.page

import android.util.Log
import androidx.compose.ui.graphics.Path
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

data class PathDataPoint(
    var x: Float,
    var y: Float,
)

fun parseV(it: String) : Float {
    return it.substring(1).toFloat()
}

fun parseXY(it: String) : PathDataPoint {
    val xy = it.substring(1).split(',')
    val x = xy[0].toFloat()
    val y = xy[1].toFloat()
    Log.d("tttt jcmdemo", "p: ${x} ${y}")
    return PathDataPoint(x, y)
}

fun parsePath(text: String) : Path {
    val p = Path()
    val pd = text.split(' ')
    var last = PathDataPoint(0f, 0f)
    for (it in pd) {
        when (it[0]) {
            'M' -> {
                last = parseXY(it)
                p.moveTo(last.x, last.y)
                Log.d("jcmdemo", "M ${last.x} ${last.y}")
            }
            'l' -> {
                val (x, y) = parseXY(it)
                last.x += x
                last.y += y
                p.relativeLineTo(x, 0f)
                Log.d("jcmdemo", "l ${last.x} ${last.y}")
            }
            'L' -> {
                last = parseXY(it)
                p.lineTo(last.x, last.y)
                Log.d("jcmdemo", "L ${last.x} ${last.y}")
            }
            'h' -> {
                val x = parseV(it)
                last.x += x
                p.relativeLineTo(x, 0f)
                Log.d("jcmdemo", "h ${last.x} ${last.y}")
            }
            'H' -> {
                val x = parseV(it)
                last.x = x
                p.lineTo(last.x, last.y)
                Log.d("jcmdemo", "H ${last.x} ${last.y}")
            }
            'v' -> {
                val y = parseV(it)
                last.y += y
                p.relativeLineTo(0f, y)
                Log.d("jcmdemo", "v ${last.x} ${last.y}")
            }
            'V' -> {
                val y = parseV(it)
                last.y = y
                p.lineTo(last.x, last.y)
                Log.d("jcmdemo", "V ${last.x} ${last.y}")
            }
            'z' -> {
                p.close()
            }
        }
    }
    return p
}

@Composable
fun PathDataParser() {
    val pt = "M0,0 l100,0 L100,100 h100 v-100 h100 v200 H0 z"

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp),
    ){
        val p = parsePath(pt)

        drawPath(
            path = p,
            color = Color.Red,
            style = Fill,
        )
    }
}

@Preview
@Composable
fun PathDataParserPreview() {

    PathDataParser()
}