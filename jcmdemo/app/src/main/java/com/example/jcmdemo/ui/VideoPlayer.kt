package com.example.jcmdemo.ui

import android.content.Context
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.jcmdemo.R
import com.example.jcmdemo.ui.page.tool.getOutputDirectory
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.StyledPlayerControlView
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.exoplayer2.ui.SubtitleView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.util.MimeTypes
//import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import java.io.File
import android.media.MediaMetadataRetriever
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.asImageBitmap
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.DefaultRenderersFactory.EXTENSION_RENDERER_MODE_ON
import com.google.android.exoplayer2.DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER

fun writeLog(context: Context, text: String) {
    var f = File(context.getOutputDirectory(), "1a.log")
    f.appendText(text)
    f.appendText("\r\n")
}

//@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun VideoPlayer(path: String, modifier: Modifier=Modifier) {
    val context = LocalContext.current

    writeLog(context, "compose $path")

    var isPlaying by remember { mutableStateOf(false) }
    var state by remember { mutableStateOf(Player.STATE_IDLE) }
    var vratio by remember { mutableStateOf(1.0f) }
    val listener = object : Player.Listener {
        override fun onRenderedFirstFrame() {
            writeLog(context, "render first $path")
        }
        override fun onIsPlayingChanged(isplaying: Boolean) {
            writeLog(context, "isplayc: ($isplaying) $path")
            isPlaying = isplaying
        }

        override fun onPlaybackStateChanged(playbackState: Int) {
            writeLog(context, "statec: ($playbackState) $path")
            state = playbackState
        }

        override fun onPlayerError(error: PlaybackException) {
            writeLog(context, "error $path  ${error.errorCodeName} => ${error.stackTraceToString()}")
        }
    }

    var thumb = remember {
        writeLog(context, "thumb: $path")
        var mmr = MediaMetadataRetriever()
        mmr.setDataSource(path)
        mmr.getFrameAtTime(1)?.asImageBitmap()
    }

    val exoPlayer = remember {
        var drf = DefaultRenderersFactory(context)
            .setExtensionRendererMode(EXTENSION_RENDERER_MODE_ON)
            //.setExtensionRendererMode(EXTENSION_RENDERER_MODE_PREFER)
        ExoPlayer.Builder(context, drf).build().apply {
            addListener(listener)
            playWhenReady = false
//            playWhenReady = true
//            val dataSourceFactory: DataSource.Factory = DefaultDataSourceFactory(context,
//                Util.getUserAgent(context, context.packageName))
//
//            val mediaSourceFactory = DefaultMediaSourceFactory(context)
//                .setDataSourceFactory(cacheDataSourceFactory)
//                .setLocalAdInsertionComponents(
//                    adsLoaderProvider, /* adViewProvider= */ playerView);
//
//            val source = ProgressiveMediaSource.Factory(dataSourceFactory)
//                .createMediaSource(MediaItem.fromUri(path))
//            setMediaSource(source)

//            for (i in 1..10) {
//                addMediaItem(MediaItem.fromUri(path))
//            }

            //vratio = (videoSize.width / videoSize.height) as Float
//            setMediaItem(MediaItem.fromUri(path))
//            prepare()
        }
    }

    val pv = remember {
        StyledPlayerView(context).apply {
            player = exoPlayer
            useController = false
            //layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
            layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)

            // minimumHeight = 100
//            visibility = VISIBLE
//            visibility = INVISIBLE
//            setShowShuffleButton(false)
//            setShowRewindButton(false)
//            setShowNextButton(false)
//            setShowPreviousButton(false)
        }
    }

//    val pv = remember {
//        StyledPlayerControlView(context).apply {
//            player = exoPlayer
//        }
//    }
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> {
                    writeLog(context,"create $path")
                }
                Lifecycle.Event.ON_START -> {
                    writeLog(context,"start $path")
                }
                Lifecycle.Event.ON_STOP -> {
                    writeLog(context, "stop $path")
                }
                Lifecycle.Event.ON_DESTROY -> {
                    writeLog(context, "destroy $path")
                    exoPlayer.stop()
                    exoPlayer.removeListener(listener)
                    exoPlayer.release()
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)


        onDispose {
            writeLog(context, "on d $path")
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    // Gateway to traditional Android Views
    if (path != null) {
        // 仿真机模拟的摄像头，mp4 文件宽高信息颠倒，导致 exoplayer 的 StyledPlayerView 自动计算的比例是颠倒的。
        // StyledPlayerView 会自动设配视频去适应布局。
        //val ratio = 0.5625f
        // val ratio = 1.777f
        Column(
            modifier = modifier
//                .aspectRatio(vratio)
                //.aspectRatio(1.777f)
                //.aspectRatio(ratio)
                //.heightIn(100.dp)
                .fillMaxWidth(),
            //contentAlignment = Alignment.Center,
        ) {
            if (isPlaying) {
                val ratio = if (thumb != null) {
                    thumb.width.toFloat() / thumb.height.toFloat()
                } else {
                    1.0f
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(ratio),
                ) {
                    AndroidView(
                        { pv },
                        modifier = Modifier.fillMaxSize()
                            //.aspectRatio(1.777f)
//                            .aspectRatio(vratio)
//                        .aspectRatio(ratio)
                            //.fillMaxSize()
//                            .weight(1.0f)
                    )
                }
            }
            else {
                if (thumb != null) {
                    Image(
                        bitmap = thumb,
                        contentDescription = "图片",
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isPlaying) {
                    IconButton(onClick = {
                        exoPlayer.pause()
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_pause_circle_outline),
                            contentDescription = "暂停"
                        )
                    }
                } else {
                    IconButton(onClick = {
                        if (state == Player.STATE_ENDED || state == Player.STATE_IDLE) {
//                            val mi = MediaItem
//                                .Builder()
//                                .setUri(path)
//                                .setMimeType(MimeTypes.VIDEO_MP4)
//                                .build()
                            val mi = MediaItem.fromUri(path)
                            exoPlayer.clearMediaItems()
                            exoPlayer.setMediaItem(mi)
                            exoPlayer.prepare()
                        }

                        exoPlayer.play()
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_play_circle_outline),
                            contentDescription = "播放"
                        )
                    }
                }
            }
        }
    } else {
        Text("空路径")
    }
}

@Preview(showBackground = true)
@Composable
fun VideoPlayerPreview() {
    VideoPlayer("")
}