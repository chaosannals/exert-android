package com.example.app24.ui.page.demo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import com.example.app24.ui.DesignPreview
import com.example.app24.ui.sdp
import com.example.app24.ui.sf

// 这个类实现不够完美，只有指定了宽高时才会正常。
// 按布局设配，大小有问题。
class TicketShape(private val cornerRadius: Float) : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Generic(
            // Draw your custom path here
            path = drawTicketPath(size = size, cornerRadius = cornerRadius)
        )
    }
}

fun drawTicketPath(size: Size, cornerRadius: Float): Path {
    return Path().apply {
        reset()
        // Top left arc
        arcTo(
            rect = Rect(
                left = -cornerRadius,
                top = -cornerRadius,
                right = cornerRadius,
                bottom = cornerRadius
            ),
            startAngleDegrees = 90.0f,
            sweepAngleDegrees = -90.0f,
            forceMoveTo = false
        )
        lineTo(x = size.width - cornerRadius, y = 0f)
        // Top right arc
        arcTo(
            rect = Rect(
                left = size.width - cornerRadius,
                top = -cornerRadius,
                right = size.width + cornerRadius,
                bottom = cornerRadius
            ),
            startAngleDegrees = 180.0f,
            sweepAngleDegrees = -90.0f,
            forceMoveTo = false
        )
        lineTo(x = size.width, y = size.height - cornerRadius)
        // Bottom right arc
        arcTo(
            rect = Rect(
                left = size.width - cornerRadius,
                top = size.height - cornerRadius,
                right = size.width + cornerRadius,
                bottom = size.height + cornerRadius
            ),
            startAngleDegrees = 270.0f,
            sweepAngleDegrees = -90.0f,
            forceMoveTo = false
        )
        lineTo(x = cornerRadius, y = size.height)
        // Bottom left arc
        arcTo(
            rect = Rect(
                left = -cornerRadius,
                top = size.height - cornerRadius,
                right = cornerRadius,
                bottom = size.height + cornerRadius
            ),
            startAngleDegrees = 0.0f,
            sweepAngleDegrees = -90.0f,
            forceMoveTo = false
        )
        lineTo(x = 0f, y = cornerRadius)
        close()
    }
}


// 这个类比较简单，不设置宽高也是正常的。
class SubscriptShape : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path()
        path.moveTo(0f, 0f)
        path.lineTo(size.width, 0f)
        path.lineTo(size.width, size.height)
        path.lineTo(size.height, size.height)
        path.cubicTo(0f,size.height,size.height,0f,0f,0f)
        return Outline.Generic(path)
    }
}

@Composable
fun CustomShapePage() {
    Column (
        modifier = Modifier
            .fillMaxSize(),
    ) {
        Box(
            modifier = Modifier
//                .fillMaxSize()
                .size(300.sdp)
                .graphicsLayer {
                    shadowElevation = 8.sdp.toPx()
                    shape = TicketShape(40.sdp.toPx())
                    clip = true
                }
                .background(Color.Black)
//                .background(Color.Black, shape = TicketShape(40f.sf))
                .drawBehind {
                    scale(scale = 0.9f) {
                        drawPath(
                            path = drawTicketPath(size = size, cornerRadius = 40.sdp.toPx()),
                            color = Color.Red,
                            style = Stroke(
                                width = 2.sdp.toPx(),
                                pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f))
                            )
                        )
                    }
                }
        )

        // 角标
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
//                .height(23.sdp)
                .background(Color.Black, SubscriptShape())
                .padding(start=22.5.sdp, end=11.sdp),
        ) {
            Text(
                text = "文本长长长",
                color = Color.White,
            )
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .height(23.sdp)
                .background(Color.Black, SubscriptShape())
                .padding(start=22.5.sdp, end=11.sdp),
        ) {
            Text(
                text = "文本长",
                color = Color.White,
            )
        }
    }
}

@Preview
@Composable
fun CustomShapePagePreview() {
    DesignPreview {
        CustomShapePage()
    }
}