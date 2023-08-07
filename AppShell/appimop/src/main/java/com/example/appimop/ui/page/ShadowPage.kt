package com.example.appimop.ui.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import com.example.appimop.ui.DesignPreview
import com.example.appimop.ui.sdp
import com.example.appimop.ui.sf

@Composable
fun ShadowPage() {
    val color by remember {
        mutableStateOf(Color(0xFF4499FF))
    }

    Box(
        contentAlignment= Alignment.Center,
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxSize(),
    ) {

        // 没有 XY Offset 模糊距离
        Box(
            modifier = Modifier
                .offset(100.sdp, -240.sdp)
                .size(140.sdp)
                .background(color, CircleShape)
                .shadow(5.sdp, CircleShape, ambientColor = Color(0x0F00689F)),
        )

        // 没有模糊距离
        Box(
            modifier = Modifier
                .offset(-100.sdp, -240.sdp)
                .size(140.sdp)
                .graphicsLayer(
                    translationY = 4f.sf,
                    shadowElevation = 5f.sf,
                    shape = CircleShape,
                    ambientShadowColor = Color(0x0F00689F),
                )
                .background(Color.White, CircleShape),
        )

        Box(
            modifier = Modifier
                .offset(-100.sdp, 240.sdp)
                .size(140.sdp)
                .graphicsLayer {
                    translationY = 4f.sf
                    shadowElevation = 5f.sf
                    shape = CircleShape
                    ambientShadowColor = Color(0x0F00689F)
                }
                .background(Color.White, CircleShape),
        )
    }
}

@Preview
@Composable
fun ShadowPagePreview() {
    DesignPreview {
        ShadowPage()
    }
}