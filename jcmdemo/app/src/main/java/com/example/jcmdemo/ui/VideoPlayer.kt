package com.example.jcmdemo.ui

import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.exoplayer2.upstream.DataSource
//import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util

@Composable
fun VideoPlayer(path: String, modifier: Modifier=Modifier) {
    // This is the official way to access current context from Composable functions
    val context = LocalContext.current

    // Do not recreate the player everytime this Composable commits
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
            prepare()
        }
    }

//    val pv = remember {
//        StyledPlayerView(context).apply {
//            player = exoPlayer
//            //setShowShuffleButton(true)
//            //setShowRewindButton(true)
//            //setShowNextButton(true)
//            //setShowPreviousButton(true)
//        }
//    }

    // Gateway to traditional Android Views
    if (path != null) {
        AndroidView(
            { context ->
                StyledPlayerView(context).apply {
                    player = exoPlayer
                    //setShowShuffleButton(true)
                    //setShowRewindButton(true)
                    //setShowNextButton(true)
                    //setShowPreviousButton(true)
                }
            },
            modifier = modifier
        )
    } else {
        Text("空路径")
    }
}

@Preview(showBackground = true)
@Composable
fun VideoPlayerPreview() {
    VideoPlayer("")
}