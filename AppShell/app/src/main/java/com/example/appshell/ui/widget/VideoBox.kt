package com.example.appshell.ui.widget


import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import com.example.appshell.VideoKit
import com.example.appshell.ui.sdp
import com.example.appshell.ui.ssp
import com.example.appshell.VideoKit.init
import com.example.appshell.VideoKit.pauseUnique
import com.example.appshell.VideoKit.playUnique
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ui.StyledPlayerView
import java.util.UUID

@Composable
fun VideoBox(
    videoUrl: Uri?,
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

    var isStartedPlay by remember(videoUrl, currentVideoId) {
        mutableStateOf(false)
    }
    val isCurrentPause by remember(currentVideoId, videoId) {
        derivedStateOf {
            currentVideoId == videoId // && isStartedPlay
        }
    }
    val playView: StyledPlayerView by remember(videoUrl) {
        mutableStateOf(StyledPlayerView(context))
    }

    var isPlaying by remember(videoUrl, currentVideoId) {
        mutableStateOf(currentVideoId == videoId)
    }
    val thumb = remember(videoUrl) {
        videoUrl?.let {
            val mmr = MediaMetadataRetriever()
            mmr.setDataSource(context, videoUrl)
            mmr.getFrameAtTime(1)?.asImageBitmap()
        }
    }
    val ratio by remember(thumb) {
        mutableStateOf(if (thumb != null) {
            thumb.width.toFloat() / thumb.height.toFloat()
        } else {
            1.0f
        })
    }

    LaunchedEffect(isCurrentPause) {
        Log.d("video-box", "isCurrentPause $isCurrentPause")
        if (isCurrentPause) {
            playView.player = player
        } else {
            playView.player = null
        }
    }

    Log.d("video-box", "disposable effect before $isPlaying $videoUrl($videoId)")
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

    val shape = RoundedCornerShape(10.sdp)
    Column(
        modifier = Modifier
            .padding(10.sdp)
            .clip(shape)
            .border(BorderStroke(1.sdp, Color.Gray), shape)
            .padding(10.sdp),
    ) {
        Text(videoUrl.toString(), fontSize = 10.ssp)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(ratio),
            contentAlignment = Alignment.Center,
        ) {
//            Log.d("video-box", "android-view before $isPlaying")
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .zIndex(10.0f)
                    .fillMaxSize()
                    .clickable {
                        Log.d("video-box", "box click $isPlaying")

                        isPlaying = !isPlaying

                        if (!isStartedPlay) {
                            isStartedPlay = true
                        }

                        if (isPlaying) {
                            player?.playUnique(videoUrl, videoId)
                        } else {
                            player?.pauseUnique(videoUrl, videoId)
                        }
                    },
            ) {
                if (!isPlaying) {
                    if (thumb != null) {
                        if (!isStartedPlay) {
                            Image(
                                bitmap = thumb,
                                contentDescription = "图片",
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .zIndex(1.0f),
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "播放",
                            tint = Color.White,
                            modifier = Modifier
                                .fillMaxSize(0.6f)
                                .zIndex(2.0f),
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "加载失败",
                            modifier = Modifier.size(300.sdp),
                        )
                    }
                }
            }

            AndroidView(
                {
                    playView
                },
                modifier = Modifier
                    .zIndex(1.0f)
                    .fillMaxSize(),
            ) {
                playView.init(player)
                Log.d("video-box", "android view reset ")
            }
        }
    }
}

@Preview
@Composable
fun VideoBoxPreview() {
    DesignPreview() {
        VideoBox(
//            videoUrl = Uri.parse("https://www.w3school.com.cn/example/html5/mov_bbb.mp4"),
            videoUrl = null,
        )
    }
}