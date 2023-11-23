package com.example.jcm3ui.ui.widget

import android.net.Uri
import android.os.Build
import android.util.Size
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.media3.ui.PlayerView.ARTWORK_DISPLAY_MODE_FILL
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.decode.VideoFrameDecoder
import com.example.jcm3ui.ui.sdp

fun ExoPlayer.replay(uri: Uri) {
    clearMediaItems()
    addMediaItem(MediaItem.fromUri(uri))
    prepare()
    play()
}

@Composable
fun rememberExoPlayer(): ExoPlayer? {
    val context = LocalContext.current
    val inspectionMode = LocalInspectionMode.current
    val exoPlayer = remember(context) {
        if (inspectionMode) null else
            ExoPlayer.Builder(context).build()
    }

    DisposableEffect(exoPlayer) {
        onDispose {
            exoPlayer?.clearMediaItems()
        }
    }
    return exoPlayer
}


@OptIn(UnstableApi::class) @Composable
fun VideoExoPlayerViewer(
    uri: Uri,
    modifier: Modifier = Modifier,
    exoPlayer: ExoPlayer,
    onPlayStateChange: (Boolean) -> Unit = {},
) {
    val context = LocalContext.current

    var isPlay by remember () {
        mutableStateOf(false)
    }
    val listener = remember() {
        object: Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                super.onIsPlayingChanged(isPlaying)
                onPlayStateChange(isPlaying)
                isPlay = isPlaying
            }
        }
    }

    DisposableEffect(uri, exoPlayer, listener) {
        exoPlayer.apply {
            addListener(listener)
            clearMediaItems()
            addMediaItem(MediaItem.fromUri(uri))
            prepare()
            stop()
        }
        onDispose {
            isPlay = false
            exoPlayer.apply {
                removeListener(listener)
                stop()
                clearMediaItems()
            }
        }
    }

    val imageLoader by remember(context) {
        derivedStateOf {
            ImageLoader.Builder(context)
                .components {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        add(ImageDecoderDecoder.Factory())
                    } else {
                        add(GifDecoder.Factory())
                    }
                    add(VideoFrameDecoder.Factory())
                }.build()
        }
    }

    Box(modifier = modifier) {
        AndroidView(
            factory = {
                PlayerView(it).apply {
                    player = exoPlayer
                    layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
                    useController = false
                }
            },
            modifier = Modifier
                .fillMaxSize(),
        )

        if (!isPlay) {
            AsyncImage(
                model = uri,
                contentDescription = "小图",
                contentScale= ContentScale.Fit,
                alignment = Alignment.Center,
                imageLoader=imageLoader,
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxSize()
                    .background(Color.Black)
            )
        }
    }
}