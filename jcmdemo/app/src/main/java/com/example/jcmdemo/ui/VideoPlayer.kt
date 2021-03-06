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

fun writeLog(context: Context, text: String) {
    var f = File(context.getOutputDirectory(), "1a.log")
    f.appendText(text)
    f.appendText("\r\n")
}

fun newPlayer() {

}

@Composable
fun VideoPlayer(path: String, modifier: Modifier=Modifier) {
    // This is the official way to access current context from Composable functions
    val context = LocalContext.current

//    val mi = remember {
//        MediaItem.fromUri(path)
//    }

    writeLog(context, path)
    //writeLog(context, "22222")

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
            writeLog(context, "error ${error.errorCodeName}")
        }
    }

    val buildPlayer = {
        ExoPlayer.Builder(context).build().apply {
            addListener(listener)
        }
    }

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
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
            addListener(listener)
//            for (i in 1..10) {
//                addMediaItem(MediaItem.fromUri(path))
//            }
            //playWhenReady = true
            //vratio = (videoSize.width / videoSize.height) as Float
            //setMediaItem(MediaItem.fromUri(path))
            //prepare()
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
                Lifecycle.Event.ON_START -> {
                    writeLog(context,"start $path")
                }
                Lifecycle.Event.ON_STOP -> {
                    writeLog(context, "stop $path")
                }
                Lifecycle.Event.ON_DESTROY -> {
                    writeLog(context, "destroy $path")
                    exoPlayer.release()
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        // When the effect leaves the Composition, remove the observer
        onDispose {
            writeLog(context, "on d $path")
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    // Gateway to traditional Android Views
    if (path != null) {
        // ??????????????????????????????mp4 ????????????????????????????????? exoplayer ??? StyledPlayerView ????????????????????????????????????
        // StyledPlayerView ???????????????????????????????????????
        //val ratio = 0.5625f
        // val ratio = 1.777f

        Column(
            modifier = modifier
                .aspectRatio(vratio)
                //.aspectRatio(1.777f)
                //.aspectRatio(ratio)
                //.heightIn(100.dp)
                .fillMaxWidth(),
            //contentAlignment = Alignment.Center,
        ) {
            AndroidView(
                { pv },
                modifier = Modifier
                    //.aspectRatio(1.777f)
                    //.aspectRatio(ratio)
                    //.fillMaxSize()
                    .weight(1.0f)
            )
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isPlaying) {
                    IconButton(onClick = {
                        exoPlayer.pause()
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_pause_circle_outline),
                            contentDescription = "??????"
                        )
                    }
                } else {
                    IconButton(onClick = {
                        if (state == Player.STATE_ENDED || state == Player.STATE_IDLE) {
                            exoPlayer.setMediaItem(MediaItem.fromUri(path))
                            exoPlayer.prepare()
                        }
                        exoPlayer.play()
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_play_circle_outline),
                            contentDescription = "??????"
                        )
                    }
                }
            }
        }
    } else {
        Text("?????????")
    }
}

@Preview(showBackground = true)
@Composable
fun VideoPlayerPreview() {
    VideoPlayer("")
}