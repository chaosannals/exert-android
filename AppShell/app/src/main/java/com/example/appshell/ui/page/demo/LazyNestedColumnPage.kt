package com.example.appshell.ui.page.demo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.appshell.ui.sdp
import com.example.appshell.ui.widget.DesignPreview
import com.example.appshell.ui.widget.LazyNestedColumn

@Composable
fun LazyNestedColumnPage() {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        LazyNestedColumn() {
            item {
                Box(
                    modifier = Modifier
                        .size(140.sdp)
                        .background(Color.Red),
                )
            }
            items(40) { index ->
                Text(
                    "I'm nested item $index",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                )
            }
        }
    }
}

@Preview
@Composable
fun LazyNestedColumnPagePreview() {
    DesignPreview {
        LazyNestedColumnPage()
    }
}