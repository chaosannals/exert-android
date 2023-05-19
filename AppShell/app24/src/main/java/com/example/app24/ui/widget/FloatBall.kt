package com.example.app24.ui.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.app24.ui.DesignPreview
import com.example.app24.ui.sdp
import com.example.app24.ui.shadow2

@Composable
fun FloatBall(
    modifier: Modifier=Modifier,
) {
    var floatExpanded by remember { mutableStateOf(false) }

    Box(
        contentAlignment=Alignment.Center,
        modifier= modifier
            .clip(CircleShape)
            .shadow2(
                color = Color.Black,
                alpha = 0.4f,
                cornersRadius = 20.5.sdp,
                shadowBlurRadius = 4.sdp,
                offsetY = (-0.5).sdp,
            )
            .padding(0.5.sdp)
            .offset(y = (-0.5).sdp)
            .background(
                color = Color.White,
                shape = CircleShape,
            )
            .clickable {
                floatExpanded = true
            },
    ) {
        Image(
            imageVector = Icons.Default.Settings,
            contentDescription = "setting",
            modifier = Modifier
                .size(40.sdp)
        )
        FloatMenu(
            expanded = floatExpanded,
            onDismissRequest = {floatExpanded = false},
        )
    }
}

@Preview
@Composable
fun FloatBallPreview() {
    DesignPreview {
        FloatBall()
    }
}