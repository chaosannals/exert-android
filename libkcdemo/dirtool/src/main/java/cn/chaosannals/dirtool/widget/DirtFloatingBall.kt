package cn.chaosannals.dirtool.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import cn.chaosannals.dirtool.DirtPreview
import cn.chaosannals.dirtool.sdp

@Composable
fun DirtFloatingBall(
    modifier: Modifier = Modifier,
    icon: ImageVector? = Icons.Default.Info,
    onClick: (() -> Unit)?=null,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(44.sdp)
            .clip(CircleShape)
            .background(Color(0xFF4499FF))
            .clickable { onClick?.invoke() },
    ) {
        icon?.let {
            Icon(
                imageVector = icon,
                tint = Color.White,
                contentDescription = "icon",
                modifier = Modifier
                    .size(24.sdp)
            )
        }
    }
}

@Preview
@Composable
fun DirtFloatingBallPreview() {
    DirtPreview() {
        DirtFloatingBall()
    }
}