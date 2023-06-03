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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import com.example.app24.ui.DesignPreview
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.sqrt
import kotlin.random.Random

private data class Rg2Point(
    val offset: Offset,
    val color: Color,
)

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
                Rg2Point(Offset(x, y), c)
            }
        }
        mutableStateOf(r)
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

            for (j in 0 until rowCount) {
                for (i in 0 until columnCount) {
                    val p = points[j][i]
                    val index = j * rowCount + i
                    val offset = canvasOffset + p.offset
                    val textOffset = offset - Offset(pointRadius, pointRadius) * 0.5f
                    drawCircle(p.color, pointRadius, offset)
                    Log.d("RandomGraph2", "textOffset: $textOffset")
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