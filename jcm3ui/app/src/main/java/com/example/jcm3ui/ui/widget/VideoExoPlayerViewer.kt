package com.example.jcm3ui.ui.widget

import android.net.Uri
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

@Composable
fun VideoExoPlayerViewer(
    uri: Uri,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val exoPlayer = remember(context) {
        ExoPlayer.Builder(context).build()
    }
    val view = remember(exoPlayer) {
        PlayerView(context).apply {
            player = exoPlayer
        }
    }

    DisposableEffect(exoPlayer) {
        onDispose {
            exoPlayer.clearMediaItems()
        }
    }

    AndroidView(
        factory = {
            view.apply {
                layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
            }
        },
        modifier = modifier,
    ) {
        exoPlayer.clearMediaItems()
        exoPlayer.addMediaItem(MediaItem.fromUri(uri))
        exoPlayer.prepare()
        exoPlayer.stop()
    }
}