package com.example.jcmdemo.ui.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.example.jcmdemo.ui.DesignPreview

@Composable
fun PictureViewer(
    url: String,
    modifier: Modifier=Modifier,
    onClicked: (() -> Unit)?=null,
) {
    var scale by remember {
        mutableStateOf(1f)
    }
    var rotation by remember {
        mutableStateOf(0f)
    }
    var offset by remember {
        mutableStateOf(Offset.Zero)
    }
    val state = rememberTransformableState { zoomChange, panChange, rotationChange ->
        scale *= zoomChange
        rotation += rotationChange
        offset += panChange
    }

    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(url)
            .size(Size.ORIGINAL)
            .build()
    )


    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
        .fillMaxSize()
        .graphicsLayer {
            // graphicsLayer 会翻转事件区域。
            // 控件的区域又没有被翻转。事件既没有触发也没有冒泡给父级。
            scaleX = scale
            scaleY = scale
            rotationZ = rotation
            translationX = offset.x
            translationY = offset.y
        }.transformable(state = state)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
            ) {
                onClicked?.invoke()
            },
    ) {
        Image(
            painter = painter,
            contentDescription = "图片",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Cyan),
        )
    }
}

@Preview
@Composable
fun PictureViewerPreview() {
    DesignPreview() {
        PictureViewer(
            url="",
        )
    }
}