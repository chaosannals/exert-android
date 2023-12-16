package com.example.jcm3ui.ui.page.layout

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import com.example.jcm3ui.ui.displayWidth
import com.example.jcm3ui.ui.ssp
import kotlin.math.ceil
import kotlin.math.floor

@Preview
@Composable
fun CustomDrawLazyVerticalGridPage(
    columnCount: Int = 4,
) {
    val colors = remember {
        mutableStateListOf<Color>().apply {
            for (i in 0.. 1000) {
                val c = 0xFF000000 + (0x00FFFFFF and (i * colorStep))
                add(Color(c))
            }
        }
    }

    var offset by remember {
        mutableFloatStateOf(0f)
    }

    val textMeasurer = rememberTextMeasurer()
    Canvas(
        modifier = Modifier
            .navigationBarsPadding()
            .fillMaxSize()
            .pointerInput(Unit) {
                detectVerticalDragGestures { change, dragAmount ->
                    change.consume()
                    offset += dragAmount
                }
            }
    ) {
        val cw = size.width / columnCount
        val rowCount = ceil(size.height / cw).toInt() + 1
        val cellCount = rowCount * columnCount
        val cellSize = Size(cw, cw)
        val startRow = floor(-offset / cw).toInt()
        val startIndex = startRow * 4

        for (i in 0 until  cellCount) {
            val index = i + startIndex
            if (!(index in 0 until  colors.size)) continue
            val top = offset + index.floorDiv(4) * cw
            val left = index.mod(4) * cw
            val cellOffset = Offset(left, top)
            drawRect(
                color = colors[index],
                topLeft = cellOffset,
                size = cellSize,
            )

            val titleLayoutResult = textMeasurer.measure(
                text = "$index",
                style = TextStyle(
                    color = Color.White,
                    fontSize = 24.ssp,
                )
            )
            drawText(
                textLayoutResult = titleLayoutResult,
                topLeft = cellOffset,
            )
        }
    }
}