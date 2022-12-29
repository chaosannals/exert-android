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
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.StyledPlayerControlView
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.exoplayer2.ui.SubtitleView
import com.google.android.exoplayer2.upstream.DataSource
//import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import java.io.File

fun writeLog2(context: Context, text: String) {
    var f = File(context.getOutputDirectory(), "12a.log")
    f.appendText(text)
    f.appendText("\r\n")
}

@Composable
fun VideoPlayer2(path: String, modifier: Modifier=Modifier) {
    val context = LocalContext.current
    var isPlaying by remember { mutableStateOf(false) }
    var state by remember { mutableStateOf(Player.STATE_IDLE) }

    writeLog2(context, path)

    val listener = object : Player.Listener {
        override fun onIsPlayingChanged(isplaying: Boolean) {
            isPlaying = isplaying
        }

        override fun onPlaybackStateChanged(playbackState: Int) {
            state = playbackState
        }

        override fun onPlayerError(error: PlaybackException) {
            writeLog2(context, "error ${error.errorCodeName}")
        }
    }

    var exoPlayer: ExoPlayer? by remember { mutableStateOf(null) }
    val pv = StyledPlayerView(context).apply {
        // player = exoPlayer
        useController = false
        layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
    }

    val initPlayer = {
        writeLog(context, "init play $path")
        if (exoPlayer != null) {
            exoPlayer?.stop()
            exoPlayer?.release()
        }
        exoPlayer = ExoPlayer.Builder(context).build().apply {
            addListener(listener)
            setMediaItem(MediaItem.fromUri(path))
            prepare()
        }
        pv.player = exoPlayer
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                    writeLog2(context,"start $path")
                }
                Lifecycle.Event.ON_STOP -> {
                    writeLog2(context, "stop $path")
                }
                Lifecycle.Event.ON_DESTROY -> {
                    writeLog2(context, "destroy $path")
                    exoPlayer?.release()
                }
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            writeLog2(context, "on d $path")
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    if (path != null) {
        Column(
            modifier = modifier
                .aspectRatio(1.0f)
                .fillMaxWidth(),
        ) {
            AndroidView(
                { pv },
                modifier = Modifier
                    .weight(1.0f)
            )
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isPlaying) {
                    IconButton(onClick = {
                        exoPlayer?.pause()
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_pause_circle_outline),
                            contentDescription = "暂停"
                        )
                    }
                } else {
                    IconButton(onClick = {
                        if (state == Player.STATE_ENDED || state == Player.STATE_IDLE) {
                            initPlayer()
                        }
                        exoPlayer?.play()
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
fun VideoPlayer2Preview() {
    VideoPlayer2("")
}