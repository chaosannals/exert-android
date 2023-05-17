package com.example.app24.ui.page.demo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.app24.ui.DesignPreview

@Composable
fun WebSecondPage() {
    Column (
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxSize(),
    ) {
        Text("web-second-page")
    }
}

@Preview
@Composable
fun WebSecondPagePreview() {
    DesignPreview {
        WebSecondPage()
    }
}