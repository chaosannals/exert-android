package com.example.appshell.ui.widget

import android.net.Uri
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import com.example.appshell.VideoKit
import com.example.appshell.VideoKit.init
import com.example.appshell.VideoKit.loadVideoThumb
import com.example.appshell.VideoKit.playUnique
import com.example.appshell.ui.sdp
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ui.StyledPlayerView
import java.util.UUID

@Composable
fun VideoDialog(
    videoUrl: Uri?,
    modifier: Modifier=Modifier,
    onClose: (() -> Unit)? = null,
) {
    val context = LocalContext.current
    var player: ExoPlayer? by remember {
        mutableStateOf(VideoKit.exoPlayer.value)
    }
    var currentVideoId: String? by remember {
        mutableStateOf(VideoKit.currentId.value)
    }
    val videoId by remember(videoUrl) {
        mutableStateOf(UUID.randomUUID().toString())
    }

    val playView: StyledPlayerView by remember(videoUrl) {
        mutableStateOf(StyledPlayerView(context))
    }

    val thumb = remember(videoUrl) {
        context.loadVideoThumb(videoUrl)
    }
    val ratio by remember(thumb) {
        mutableStateOf(if (thumb != null) {
            thumb.width.toFloat() / thumb.height.toFloat()
        } else {
            1.0f
        })
    }

    val isCurrent by remember(currentVideoId, videoId) {
        derivedStateOf {
            currentVideoId == videoId
        }
    }

    val visible by remember(videoUrl) {
        derivedStateOf {
            videoUrl != null
        }
    }

    LaunchedEffect(isCurrent) {
        if (isCurrent) {
            playView.player = player
        } else {
            playView.player = null
        }
    }

    DisposableEffect(Unit) {
        val playerDisposable = VideoKit.exoPlayer.subscribe {
            player = it
        }
        val currentVedioIdDisposable = VideoKit.currentId.subscribe {
            currentVideoId = it
        }
        onDispose {
            playerDisposable.dispose()
            currentVedioIdDisposable.dispose()
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
            modifier = Modifier
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
            AndroidView(
                {
                    playView
                },
                modifier = Modifier
                    .zIndex(1.0f)
                    .fillMaxWidth()
                    .aspectRatio(ratio),
            ) {
                playView.init(player, useControl = true)
                player?.playUnique(videoUrl, videoId)
                Log.d("video-box", "android view reset ")
            }
        }
    }
}

@Preview
@Composable
fun VideoDialogPreview() {
    DesignPreview() {
        VideoDialog(null)
    }
}