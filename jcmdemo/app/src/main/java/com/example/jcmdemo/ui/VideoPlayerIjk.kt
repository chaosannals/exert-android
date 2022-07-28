package com.example.jcmdemo.ui

import android.view.SurfaceView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
//import tv.danmaku.ijk.media.player.*

@Composable
fun VideoPlayerIjk() {
    //val player = IjkMediaPlayer
    Column() {
        AndroidView(
            { context ->
                SurfaceView(context)
            },
            modifier = Modifier.fillMaxWidth()
        ) {

        }
    }
}

@Preview(showBackground = true)
@Composable
fun VideoPlayerIjkPreview() {
    VideoPlayerIjk()
}