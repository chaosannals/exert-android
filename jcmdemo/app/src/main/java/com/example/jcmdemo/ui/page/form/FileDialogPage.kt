package com.example.jcmdemo.ui.page.form

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.net.toFile
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.example.jcmdemo.ui.DesignPreview
import com.example.jcmdemo.ui.sdp
import com.example.jcmdemo.ui.ssp

@Composable
fun FileDialogPage() {
    val pictureUrl = null
    val context = LocalContext.current

    var image: Any? by remember {
        mutableStateOf(ImageRequest.Builder(context)
            .data(pictureUrl)
            .size(Size.ORIGINAL)
            .build())
    }
    val pickPictureLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) {
        it?.let {
            image = ImageRequest.Builder(context)
                .data(it)
                .size(Size.ORIGINAL)
                .build()
        }
    }

    Box(
        modifier = Modifier
            .size(60.sdp)
            .clip(CircleShape)
            .background(Color(0xFFF4F6F8))
            .clickable {
                pickPictureLauncher.launch("image/*")
            },
    ) {
        AsyncImage(
            model = image,
            contentDescription = "图片",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxSize(),
        )
        Box(
            contentAlignment= Alignment.Center,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .height(18.5.sdp)
                .fillMaxWidth()
                .background(Color(0x44444444)),
        ) {
            Text(
                text="编辑",
                color=Color.White,
                fontSize = 11.ssp,
            )
        }
    }
}

@Preview
@Composable
fun FileDialogPagePreview() {
    DesignPreview {
        FileDialogPage()
    }
}
