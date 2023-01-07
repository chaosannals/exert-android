package com.example.jcmdemo.ui.widget.graphic2d

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.tooling.preview.Preview
import com.example.jcmdemo.ui.DesignPreview
import com.example.jcmdemo.ui.sf

class ShadowBoxScope(
    val box: BoxScope,
) {

}

@Composable
fun ShadowBox(
    modifier: Modifier = Modifier,
    shape: Shape = RectangleShape,
    radius: Float = 4f,
    offsetX: Float = 0f,
    offsetY: Float = 4f,
    color: Color = Color.Black,
    content: @Composable (ShadowBoxScope.() -> Unit)?=null,
) {
    Box(
        modifier = Modifier
            .drawBehind {
                val paint = Paint()
                val frameworkPaint = paint.asFrameworkPaint()
                drawIntoCanvas {
                    frameworkPaint.setShadowLayer(
                        radius.sf,
                        offsetX.sf,
                        offsetY.sf,
                        color.toArgb()
                    )
                    it.drawOutline(
                        shape.createOutline(size, layoutDirection,this), paint)
                }
            },
    ) {
        Box(
            modifier = modifier
                .clip(shape)
        ) {
            content?.let {
                val scope = ShadowBoxScope(this)
                scope.it()
            }
        }
    }
}

@Preview
@Composable
fun ShadowBoxPreview() {
    DesignPreview() {
        Column (
            verticalArrangement=Arrangement.Center,
            horizontalAlignment=Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
        ) {

        }
    }
}