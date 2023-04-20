package com.example.appshell.ui.page.demo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.appshell.ui.sdp
import com.example.appshell.ui.widget.DesignPreview
import com.example.appshell.ui.widget.LazyNestedPreColumn

@Composable
fun LazyNestedPreColumnPage() {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        LazyNestedPreColumn() {
            item {
                Box(modifier = Modifier.size(240.sdp).background(Color.Red))
            }
            item {
                Box(modifier = Modifier.size(140.sdp).background(Color.Green))
            }
            item {
                Box(modifier = Modifier.size(240.sdp).background(Color.Blue))
            }
            item {
                Box(modifier = Modifier.size(140.sdp).background(Color.Cyan))
            }
            items(4) { index ->
                Text(
                    "I'm item $index",
                    modifier = Modifier
//                        .fillMaxWidth()
                        .padding(16.dp),
                )
            }
        }
    }
}

@Preview
@Composable
fun LazyNestedPreColumnPagePreview() {
    DesignPreview {
        LazyNestedPreColumnPage()
    }
}