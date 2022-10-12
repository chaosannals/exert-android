package com.example.jcm3demo.ui.page.tool

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
import com.example.jcm3demo.R

@Composable
fun ImageBox(imgPath: String) {
    val shape = RoundedCornerShape(10.dp)
    Column(
        modifier = Modifier
            .padding(10.dp)
            .aspectRatio(1.0f)
            .clip(shape)
            .border(BorderStroke(1.dp, colorResource(id = R.color.gray_2)), shape)
            .padding(10.dp),
    ) {
        Text(imgPath, fontSize = 10.sp)
        val painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imgPath)
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
            Box (
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painterResource(id = R.drawable.ic_error_outline),
                    contentDescription="加载失败",
                    modifier = Modifier.size(100.dp),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ImageBoxPreview() {
    ImageBox("")
}