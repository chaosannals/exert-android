package com.example.jcm3demo.ui.page.tool

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun VideosPage() {
    val context = LocalContext.current
    var fs : MutableList<String> by remember {
        mutableStateOf(
            context
                .getOutputDirectory()
                .walk()
                .maxDepth(1)
                .filter { it.isFile }
                .filter { it.extension in listOf("mp4") }
                .map { it.path }
                .toMutableList()
        )
    }
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 10.dp)
    ) {
//    LazyColumn {
        items(fs) {
            VideoBox(videoPath = it)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun VideosPagePreview() {
    VideosPage()
}