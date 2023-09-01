package com.example.bootdemo.ui.page.drawing

import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.decode.SvgDecoder
import coil.decode.VideoFrameDecoder
import coil.request.ImageRequest
import coil.request.ImageResult

@Composable
fun CoilGifPage() {
    val context = LocalContext.current
    val imageLoader = remember(context) {
        ImageLoader.Builder(context)
            .components {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
                add(SvgDecoder.Factory())
                add(VideoFrameDecoder.Factory())
            }
            .build()
    }

    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        AsyncImage(
            model = "file:///android_asset/test.gif",
            contentDescription = "1",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .border(1.dp, Color.Black),
        )

        AsyncImage(
            model = "file:///android_asset/test.gif",
            imageLoader = imageLoader,
            contentDescription = "2",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .border(1.dp, Color.Black),
        )

        // 此废弃函数可以
        Image(
            painter = rememberImagePainter(
                data = "file:///android_asset/test.gif",
                imageLoader = imageLoader
            ),
            contentDescription = "废弃函数",
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .border(1.dp, Color.Black)
        )

        AsyncImage(
            model = "file:///android_asset/test.svg",
            imageLoader = imageLoader,
            contentDescription = "SVG",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .border(1.dp, Color.Black),
        )
    }
}

@Preview
@Composable
fun CoilGifPagePreview() {
    CoilGifPage()
}