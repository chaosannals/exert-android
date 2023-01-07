package com.example.jcmdemo.ui.page.graphic2d

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.tooling.preview.Preview
import com.example.jcmdemo.ui.DesignPreview
import com.example.jcmdemo.ui.sdp
import com.example.jcmdemo.ui.sf

@Composable
fun ShadowPage() {
    Column (
        verticalArrangement=Arrangement.Center,
        horizontalAlignment=Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .padding(40.sdp)
                .size(140.sdp)
                .background(Color.Red)
                .drawBehind {
                    val paint = Paint()
                    val frameworkPaint = paint.asFrameworkPaint()
                    drawIntoCanvas {
                        frameworkPaint.setShadowLayer(
                            4f.sf,
                            0f,
                            4f.sf,
                            Color.Black.toArgb()
                        )
                        it.drawOutline(
                            RectangleShape.createOutline(size, layoutDirection, this), paint
                        )
                    }
                    drawLine(
                        Color.Black,
                        Offset(0f, 0f),
                        Offset(size.width, size.height),
                        4f,
                    )
                },
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            )
        }

        Box(
            modifier = Modifier
                .padding(40.sdp)
                .size(140.sdp)
                .background(Color.Red)
                .drawBehind {
                    val paint = Paint()
                    val frameworkPaint = paint.asFrameworkPaint()
                    drawIntoCanvas {
                        frameworkPaint.setShadowLayer(
                            4f.sf,
                            0f,
                            4f.sf,
                            Color.Black.toArgb()
                        )
                        it.drawOutline(
                            CircleShape.createOutline(size, layoutDirection, this), paint
                        )
                    }
                    drawLine(
                        Color.Black,
                        Offset(0f, 0f),
                        Offset(size.width, size.height),
                        4f,
                    )
                },
        )
    }
}

@Preview
@Composable
fun ShadowPagePreview() {
    DesignPreview() {
        ShadowPage()
    }
}