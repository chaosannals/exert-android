package com.example.jcmdemo.ui

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
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.StyledPlayerControlView
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.exoplayer2.ui.SubtitleView
import com.google.android.exoplayer2.upstream.DataSource
//import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util

@Composable
fun VideoPlayer(path: String, modifier: Modifier=Modifier) {
    // This is the official way to access current context from Composable functions
    val context = LocalContext.current

//    val mi = remember {
//        MediaItem.fromUri(path)
//    }

    var isPlaying by remember { mutableStateOf(false) }
    var state by remember { mutableStateOf(Player.STATE_IDLE) }
    val listener = object : Player.Listener {
        override fun onIsPlayingChanged(isplaying: Boolean) {
            isPlaying = isplaying
        }

        override fun onPlaybackStateChanged(playbackState: Int) {
            state = playbackState
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
            setMediaItem(MediaItem.fromUri(path))
//            for (i in 1..10) {
//                addMediaItem(MediaItem.fromUri(path))
//            }
            //playWhenReady = true
            addListener(listener)
            prepare()
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
                Lifecycle.Event.ON_START -> Unit
                Lifecycle.Event.ON_STOP -> Unit
                Lifecycle.Event.ON_DESTROY -> exoPlayer.release()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        // When the effect leaves the Composition, remove the observer
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    // Gateway to traditional Android Views
    if (path != null) {
        // 仿真机模拟的摄像头，mp4 文件宽高信息颠倒，导致 exoplayer 自动计算的比例是颠倒的。
        //val ratio = 0.5625f
        // val ratio = 1.777f
        Column(
            modifier = modifier
                .aspectRatio(1.0f)
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
                            contentDescription = "暂停"
                        )
                    }
                } else {
                    IconButton(onClick = {
                        if (state == Player.STATE_ENDED) {
                            exoPlayer.setMediaItem(MediaItem.fromUri(path))
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