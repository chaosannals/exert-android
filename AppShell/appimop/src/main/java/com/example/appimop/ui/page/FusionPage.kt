package com.example.appimop.ui.page

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.RenderEffect
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import com.example.appimop.ui.DesignPreview
import com.example.appimop.ui.sdp
import com.example.appimop.ui.sf

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun FusionPage() {
    val color by remember {
        mutableStateOf(Color(0xFF4499FF))
    }

    Box(
        contentAlignment=Alignment.TopCenter,
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxSize(),
    ) {
        Box(
            modifier = Modifier
                .offset(100.sdp, 140.sdp)
                .size(140.sdp)
                .background(color, CircleShape),
        )
        Box(
            modifier = Modifier
                .size(240.sdp)
                .background(color, CircleShape),
        )

        val sharpen = remember {
            ColorMatrix(
                floatArrayOf(
                    1f, 0f, 0f, 0f, 0f,
                    0f, 1f, 0f, 0f, 0f,
                    0f, 0f, 1f, 0f, 0f,
                    0f, 0f, 0f, 20f, -100f,
                ),
            )
        }

        // 滤镜的使用是声明相反顺序。
        Canvas(
            modifier = Modifier
                .offset(y=340.sdp)
                .fillMaxSize()
                .graphicsLayer {// 顺序 2
                    renderEffect = RenderEffect.createColorFilterEffect(
                        ColorMatrixColorFilter(sharpen)
                    ).asComposeRenderEffect()
                }
                .blur(40.sdp) // 顺序 1，融合依赖模糊制作，所以模糊会使得面积变大
                .scale(0.8f) // 模糊导致面积变大了，适当缩减了面积。
        ) {
            drawCircle(
                color = color,
                radius = 120f.sf, //融合依赖模糊制作，所以模糊会使得面积变大
                center = Offset(x = size.width / 2, y = 140f.sf),
            )

            drawCircle(
                color = color,
                radius = 70f.sf, // 融合依赖模糊制作，所以模糊会使得面积变大
                center = Offset(x = size.width / 2 + 100f.sf, y = 240f.sf),
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Preview
@Composable
fun FusionPagePreview() {
    DesignPreview {
        FusionPage()
    }
}