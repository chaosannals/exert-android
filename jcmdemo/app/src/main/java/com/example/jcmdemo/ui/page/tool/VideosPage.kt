package com.example.jcmdemo.ui.page.tool

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jcmdemo.R
import com.example.jcmdemo.ui.VideoPlayer

@Composable
fun VideoItem(path: String) {
    Column(
        modifier = Modifier
            .padding(10.dp)
            .border(BorderStroke(1.dp, colorResource(id = R.color.gray_2))),
    ) {
        Text(text = path, fontSize = 10.sp)
        VideoPlayer(path = path, modifier = Modifier.fillMaxWidth())
    }
}

@ExperimentalFoundationApi
@Composable
fun VideosPage() {
    val context = LocalContext.current
    var fs : MutableList<String> by remember {
        mutableStateOf(
            context.getOutputDirectory().walk()
                .maxDepth(1)
                .filter { it.isFile && it.extension in listOf("mp4") }
                .map { it.path }.toMutableList()
        )
    }
    Column (
        modifier = Modifier.fillMaxSize()
    ){
        Text("videos: ${fs.size}")
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.weight(1.0f)
                .padding(0.dp, 0.dp, 0.dp, 50.dp)
        ) {
            items(fs.size) { i ->
                VideoItem(fs[i])
            }
        }
    }
}

@ExperimentalFoundationApi
@Preview(showBackground = true)
@Composable
fun VideosPagePreview() {
    VideosPage()
}