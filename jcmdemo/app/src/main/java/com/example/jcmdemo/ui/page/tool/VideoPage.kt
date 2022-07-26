package com.example.jcmdemo.ui.page.tool

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jcmdemo.ui.VideoPlayer
import com.example.jcmdemo.ui.VideoPlayer2
import com.example.jcmdemo.ui.VideoPlayer3

@Composable
fun VideoPage() {
    val context = LocalContext.current
//    val f = context
//        .getOutputDirectory()
//        .walk()
//        .maxDepth(1)
//        .filter { it.isFile && it.extension in listOf("mp4") }
//        .first()
    val fs = context
        .getOutputDirectory()
        .walk()
        .maxDepth(1)
        .filter { it.isFile && it.extension in listOf("mp4") }
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(0.dp, 0.dp, 0.dp, 60.dp),

    ) {
        //VideoPlayer3(path = f.path)
        fs.forEach { f ->
            VideoPlayer3(path = f.path)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun VideoPagePreview() {
    VideoPage()
}