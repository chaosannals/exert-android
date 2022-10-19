package com.example.anidemo.ui.widget

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun PullDownPushUpBox(
    modifier: Modifier = Modifier,
    content: (@Composable (Float) -> Unit)? = null,
) {
    var contentHeight by remember {
        mutableStateOf(0)
    }
    var contentSurplusHeight by remember {
        mutableStateOf(0)
    }
}

@Preview(widthDp = 375, heightDp = 668)
@Composable
fun PullDowPushUpBoxPreview() {
    PullDownPushUpBox()
}