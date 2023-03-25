package com.example.anidemo.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.anidemo.LocalNavController

fun Modifier.design(
    maxWidth: Dp = 375.sdp,
    maxHeight: Dp = 667.sdp,
) : Modifier {
    this.sizeIn(
        minWidth = 0.dp,
        minHeight = 0.dp,
        maxWidth = maxWidth,
        maxHeight = maxHeight,
    )
    return this
}

@Composable
fun DesignPreview(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalNavController provides rememberNavController(),
    ) {
        Box(
            contentAlignment = Alignment.TopStart,
            modifier=modifier,
        ) {
            content()
        }
    }
}

@Preview
@Composable
fun DesignPreviewPreview() {
    DesignPreview {
        
    }
}