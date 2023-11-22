package com.example.jcm3ui.ui.page

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.jcm3ui.ui.routeTo
import com.example.jcm3ui.ui.sdp

@Composable
fun HomeButton(
    title: String,
    path: String,
) {
    Text(
        text = title,
        modifier = Modifier
            .padding(10.sdp)
            .clickable {
                routeTo(path)
            }
    )
}

@Preview
@Composable
fun HomePage() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        HomeButton("文件选择", "demo/file-pick")
        HomeButton("拍摄", "demo/camera-shot")
    }
}