package com.example.appshell.ui.page.demo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.appshell.ui.sdp
import com.example.appshell.ui.widget.DesignPreview
import com.example.appshell.ui.widget.LazyDragColumn

@Composable
fun LazyDragPage() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyDragColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                Box (
                    modifier = Modifier
                        .size(300.sdp)
                        .background(Color.Red)
                )
            }
            items(100) {
                val color = when (it % 3) {
                    0 -> Color.Red
                    1 -> Color.Green
                    2-> Color.Blue
                    else -> Color.White
                }
                Box (
                    modifier = Modifier
                        .size((100 + it).sdp)
                        .background(color)
                )
            }
        }
    }
}

@Preview
@Composable
fun LazyDragPagePreview() {
    DesignPreview {
        LazyDragPage()
    }
}