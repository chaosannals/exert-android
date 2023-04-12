package com.example.appshell.ui.page.demo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.appshell.ui.widget.DesignPreview
import com.example.appshell.ui.widget.LazyNestedPreColumn

@Composable
fun LazyNestedPreColumnPage() {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        LazyNestedPreColumn() {
            items(40) { index ->
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