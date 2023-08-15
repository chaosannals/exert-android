package com.example.bootdemo.ui.widget

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bootdemo.ui.LocalRouter

@Composable
fun FloatBall(
    icon: ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    val mode = LocalInspectionMode.current
    val router = if (mode) null else LocalRouter.current

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(64.dp)
            .background(Color.White, CircleShape)
            .shadow(1.dp, CircleShape)
            .clickable
            {
                onClick()
            },
        ) {
        Icon(
            imageVector = icon,
            contentDescription = "打印信息",
            modifier = Modifier
                .fillMaxSize(0.84f)
            )
    }
}

@Preview
@Composable
fun FloatBallPreview() {
    FloatBall(Icons.Default.Info)
}