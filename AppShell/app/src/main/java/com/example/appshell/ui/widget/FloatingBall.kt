package com.example.appshell.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.appshell.ui.shadow2
import com.example.appshell.ui.sdp

@Composable
fun FloatingBall(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .aspectRatio(1f)
            .shadow2(
                alpha = 0.9f,
                cornersRadius = 50.sdp,
                shadowBlurRadius = 0.4.sdp,
                offsetY = 1.sdp
            )
            .padding(1.sdp)
            .background(Color.White, CircleShape)
            .clip(CircleShape)
            .clickable { onClick() },
    ) {
        Icon(
            imageVector = Icons.Default.Settings,
            contentDescription = "setting",
            modifier = Modifier
                .fillMaxSize(0.84f)
        )
    }
}

@Preview
@Composable
fun FloatingBallPreview() {
    DesignPreview {
        FloatingBall()
    }
}