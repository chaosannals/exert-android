package com.example.jcm3ui.ui.page.demo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun FileViewPage() {
    Column(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        // 顶部
        Row(
            horizontalArrangement= Arrangement.SpaceBetween,
            verticalAlignment= Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            
        }
    }
}