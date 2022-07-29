package com.example.jcmdemo.ui.page.tool

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.example.jcmdemo.R
import java.io.File

@Composable
fun ImageItem(path: String) {
    val shape = RoundedCornerShape(10.dp)
    Column(
        modifier = Modifier
            //.fillMaxWidth()
            .padding(10.dp)
            .aspectRatio(1.0f)
            .clip(shape)
            .border(BorderStroke(1.dp, colorResource(id = R.color.gray_2)), shape)
            .padding(10.dp),
    ) {
        Text(path, fontSize = 10.sp)
        var painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data(path)
                .size(Size.ORIGINAL) // Set the target size to load the image at.
                .build()
        )
        if (painter.state is AsyncImagePainter.State.Success) {
            Image(
                painter = painter,
                contentDescription = "图片",
                modifier = Modifier.fillMaxWidth()
            )
        }
        else {
            Icon(painterResource(id = R.drawable.ic_gist), contentDescription="加载失败")
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun ImagesPage() {
    val context = LocalContext.current
    var fs : MutableList<String> by remember {
        var ml = mutableListOf<String>()
        var ft = context.getOutputDirectory().walk()
        ft.maxDepth(1)
            .filter { it.isFile }
            .filter { it.extension in listOf("png", "jpg") }
            .forEach { ml.add(it.path) }
        mutableStateOf(ml)
    }

    Text("fs: ${fs.size}", fontSize = 10.sp)
    LazyVerticalGrid(
        cells = GridCells.Fixed(2),
        modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 60.dp)
    ) {
        items(fs.size) { i ->
            val p = fs[i]
            ImageItem(path = p)
        }
    }
}

@ExperimentalFoundationApi
@Preview(showBackground = true)
@Composable
fun ImagesPagePreview() {
    ImagesPage()
}