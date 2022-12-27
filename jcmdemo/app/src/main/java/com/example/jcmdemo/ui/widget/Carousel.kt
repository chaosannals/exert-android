package com.example.jcmdemo.ui.widget

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.jcmdemo.ui.DesignPreview
import com.example.jcmdemo.ui.sdp
import com.example.jcmdemo.ui.ssp
import kotlinx.coroutines.delay
import kotlin.math.ceil
import kotlin.math.round

class CarouselItem(
    val color: Color,
)

@Composable
fun Carousel(
    modifier: Modifier =Modifier,
    items: List<CarouselItem>,
) {
    val context = LocalContext.current
    var offset by remember {
        mutableStateOf(0f)
    }
    var index by remember {
        mutableStateOf(0)
    }
    var step by remember {
        mutableStateOf(0f)
    }
    val scrollState = rememberScrollableState() {
        offset += it
        it
    }
    var s by remember {
        mutableStateOf(0)
    }

    suspend fun turnTo(targetOffset: Float) {
        val animateCount = 44
        val animateDelta = ((targetOffset - offset) / animateCount).toInt()
        for (i in 0 until animateCount) {
            offset += animateDelta
            delay(1)
        }
        offset = targetOffset
    }

    LaunchedEffect(scrollState.isScrollInProgress) {
        if (!scrollState.isScrollInProgress) {
            Toast.makeText(context, "${++s}", Toast.LENGTH_SHORT).show()
            turnTo(round(offset  / step) * step)
            Toast.makeText(context, "${s} final", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        while(true) {
            delay(4000)
            if (!scrollState.isScrollInProgress) {
                turnTo(round((offset - step)  / step) * step)
            }
        }
    }

    Box (
        contentAlignment = Alignment.Center,
        modifier = modifier,
    ) {
        Box (
            contentAlignment = Alignment.Center,
            modifier= Modifier
                .align(Alignment.BottomEnd)
                .padding(15.sdp, 10.sdp)
                .height(24.sdp)
                .background(Color(0x22444444), RoundedCornerShape(20.5.sdp))
                .padding(7.5.sdp, 0.dp)
                .zIndex(10f)
        ) {
            Text(
                text = "${index + 1}/${items.size}",
                color=Color(0xFF999999),
                fontSize = 12.ssp,
            )
        }
        Layout(
            modifier = Modifier
                .scrollable(scrollState, Orientation.Horizontal),
            content = @Composable {
                items.forEach {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(it.color),
                    )
                }
            }
        ) { ms, cs ->
            val cellWidth = cs.maxWidth
            val allWidth = cellWidth * items.size

            val ofs = offset
            val offsetX = ofs % allWidth
            val offsetIndex = ceil(offsetX / cellWidth).toInt()

            val offsetMiddleX = (ofs - cellWidth * 0.5) % allWidth
            val offsetMiddleIndex = ceil(offsetMiddleX / cellWidth).toInt()

            index = (items.size - offsetMiddleIndex) % items.size
            step = cellWidth.toFloat()

            layout(cs.maxWidth, cs.maxHeight) {
                if (ms.size == 1) {
                    ms[0].measure(cs).place(0, 0)
                } else {
                    ms.forEachIndexed { i, m ->
                        val p = m.measure(cs)
                        val pIndex = (i + offsetIndex + items.size) % items.size
                        val startX = offsetX - offsetIndex * cellWidth
                        val x = pIndex * cellWidth + startX
                        p.place(x.toInt(), 0)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun CarouselPreview() {
    DesignPreview {
        Carousel(
            modifier=Modifier
                .size(375.sdp, 281.sdp),
            items= listOf(
                CarouselItem(Color.Red),
                CarouselItem(Color.Gray),
                CarouselItem(Color.Green),
                CarouselItem(Color.Blue),
            )
        )
    }
}