package com.example.appshell.ui.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import com.example.appshell.ui.dp2px
import com.example.appshell.ui.sdp
import com.example.appshell.ui.shadow2

@Composable
fun NavbarButton(
    imageVector: ImageVector,
    modifier: Modifier=Modifier,
    onClicked: (() -> Unit)?=null,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(40.sdp)
            .clickable
            {
                onClicked?.invoke()
            },
    ) {
        Image(
            imageVector = imageVector,
            contentDescription = "image",
            modifier = Modifier
                .size(34.sdp)
        )
    }
}

@Composable
fun Navbar(
    modifier: Modifier=Modifier,
    content: @Composable () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .shadow2(
                color=Color(0x1A4499FF),
                alpha = 0.4f,
//                cornersRadius = 1.sdp,
                shadowBlurRadius = 0.4.sdp,
                offsetY = 0.4.sdp,
            )
            .padding(top=0.8.sdp)
            .background(
                color = Color.White,
            )
            .padding(4.sdp)
            .fillMaxWidth()
    ) {
        content()
    }
}

@Preview
@Composable
fun NavbarButtonPreview() {
    DesignPreview() {
        NavbarButton(
            imageVector = Icons.Default.Home
        )
    }
}

@Preview
@Composable
fun NavbarPreview() {
    DesignPreview {
        Navbar() {
            NavbarButton(
                imageVector = Icons.Default.Home
            )
            NavbarButton(
                imageVector = Icons.Default.Build
            )
            NavbarButton(
                imageVector = Icons.Default.Settings
            )
        }
    }
}