package com.example.app24.ui.page.demo

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import com.example.app24.ui.DesignPreview
import kotlin.math.ceil
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.random.Random

private data class Rg2Point(
    val offset: Offset,
    val color: Color,
    val radius: Float,
)

private data class Rg2Line(
    val start: Rg2Point,
    val end: Rg2Point,
    val kind: Int = 0,
    val m1: Offset? = null,
    val m2: Offset? = null,
)

private inline fun IntOffset.isIn(maxX: Int, maxY: Int) : Boolean {
    return x in 0 until maxX && y in 0 until maxY
}

// Array<Array<Rg2Point>> 二级数组长度 必须等长
private fun Array<Array<Rg2Point>>.getOrNull(x: Int, y: Int): Rg2Point? {
    if (y in 0 until size && x in 0 until  this[0].size) {
        return this[y][x]
    }
    return null
}

private fun Offset.ratote(a: Float): Offset {
    val r = sqrt(x * x + y * y)
    return Offset(r * cos(a), r * sin(a))
}

@OptIn(ExperimentalTextApi::class, ExperimentalMaterial3Api::class)
@Composable
fun RandomGraph2Page() {
    val rand = remember {
        Random(444)
    }
    var pointCount by remember {
        mutableStateOf(44)
    }
    var canvasSize by remember {
        mutableStateOf(Size(1f, 1f))
    }
    var canvasOffset by remember {
        mutableStateOf(Offset.Zero)
    }
    var pointRadius by remember(canvasSize) {
        mutableStateOf(canvasSize.width * 0.04f)
    }
    var areaSize by remember(canvasSize) {
        val a = canvasSize.width * 0.4f
        mutableStateOf(Size(a, a))
    }

    val columnCount by remember(pointCount, canvasSize) {
        derivedStateOf { floor(sqrt(pointCount.toFloat())).toInt() }
    }
    val rowCount by remember(columnCount) {
        derivedStateOf { ceil(pointCount.toFloat() / columnCount).toInt() }
    }

    val textMeasurer = rememberTextMeasurer()
    val points by remember(areaSize, rowCount, columnCount) {
        val r = Array(rowCount) { j ->
            // TODO
            Array(columnCount) { i ->
                Log.d("RandomGraph2", "($i, $j)")
                val x = i * areaSize.width + rand.nextFloat() * areaSize.width
                val y = j * areaSize.height + rand.nextFloat() * areaSize.height
                val c = Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat())
                val r = pointRadius * 0.4f + rand.nextFloat() * pointRadius * 0.6f
                Rg2Point(Offset(x, y), c, r)
            }
        }
        mutableStateOf(r)
    }

    val lines by remember(points) {
        val r = mutableListOf<Rg2Line>()
        for (j in 0 until points.size){
            val row = points[j]
            for (i in 0 until row.size) {
                val cell = row[i]
                // 东南方向扫，
                points.getOrNull(i + 1, j)?.let {
                    //
                    val k = rand.nextInt(0, 2)
                    val line = when (k) {
                        0 -> Rg2Line(cell, it)
                        else -> {
                            val d = (it.offset - cell.offset) / 4f
                            val m1 = cell.offset + d.ratote((rand.nextFloat() - 0.5f) * 0.4f)
                            val m2 = it.offset - d.ratote((rand.nextFloat() - 0.5f) * 0.4f)
                            Rg2Line(cell, it, k, m1, m2)
                        }
                    }
                    r.add(line)
                }
                points.getOrNull(i, j + 1)?.let {
                    r.add(Rg2Line(cell, it))
                }
                // 西北方向扫
//                points.getOrNull(i, j - 1)?.let {
//                    r.add(Rg2Line(cell, it))
//                }
//                points.getOrNull(i - 1, j)?.let {
//                    r.add(Rg2Line(cell, it))
//                }
            }
        }
        mutableStateOf(r.toList())
    }

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxSize()
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    canvasOffset += dragAmount
                }
            },
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RectangleShape),
        ) {
            if (canvasSize != size) {
                canvasSize = size
            }

            val p = Path()
            for (line in lines) {
                val start = canvasOffset + line.start.offset
                val end = canvasOffset + line.end.offset
                val k = rand.nextInt(0, 2)

                p.moveTo(start.x, start.y)
                when (line.kind) {
                    0 -> {
                        p.lineTo(end.x, end.y)
                    }
                    1 -> {
                        val m1 = canvasOffset + line.m1!!
                        val m2 = canvasOffset + line.m2!!
                        p.cubicTo(m1.x, m1.y, m2.x, m2.y, end.x, end.y)
                    }
                }
            }
            drawPath(
                path = p,
                color=Color.Black,
                style = Stroke(
                    width = 4f,
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round,
                )
            )

            for (j in 0 until rowCount) {
                for (i in 0 until columnCount) {
                    val p = points[j][i]
                    val index = j * rowCount + i
                    val offset = canvasOffset + p.offset
                    val textOffset = offset - Offset(p.radius, p.radius) * 0.5f
                    // Log.d("RandomGraph2", "textOffset: $textOffset")

                    if (offset.x in 0f .. size.width && offset.y in 0f .. size.height) {
                        drawCircle(p.color, p.radius, offset)
                    }

                    if (textOffset.x in 0f .. size.width && textOffset.y in 0f .. size.height) {
                        drawText(textMeasurer, "$index", textOffset)
                    }
                }
            }
        }
        TextField(
            value = "$pointCount",
            onValueChange =
            {
                pointCount = it.toIntOrNull() ?: 0
            },
            modifier = Modifier.fillMaxWidth(),
        )
        TextField(
            label = { Text("点半径") },
            value = "$pointRadius",
            onValueChange =
            {
                pointRadius = it.toFloatOrNull() ?: 4f
            },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview
@Composable
fun RandomGraph2PagePreview() {
    DesignPreview {
        RandomGraph2Page()
    }
}