package com.example.app24.ui.widget

import android.graphics.Paint
import android.text.TextPaint
import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.AnimationConstants
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.app24.d2md
import com.example.app24.dt
import com.example.app24.ui.drawShadow
import com.example.app24.ui.sdp
import com.example.app24.ui.sf
import com.example.app24.ui.ssp
import java.util.Calendar
import java.util.Date
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow


class LineGraphPoint(
    val yValue: Float,
    val xDate: Date,
)

class LineGraph(
    val color: Color,
    val points: List<LineGraphPoint>,
) {
    fun calculateMaxY() : Float {
        return points.map { it.yValue }
            .reduce { a, b -> max(a, b) }
    }
    fun calculateMinX() : Date {
        return points.map { it.xDate }
            .reduce { a, b -> if (a.time < b.time) { a } else { b } }
    }
    fun calculateMaxX() : Date {
        return points.map { it.xDate }
            .reduce { a, b -> if (a.time >= b.time) { a } else { b } }
    }

    fun at(i: Int) : LineGraphPoint {
        return points[i]
    }
}

private fun calculateMaxDate(
    lines: List<LineGraph>,
) : Date {
    return lines.map { it.calculateMaxX() }
        .reduce { a, b -> if (a.time >= b.time) { a } else { b } }
}

private fun calculateMinDate(
    lines: List<LineGraph>,
) : Date {
    return lines.map { it.calculateMinX() }
        .reduce { a, b -> if (a.time < b.time) { a } else { b } }
}

private fun calculateMaxY(
    lines: List<LineGraph>,
) : Float {
    return lines.map { it.calculateMaxY() }
        .reduce { a, b -> max (a, b) }
}

fun stepY(d: Float) : Float {
    val f = 10f.pow(floor(log10(d)))
    return if (f > 1) {
        ceil(d  / f) * f
    } else {
        10f
    }
}

private fun Date.subd(other: Date) : Long {
    return (time - other.time) / 86400000
}

data class DotLineData(
    val offset: Offset,
    val yValue: Float,
)

@OptIn(ExperimentalTextApi::class)
@Composable
fun LineGraphView(
    lines: List<LineGraph>,
    rowCount: Int = 5,
    columnCount: Int = 7,
    minY: Float = 0f,
) {
    val minDate = calculateMinDate(lines)
    val maxDate = calculateMaxDate(lines)
    val dateCount = maxDate.subd(minDate)
    val maxY = calculateMaxY(lines)
    val dX = ceil(dateCount.toFloat() / columnCount).toInt()
    // dateCount = (dX * columnCount).toLong()
    val dY = stepY((maxY - minY) / rowCount)

    val context = LocalContext.current
    val gray =  Color.Gray

    // 左文字
    val ltPaint = TextPaint()
    ltPaint.color = Color.Gray.toArgb()
    ltPaint.strokeWidth = 0.5f.sf
    ltPaint.style = Paint.Style.FILL
    ltPaint.textSize = 10f.sf
    ltPaint.textAlign = Paint.Align.RIGHT

    // 下文字
    val btPaint = TextPaint()
    btPaint.color = Color.Gray.toArgb()
    btPaint.strokeWidth = 0.5f.sf
    btPaint.style = Paint.Style.FILL
    btPaint.textSize = 10f.sf
    btPaint.textAlign = Paint.Align.CENTER

    var tapPoint: Offset? by remember {
//        mutableStateOf(null)
        mutableStateOf(Offset.Zero)
    }

    val textMeasurer = rememberTextMeasurer()

    var isShowed by remember { mutableStateOf(false) }
//    var isShowed by remember { mutableStateOf(true) }
    val yPercentage by animateFloatAsState(
        targetValue = if (isShowed) 1f else 0f,
        animationSpec = tween(1000),
    )
    LaunchedEffect(isShowed) {
        if (!isShowed) {
            isShowed = true
        }
    }

    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(tapPoint) {
        if (tapPoint != null) {
            focusRequester.requestFocus()
        }
    }

    Canvas(
        modifier = Modifier
            .padding(15.sdp, 0.dp)
            .size(345.sdp, 180.sdp)
            .focusRequester(focusRequester)
            .onFocusChanged
            {
                Log.d("LineGraphView", "focus: $it")
                if (!it.isFocused) {
                    tapPoint = null
                }
            }
            .focusTarget() // 起效焦点，一般 TextField 等默认都是已经加上了的，无须加。非 TextField 类需要自己加上。
            .pointerInput(Unit) {
                detectTapGestures { tapPoint = it }
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { tapPoint = it },
                    onDrag = { change, dragAmount ->
                        change
                            .copy(
                                scrollDelta = change.scrollDelta.copy(y = 0f),
                            )
                            .consume()
                        tapPoint?.let {
                            tapPoint = it + dragAmount
                        }
                    },
                )
            },
    ) {
        val w = size.width
        val h = size.height

        // 绘图区
        val areaT = 0.0416f * h
        val areaL = 0.1f * w
        val areaR = 0.9652f * w
        val areaB = 0.8861f * h

        // 纵轴标签
        val textL = 0.0623f * w

        val lineW = 0.0014f * w
        val pathW = 0.0043f * w

        val rowDelta = (areaB - areaT) / rowCount

        val columnDelta = (areaR - areaL) / columnCount
        val columnStart = areaL + columnDelta / 2
        val columnEnd = areaR - columnDelta / 2
        val deltaX = (columnEnd - columnStart) / dateCount
        val ratioY = rowDelta / dY

        // 行
        for (i in 0 until rowCount) {
            val dd = areaB - i * rowDelta
            drawIntoCanvas {
                it.nativeCanvas.drawText(
                    (i * dY).toInt().toString(),
                    textL, dd,
                    ltPaint
                )
            }
            drawLine(
                start = Offset(areaL, dd),
                end = Offset(areaR, dd),
                color = gray,
                strokeWidth = lineW,
            )
        }

        // 列
        val cd = Calendar.getInstance()
        cd.time = minDate
        for (i in 0 until columnCount) {
            val dd = dX * i
            val dx = columnStart + deltaX * dd

            drawIntoCanvas {
                it.nativeCanvas.drawText(
                    cd.time.d2md,
                    dx, h,
                    btPaint
                )
            }

            cd.add(Calendar.DATE, dX)
        }

        // 线
        lines.forEachIndexed { j, it ->
            val p = Path()

            var lpx: Float? = null
            var lpy: Float? = null
            val doffset = mutableListOf<DotLineData>()
            it.points.forEachIndexed { i, lp ->
                val dx = columnStart + lp.xDate.subd(minDate) * deltaX
                val dy = (areaB - (lp.yValue * yPercentage) * ratioY + 2f * j)

                doffset.add(
                    DotLineData(
                        offset=Offset(dx, dy),
                        yValue = lp.yValue,
                    )
                )

                if (i == 0) {
                    p.moveTo(dx, dy)
                } else {
                    val mx = (dx + lpx!!) / 2
                    val my = (dy + lpy!!) / 2
                    val ml = abs(dy - lpy!!) / 2
                    if (lpy!! < dy) {
                        p.cubicTo(mx, my - ml, mx, my + ml, dx, dy)
                    } else {
                        p.cubicTo(mx, my + ml, mx, my - ml, dx, dy)
                    }
                }

                lpx = dx
                lpy = dy
            }

            drawPath(
                path = p,
                color = it.color,
                style = Stroke(
                    width = pathW,
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round,
                )
            )

            tapPoint?.let { tp ->
                val needed = doffset.reduce { a, n ->
                    val ad = abs(tp.x - a.offset.x)
                    val nd = abs(tp.x - n.offset.x)
                    if (ad < nd) a else n
                }

                drawCircle(
                    color = Color.White,
                    radius = 0.00725f * w,
                    center = needed.offset,
                )

                drawCircle(
                    color = it.color,
                    radius = 0.00725f * w,
                    center = needed.offset,
                    style = Stroke(
                        width = pathW
                    )
                )

                val textBoxSize = Size(90f.sf, 45.5f.sf)
                val textBoxTopLeft = needed.offset.copy(
                    x = min(needed.offset.x, areaR - textBoxSize.width),
                    y = min(needed.offset.y, areaB - textBoxSize.height),
                )

                drawShadow(
                    topLeft = textBoxTopLeft,
                    size = textBoxSize,
                    offset = Offset(0f, 3f.sf),
                    color = Color(0xBDD8E4F6),
                    shadowBlurRadius=5.sdp,
                )
                drawRoundRect(
                    color = Color.White,
                    topLeft = textBoxTopLeft,
                    size = textBoxSize,
                    cornerRadius = CornerRadius(5f.sf)
                )
                val title = "访问数量访问本厂：${needed.yValue}"
                val titleLayoutResult = textMeasurer.measure(
                    text = AnnotatedString(
                        text=title,
                        spanStyles = listOf(
                            AnnotatedString.Range(
                                SpanStyle(
                                    color = Color(0xFF666666),
                                    fontSize = 10.ssp,
                                ),
                                0, 4
                            ),
                            AnnotatedString.Range(
                                SpanStyle(
                                    color = Color(0xFF666666),
                                    fontSize = 9.ssp,
                                ),
                                4, 9
                            ),
                        ),
                        paragraphStyles = listOf(
                            AnnotatedString.Range(
                                ParagraphStyle(),
                                0, 4
                            ),
                        ),
                    ),
                    style = TextStyle(
                        color = Color(0xFFFF8D1A),
                        fontSize = 9.ssp,
                        fontWeight = FontWeight.Bold
                    ),
                )
                drawText(
                    textLayoutResult = titleLayoutResult,
                    topLeft = textBoxTopLeft.copy(
                        x = textBoxTopLeft.x + 10f.sf,
                        y = textBoxTopLeft.y + (textBoxSize.height - titleLayoutResult.size.height) / 2
                    ),
                )

                drawLine(
                    start = Offset(needed.offset.x, areaT),
                    end = Offset(needed.offset.x, areaB),
                    color = gray,
                    strokeWidth = lineW,
                    pathEffect = PathEffect.dashPathEffect(
                        floatArrayOf(10f, 10f), 0f
                    ),
                )
            }
        }
    }
}

@Preview
@Composable
fun LineGraphViewPreview() {
    LineGraphView(
        lines = listOf(
//            LineGraph(
//                color = Color.Yellow,
//                points = listOf(
//                    LineGraphPoint( 10.1f, "2022-02-01 00:00:00".dt),
//                    LineGraphPoint( 354.5f, "2022-02-02 00:00:00".dt),
//                    LineGraphPoint( 256.6f, "2022-02-03 00:00:00".dt),
//                    LineGraphPoint( 50.5f, "2022-02-04 00:00:00".dt),
//                    LineGraphPoint( 205.4f, "2022-02-05 00:00:00".dt),
//                    LineGraphPoint( 401.3f, "2022-02-06 00:00:00".dt),
//                    LineGraphPoint( 411.3f, "2022-02-07 00:00:00".dt),
//                    LineGraphPoint( 491.3f, "2022-02-08 00:00:00".dt),
//                    LineGraphPoint( 205.4f, "2022-02-09 00:00:00".dt),
//                    LineGraphPoint( 401.3f, "2022-02-10 00:00:00".dt),
//                    LineGraphPoint( 411.3f, "2022-02-11 00:00:00".dt),
//                    LineGraphPoint( 491.3f, "2022-02-12 00:00:00".dt),
//                )
//            ),
            LineGraph(
                color = Color.Blue,
                points = listOf(
                    LineGraphPoint( 20f, "2022-02-01 00:00:00".dt),
                    LineGraphPoint( 353f, "2022-02-02 00:00:00".dt),
                    LineGraphPoint( 20f, "2022-02-03 00:00:00".dt),
                    LineGraphPoint( 100f, "2022-02-04 00:00:00".dt),
                    LineGraphPoint( 10f, "2022-02-05 00:00:00".dt),
                    LineGraphPoint( 666f, "2022-02-06 00:00:00".dt),
                )
            ),
        )
    )
}