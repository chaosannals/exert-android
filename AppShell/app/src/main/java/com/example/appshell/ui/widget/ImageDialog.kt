package com.example.appshell.ui.widget

import android.net.Uri
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import com.example.appshell.ui.sdp

@Composable
fun ImageDialog(
    imageUri: Uri?,
    modifier: Modifier=Modifier,
    onClose: (() -> Unit)? = null,
) {
    val visible by remember(imageUri) {
        derivedStateOf {
            imageUri != null
        }
    }

    AnimatedVisibility(
        visible = visible,
        enter= fadeIn(),
        exit= fadeOut(),
        modifier = modifier
            .fillMaxSize()
            .background(Color.Gray),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .fillMaxSize(),
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .zIndex(10f)
                    .align(Alignment.TopEnd)
                    .padding(10.sdp)
                    .size(24.sdp)
                    .background(Color(0x44FFFFFF), CircleShape)
                    .clickable { onClose?.invoke() },
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "关闭",
                    tint = Color.White,
                    modifier = Modifier.size(16.sdp),
                )
            }

            AsyncImage(
                model = imageUri,
                contentDescription = "图片",
                alignment=Alignment.Center,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .zIndex(1f)
                    .fillMaxSize(),
            )
        }
    }
}

@Preview
@Composable
fun ImageDialogPreview() {
    DesignPreview {
        ImageDialog(null)
    }
}