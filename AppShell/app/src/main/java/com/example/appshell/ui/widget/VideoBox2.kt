package com.example.appshell.ui.widget

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.Log
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.appshell.ui.sdp
import com.example.appshell.ui.ssp
import com.example.appshell.ui.widget.VideoPlayer2.playFromUri
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.StyledPlayerView
import io.reactivex.rxjava3.subjects.BehaviorSubject
import java.util.UUID

// TODO 第二种实现，使用 CompositionLocalProvider

enum class Video2Mode {
    IsLoaded,
    IsPlaying,
    IsPause,
}

interface VideoPlayer2Listener: Player.Listener {
    fun getId() : String
    fun onBeReplaced()
}

object VideoPlayer2 {
    val exoPlayer: BehaviorSubject<ExoPlayer> = BehaviorSubject.create()
    val exoListener: BehaviorSubject<VideoPlayer2Listener> = BehaviorSubject.create()

    fun ensure(context: Context, listener: VideoPlayer2Listener): ExoPlayer {
        Log.d("video-box", "ensure")

        if (exoPlayer.value == null) {
            val drf = DefaultRenderersFactory(context)
                .setExtensionRendererMode(DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER)
            exoPlayer.onNext(ExoPlayer.Builder(context, drf).build().apply {
                addListener(listener)
                playWhenReady = false
            })
            exoListener.onNext(listener)
        } else {
            exoPlayer.value!!.stop()
            exoPlayer.value!!.clearMediaItems()
            exoListener.value?.let {
                exoPlayer.value!!.removeListener(it)
                Log.d("video-box", "before onBeReplaced ${listener.getId()} => ${it.getId()}")
                if (listener.getId() != it.getId()) {
                    it.onBeReplaced()
                }
            }
            exoPlayer.value!!.addListener(listener)
            exoListener.onNext(listener)
        }

        return exoPlayer.value!!
    }

    fun ExoPlayer.playFromUri(uri: Uri) {
        val mi = MediaItem.fromUri(uri)
        this.clearMediaItems()
        this.setMediaItem(mi)
        this.prepare()
        this.play()
    }
}

@Composable
fun VideoBox2(
    videoUrl: Uri?,
) {
    val context = LocalContext.current
    var mode by remember(videoUrl) { mutableStateOf(Video2Mode.IsLoaded) }
    val videoId by remember(videoUrl) {
        mutableStateOf(UUID.randomUUID().toString())
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

    val listener by remember(videoUrl) {
        mutableStateOf(
            object : VideoPlayer2Listener {
                override fun getId(): String {
                    return videoId
                }
                override fun onRenderedFirstFrame() {
                }
                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    Log.d("video-box", "onIsPlayingChanged $mode => $isPlaying")
//                    mode = if (isPlaying) Video2Mode.IsPlaying else Video2Mode.IsPause
                }

                override fun onBeReplaced() {
                    Log.d("video-box", "onBeReplaced $mode")
                    mode = Video2Mode.IsLoaded
                }

                override fun onPlayerError(error: PlaybackException) {

                }
            }
        )
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
            Log.d("video-box", "android-view before $mode")
            if (mode == Video2Mode.IsPlaying) {
                AndroidView(
                    {
                        StyledPlayerView(it)
                    },
                    modifier = Modifier
                        .zIndex(1.0f)
                        .fillMaxSize(),
                ) {
                    Log.d("video-box", "android view reset ")
                    val ep = VideoPlayer2.ensure(context, listener)
                    it.player = ep
                    it.useController = false
                    it.layoutParams = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    videoUrl?.let {
                        ep.playFromUri(it)
                    }
                }

//                VideoPauseBox(
//                    isPause=mode == Video2Mode.IsPause,
//                    onClick = {
//                        Log.d("video-box", "video-pause-box: click $mode")
//                        mode = if (mode == Video2Mode.IsPause) Video2Mode.IsPlaying else Video2Mode.IsPause
//                        if (mode == Video2Mode.IsPause) {
//                            VideoPlayer.pause()
//                        } else {
//                            VideoPlayer.play()
//                        }
//                    }
//                )
            } else if (thumb != null) {
                Image(
                    bitmap = thumb,
                    contentDescription = "图片",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxSize()
                        .zIndex(1.0f),
                )
                IconButton(
                    modifier = Modifier.zIndex(2.0f),
                    onClick = {
                        Log.d("video-box", "thumb click")
                        mode = Video2Mode.IsPlaying
                    },
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "播放",
                        tint = Color.White,
                        modifier = Modifier.fillMaxSize(0.6f),
                    )
                }
            } else {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription="加载失败",
                    modifier = Modifier.size(300.sdp),
                )
            }
        }
    }
}

@Preview
@Composable
fun VideoBox2Preview() {
    DesignPreview {
        VideoBox2(videoUrl = null)
    }
}