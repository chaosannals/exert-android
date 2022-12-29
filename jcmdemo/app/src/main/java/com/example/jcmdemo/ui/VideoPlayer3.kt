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
import com.google.android.exoplayer2.*
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

fun writeLog3(context: Context, text: String) {
    var f = File(context.getOutputDirectory(), "13a.log")
    f.appendText(text)
    f.appendText("\r\n")
}

@Composable
fun VideoPlayer3(path: String, modifier: Modifier=Modifier) {
    val context = LocalContext.current
    writeLog3(context, path)

    var isPlaying by remember { mutableStateOf(false) }
    var state by remember { mutableStateOf(Player.STATE_IDLE) }
    var vratio by remember { mutableStateOf(1.0f) }
    val listener = object : Player.Listener {
        override fun onIsPlayingChanged(isplaying: Boolean) {
            isPlaying = isplaying
        }

        override fun onPlaybackStateChanged(playbackState: Int) {
            state = playbackState
        }

        override fun onPlayerError(error: PlaybackException) {
            writeLog3(context, "error $path  ${error.errorCodeName} => ${error.stackTrace}")
        }
    }

    val exoPlayer = remember {
        var drf = DefaultRenderersFactory(context)
            .setExtensionRendererMode(DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER)
        ExoPlayer.Builder(context, drf).build().apply {
            addListener(listener)
            setMediaItem(MediaItem.fromUri(path))
            prepare()
        }
    }

    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                    writeLog3(context,"start $path")
                }
                Lifecycle.Event.ON_STOP -> {
                    writeLog3(context, "stop $path")
                }
                Lifecycle.Event.ON_DESTROY -> {
                    writeLog3(context, "destroy $path")
                    exoPlayer.release()
                }
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            writeLog3(context, "on d $path")
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    if (path != null) {
        Column(
            modifier = modifier
                .aspectRatio(vratio)
                .fillMaxWidth(),
        ) {
            AndroidView(
                {
                    StyledPlayerView(context).apply {
                        player = exoPlayer
                        useController = false
                        layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
                    }
                },
                modifier = Modifier
                    .weight(1.0f)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
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
fun VideoPlayer3Preview() {
    VideoPlayer3("")
}