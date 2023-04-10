package com.example.appshell.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.zIndex
import com.example.appshell.ui.ssp

@Composable
fun LoadingPane(
    modifier: Modifier=Modifier,
    onClicked: (() -> Unit)? = null,
) {
    Box (
        contentAlignment = Alignment.Center,
        modifier = modifier
            .zIndex(1000f)
            .fillMaxSize()
            .background(Color(0x1A000000))
            .clickable {
                onClicked?.invoke()
            },
    ) {
        Text(
            text = "Loading...",
            color= Color.White,
            fontSize = 24.ssp,
        )
    }
}

@Preview
@Composable
fun LoadingPanePreview() {
    DesignPreview() {
        LoadingPane()
    }
}