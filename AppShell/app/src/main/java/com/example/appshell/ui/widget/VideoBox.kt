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
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PlayArrow
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
import com.example.appshell.ui.sdp
import com.example.appshell.ui.ssp
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.StyledPlayerView
import java.util.UUID

// TODO 暂停有问题。

interface VideoPlayerListener : Player.Listener {
    fun getId() : String
    fun onBeReplaced()
//    fun onEnsured() {}
}

object VideoPlayer {
    private var exoPlayer: ExoPlayer? = null
    private var exoListener: VideoPlayerListener? = null

    fun ensure(context: Context, listener: VideoPlayerListener): ExoPlayer {
        Log.d("video-box", "ensure")

        if (exoPlayer == null) {
            val drf = DefaultRenderersFactory(context)
                .setExtensionRendererMode(EXTENSION_RENDERER_MODE_PREFER)
            exoPlayer = ExoPlayer.Builder(context, drf).build().apply {
                addListener(listener)
                playWhenReady = false
            }
            exoListener = listener
        } else {
            exoPlayer!!.stop()
            exoPlayer!!.clearMediaItems()
            exoListener?.let {
                exoPlayer!!.removeListener(it)
                Log.d("video-box", "before onBeReplaced ${listener.getId()} => ${it.getId()}")
                if (listener.getId() != it.getId()) {
                    it.onBeReplaced()
                }
            }
            exoPlayer!!.addListener(listener)
            exoListener = listener
        }

//        listener.onEnsured()
        return exoPlayer!!
    }

    fun pause() {
        exoPlayer?.pause()
    }

    fun play() {
        exoPlayer?.play()
    }
}

fun ExoPlayer.playFromUri(uri: Uri) {
    val mi = MediaItem.fromUri(uri)
    this.clearMediaItems()
    this.setMediaItem(mi)
    this.prepare()
    this.play()
}

@Composable
fun VideoPauseBox(
    isPause: Boolean,
    onClick: (() -> Unit)? = null,
) {

    Box(
        modifier = Modifier
            .zIndex(2.0f)
            .clickable {
                onClick?.invoke()
            }
            .fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        if (isPause) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "播放（暂停）",
                tint = Color.Cyan,
                modifier = Modifier.fillMaxSize(0.6f),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun VideoPauseBoxPreview() {
    VideoPauseBox(true)
}

@Composable
fun VideoBox(
    videoUrl: Uri?,
) {
    val context = LocalContext.current
    var isPlaying by remember(videoUrl) { mutableStateOf(false) }
    var isPause by remember(videoUrl) { mutableStateOf(false) }

//    val viewId by remember {
//        mutableStateOf(UUID.randomUUID().toString())
//    }

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
            object : VideoPlayerListener {
                override fun getId(): String {
//            return videoUrl.toString()
                    return videoId
                }
                override fun onRenderedFirstFrame() {
                }
                override fun onIsPlayingChanged(isplaying: Boolean) {
                    Log.d("video-box", "onIsPlayingChanged $isPlaying => $isplaying")
                    if (!isPause) {
                        isPlaying = isplaying
                    }
                }

                override fun onBeReplaced() {
                    Log.d("video-box", "onBeReplaced $isPlaying")
                    isPause = false
                    isPlaying = false
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
            Log.d("video-box", "android-view before $isPlaying")
            if (isPlaying) {
                AndroidView(
                    {
                        StyledPlayerView(it)
                    },
                    modifier = Modifier
                        .zIndex(1.0f)
                        .fillMaxSize(),
                ) {
                    Log.d("video-box", "android view reset ")
                    val ep = VideoPlayer.ensure(context, listener)
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

                VideoPauseBox(
                    isPause=isPause,
                    onClick = {
                        Log.d("video-box", "video-pause-box: click $isPause")
                        isPause = !isPause
                        if (isPause) {
                            VideoPlayer.pause()
                        } else {
                            VideoPlayer.play()
                        }
                    }
                )
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
                        isPlaying = true
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
