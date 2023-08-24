package com.example.app24.ui.page.demo


import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.zIndex
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.example.app24.ui.DesignPreview
import com.example.app24.ui.sdp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.ceil
import kotlin.math.floor

@Composable
fun Carousel(
    items: List<String>,
    modifier: Modifier = Modifier,
    autoTurnMs: Long = 6000,
    size: DpSize = DpSize(345.sdp, 86.sdp),
    onClicked: ((Int, String) -> Unit)?=null,
) {
    val coroutineScope = rememberCoroutineScope()
    val offset = remember {
        Animatable(
            initialValue = 0f,
            visibilityThreshold=1f
        )
    }
    var index by remember {
        mutableStateOf(0)
    }
    var step by remember {
        mutableStateOf(0f)
    }

    val isNotEmpty by remember(items) {
        derivedStateOf {
            items.isNotEmpty()
        }
    }

    var turnLast by remember {
        mutableStateOf(0f)
    }
    var isDragging by remember {
        mutableStateOf(false)
    }
    var dragStart by remember {
        mutableStateOf(0f)
    }

    if (isNotEmpty) {
        LaunchedEffect(isDragging) {
            if (!isDragging) {
                val i = floor(offset.value / step)
                val s = offset.value - turnLast
                val t = if (s > 0) i + 1 else i
                turnLast = t * step
                offset.animateTo(turnLast)
            }
        }

        LaunchedEffect(isDragging) {
            while (true) {
                delay(autoTurnMs)
                if (!isDragging) {
                    turnLast = floor((offset.value - step) / step) * step
                    offset.animateTo(turnLast)
                }
            }
        }
    }

    Box (
        contentAlignment = Alignment.Center,
        modifier = modifier
            .padding(top = 10.sdp, bottom = 2.5.sdp)
            .size(size)
            .clip(RoundedCornerShape(8.sdp))
            .background(Color.Gray),
    ) {
        if (isNotEmpty) {
            // 点
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 7.sdp)
                    .zIndex(10f),
            ) {
                for (i in 0 until items.size) {
                    Box(
                        modifier = Modifier
                            .padding(2.sdp),
                    ) {
                        if (index == i) {
                            Box(
                                modifier = Modifier
                                    .size(15.sdp, 6.sdp)
                                    .background(
                                        Color.White,
                                        RoundedCornerShape(3.sdp),
                                    )
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .size(6.sdp)
                                    .background(
                                        Color(0x8AFFFFFF),
                                        CircleShape,
                                    )
                            )
                        }
                    }
                }
            }

            // 图
            Layout(
                modifier = Modifier
                    .pointerInput(Unit) {
                        detectHorizontalDragGestures(
                            onDragStart = {
                                dragStart = it.x
                                isDragging = true
                            },
                            onDragEnd = {
                                isDragging = false
                            }
                        ) { _, dragAmount ->
                            coroutineScope.launch {
                                offset.snapTo(offset.value + dragAmount)
                            }
                        }
                    },
                content = @Composable @Suppress("NON_SOURCE_ANNOTATION_ON_INLINED_LAMBDA_EXPRESSION") {
                    items.forEachIndexed { i, url ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            val p = rememberAsyncImagePainter(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(url)
                                    .size(Size.ORIGINAL)
                                    .build()
                            )
                            Image(
                                painter = p,
                                contentDescription = "图片",
                                contentScale = ContentScale.FillWidth,
                                modifier = Modifier
                                    .clickable {
                                        onClicked?.invoke(i, url)
                                    }
                                    .fillMaxSize()
                                    .background(Color.Gray),
                            )
                        }
                    }
                }
            ) { ms, cs ->
                val cellWidth = cs.maxWidth
                val allWidth = cellWidth * items.size

                val ofs = offset.value
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
}


@Composable
fun CarouselPage() {
    Column(
        verticalArrangement=Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxSize()
    ) {
        Carousel(
            items = listOf(
                "https://picsum.photos/id/4/345/86",
                "https://picsum.photos/id/5/345/86",
                "https://picsum.photos/id/6/345/86",
            ),
        )
    }
}

@Preview
@Composable
fun CarouselPagePreview() {
    DesignPreview {
        CarouselPage()
    }
}