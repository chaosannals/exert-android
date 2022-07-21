package com.example.jcmdemo.ui.page.tool

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jcmdemo.ui.VideoPlayer

@Composable
fun VideoPage() {
    val context = LocalContext.current
    val f = context
        .getOutputDirectory()
        .walk()
        .maxDepth(1)
        .filter { it.isFile && it.extension in listOf("mp4") }
        .first()
    Column(
        modifier = Modifier.fillMaxSize()
            .padding(0.dp,0.dp,0.dp, 60.dp)
    ) {
        VideoPlayer(path = f.path)
    }
}

@Preview(showBackground = true)
@Composable
fun VideoPagePreview() {
    VideoPage()
}