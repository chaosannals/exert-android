package com.example.appshell.ui.page.demo

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import coil.ImageLoader
import coil.compose.*
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.ImageRequest
import coil.size.Size
import com.example.appshell.R
import com.example.appshell.ui.sdp
import com.example.appshell.ui.widget.DesignPreview
import kotlin.random.Random

@Composable
fun CoilPhotoPage() {
    val context = LocalContext.current
    val pictureUrl by remember {
        val pid = Random.nextInt(100, 400)
        mutableStateOf("https://picsum.photos/id/$pid/400/400")
    }
    val photo = rememberAsyncImagePainter(
        imageLoader = ImageLoader.Builder(context)
            .memoryCache {
                MemoryCache.Builder(context)
                    .maxSizePercent(0.25)
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(context.cacheDir.resolve("image_cache"))
                    .maxSizePercent(0.02)
                    .build()
            }
            .build(),
        model = ImageRequest.Builder(context)
            .data(pictureUrl)
            .size(Size.ORIGINAL)
            .build()
    )

    var photoModel: Any? by remember {
        mutableStateOf(pictureUrl)
    }

    val fileLoader = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
    ) { result ->
        result?.let {
            photoModel = ImageRequest.Builder(context)
                .data(it)
                .size(Size.ORIGINAL)
                .build()
        }
    }

    val lazyState = rememberLazyListState()

    LazyColumn (
        state = lazyState,
        verticalArrangement=Arrangement.Center,
        horizontalAlignment=Alignment.CenterHorizontally,
        modifier = Modifier
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .padding(14.sdp)
            ) {
                val painter =
                    if (photo.state is AsyncImagePainter.State.Loading || photo.state is AsyncImagePainter.State.Error) {
                        painterResource(id = R.drawable.no_photo)
                    } else {
                        photo.state.painter
                    }

                Image(
                    painter = painter!!,
                    contentDescription = "图片",
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .fillMaxSize(),
                )
            }
        }

        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .padding(14.sdp),
            ) {
                AsyncImage(
                    model = "https://picsum.photos/id/4/400/400",
                    contentDescription = "AsyncImage",
                    placeholder = painterResource(id = R.drawable.no_photo),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize(),
                )
            }
        }

        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .padding(14.sdp)
                    .clickable
                    {
                        fileLoader.launch("image/*")
                    },
            ) {
                SubcomposeAsyncImage(
                    model = photoModel,
                    loading = {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .fillMaxSize(),
                        )
                    },
                    contentScale = ContentScale.Crop,
                    contentDescription = "SubcomposeAsyncImage",
                    modifier = Modifier
                        .fillMaxSize(),
                )
            }
        }
        item {
            SubcomposeAsyncImage(
                model = pictureUrl,
                contentDescription = "SubcomposeAsyncImage content"
            ) {
                val state = painter.state
                if (state is AsyncImagePainter.State.Loading || state is AsyncImagePainter.State.Error) {
                    CircularProgressIndicator()
                } else {
                    SubcomposeAsyncImageContent()
                }
            }
        }
    }
}

@Preview
@Composable
fun CoilPhotoPagePreview() {
    DesignPreview {
        CoilPhotoPage()
    }
}