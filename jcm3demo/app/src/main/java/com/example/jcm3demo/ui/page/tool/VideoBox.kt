package com.example.jcm3demo.ui.page.tool

import android.content.Context
import android.media.MediaMetadataRetriever
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import com.example.jcm3demo.R
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.StyledPlayerView
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

fun writeLog(context: Context, text: String) {
    val f = File(context.getOutputDirectory(), "1.log")
    val d = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(Date())
    f.appendText("[$d] ")
    f.appendText(text)
    f.appendText("\r\n")
}

@Composable
fun VideoBox(videoPath: String) {
    val context = LocalContext.current
    val shape = RoundedCornerShape(10.dp)
    var isPlaying by remember { mutableStateOf(false) }
    var isPause = false
    val thumb = remember {
        val mmr = MediaMetadataRetriever()
        mmr.setDataSource(videoPath)
        mmr.getFrameAtTime(1)?.asImageBitmap()
    }

    val listener = object : VideoPlayerListener {
        override fun getId(): String {
            return videoPath
        }
        override fun onRenderedFirstFrame() {
            writeLog(context, "render first $videoPath")
        }
        override fun onIsPlayingChanged(isplaying: Boolean) {
            writeLog(context, "isplayc: ($isplaying) $videoPath")
            if (!isPause) {
                isPlaying = isplaying
            }
        }

        override fun onBeReplaced() {
            writeLog(context, "be replace: $videoPath")
            isPause = false
            isPlaying = false
        }

        override fun onPlayerError(error: PlaybackException) {
            writeLog(context, "error $videoPath  ${error.errorCodeName} => ${error.stackTraceToString()}")
        }
    }

    Column(
        modifier = Modifier
            .padding(10.dp)
            .clip(shape)
            .border(BorderStroke(1.dp, colorResource(id = R.color.gray_2)), shape)
            .padding(10.dp),
    ) {
        Text(videoPath, fontSize = 10.sp)

        val ratio = if (thumb != null) {
            thumb.width.toFloat() / thumb.height.toFloat()
        } else {
            1.0f
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(ratio),
            contentAlignment = Alignment.Center,
        ) {
            if (isPlaying) {
                AndroidView(
                    {
                        writeLog(it, "android view:  isplay: $isPlaying, isPause: $isPause")
                        StyledPlayerView(it)
                    },
                    modifier = Modifier
                        .zIndex(1.0f)
                        .fillMaxSize(),
                ) {
                    var ep = VideoPlayer.ensure(context, listener)
                    it.player = ep
                    it.useController = false
                    it.layoutParams = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    ep.playFromUri(videoPath)
                }

                VideoPauseBox(
                    onClick = { ispause ->
                        isPause = ispause
                        if (ispause) {
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
                    modifier = Modifier
                        .fillMaxSize()
                        .zIndex(1.0f),
                )
                IconButton(
                    modifier = Modifier.zIndex(2.0f),
                    onClick = {
                        isPlaying = true
                    },
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_play_circle_outline),
                        contentDescription = "播放",
                        tint = colorResource(id = R.color.white),
                        modifier = Modifier.fillMaxSize(0.6f),
                    )
                }
            } else {
                Icon(
                    painterResource(id = R.drawable.ic_error_outline),
                    contentDescription="加载失败",
                    modifier = Modifier.size(300.dp),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun VideoBoxPreview() {
    VideoBox("")
}