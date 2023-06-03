package com.example.app24.ui.page.demo

import androidx.compose.ui.geometry.Size
import androidx.compose.foundation.Canvas
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
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import com.example.app24.ui.DesignPreview
import kotlin.random.Random

private data class RgPoint(
    val offset: Offset,
    val color: Color,
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTextApi::class)
@Composable
fun RandomGraphPage() {
    val rand = remember {
        Random(444)
    }

    var pointCount by remember {
        mutableStateOf(4)
    }
    var canvasSize by remember {
        mutableStateOf(Size(1f, 1f))
    }
    var pointRadius by remember(canvasSize) {
        mutableStateOf(canvasSize.width * 0.04f)
    }

    val textMeasurer = rememberTextMeasurer()
    val points = remember(pointCount, canvasSize) {
        val r = mutableListOf<RgPoint>()
        for (i in 0 until pointCount) {
            val x = rand.nextFloat() * canvasSize.width
            val y = rand.nextFloat() * canvasSize.height
            val c = Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat())
            r.add(RgPoint(Offset(x, y), c))
        }
        r.toMutableStateList()
    }



    Column(
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
        ) {
            if (canvasSize != size) {
                canvasSize = size
            }


            points.forEachIndexed { i, p ->
                val textOffset = p.offset - Offset(pointRadius, pointRadius) * 0.5f
                drawCircle(p.color, pointRadius, p.offset)
                drawText(textMeasurer, "$i", textOffset)
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
fun RandomGraphPagePreview() {
    DesignPreview {
        RandomGraphPage()
    }
}