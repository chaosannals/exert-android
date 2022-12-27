package com.example.jcmdemo.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.jcmdemo.LocalNavController

@Composable
fun DesignPreview(
    modifier: Modifier = Modifier,
    content: @Composable ()->Unit,
) {
    CompositionLocalProvider(
        LocalNavController provides rememberNavController(),
    ) {
        Box(
            modifier = modifier
                .sizeIn(
                    minWidth = 0.dp,
                    maxWidth = 375.sdp,
                    minHeight = 0.dp,
                    maxHeight = 667.sdp,
                ),
        ) {
            content()
        }
    }
}

@Preview
@Composable
fun DesignPreviewPreview() {
    DesignPreview() {

    }
}