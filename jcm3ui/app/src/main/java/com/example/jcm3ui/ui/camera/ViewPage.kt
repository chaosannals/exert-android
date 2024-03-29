package com.example.jcm3ui.ui.camera

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.zIndex
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.decode.VideoFrameDecoder
import com.example.jcm3ui.cameraActivityShotContentUriSubject
import com.example.jcm3ui.cameraActivityShotTypeSubject
import com.example.jcm3ui.cameraRouteTo
import com.example.jcm3ui.ui.page.demo.FileType
import com.example.jcm3ui.ui.page.demo.VideoInfo
import com.example.jcm3ui.ui.sdp
import com.example.jcm3ui.ui.ssp
import com.example.jcm3ui.ui.widget.VideoExoPlayerViewer
import com.example.jcm3ui.ui.widget.rememberExoPlayer
import com.example.jcm3ui.ui.widget.replay

data class FilePickInfo(
    val uri: String,
    val size: Int,
    val type: String,
    val ext: String,
    val duration: Float?=null,
)

private fun Context.getFileInfo(uri: Uri): FilePickInfo? {
    val projection = arrayOf(
        MediaStore.MediaColumns.SIZE,
        MediaStore.MediaColumns.DATA,
        MediaStore.MediaColumns.DURATION,
        MediaStore.MediaColumns.MIME_TYPE,
    )
    return contentResolver.query(uri, projection, null, null, null)?.use {
        val sizeIndex = it.getColumnIndexOrThrow(MediaStore.MediaColumns.SIZE)
        val dataIndex = it.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
        val durationIndex = it.getColumnIndexOrThrow(MediaStore.MediaColumns.DURATION)
        val mimeIndex = it.getColumnIndexOrThrow(MediaStore.MediaColumns.MIME_TYPE)
        it.moveToFirst()
        val data = it.getString(dataIndex)
        val mime = it.getString(mimeIndex)
        val size = it.getInt(sizeIndex)
        val duration = it.getInt(durationIndex)
        return FilePickInfo(
            uri = uri.toString(),
            size = size,
            type = if (mime.contains("image")) "image" else "video",
            ext = data.substringAfter(".", ""),
            duration = duration.div(1000f)
        )
    }
}

@Preview
@Composable
fun ViewPage() {
    val context = LocalContext.current
    val type by cameraActivityShotTypeSubject.collectAsState()
    val contentUri by cameraActivityShotContentUriSubject.collectAsState()

    val exoPlayer = rememberExoPlayer()

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

    var isPlay by remember() {
        mutableStateOf(false)
    }
    LaunchedEffect(contentUri) {
        isPlay = false
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            contentAlignment= Alignment.BottomStart,
            modifier = Modifier
                .zIndex(4f)
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .height(64.sdp)
                .background(Color(0xFF333333))
                .padding(start = 15.sdp, end = 15.sdp, bottom = 15.sdp)
        ) {
            Text(
                text = "重拍",
                color= Color.White,
                fontSize=14.ssp,
                modifier = Modifier
                    .clickable {
                        cameraRouteTo("[back]")
                    }
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            if (type == FileType.Image) {
                contentUri?.let {
                    AsyncImage(
                        model = it,
                        contentDescription = "缩略图",
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.Center,
                        imageLoader = imageLoader,
                        modifier = Modifier
                            .fillMaxSize()
                    )
                }
            } else {
                contentUri?.let {
                    exoPlayer?.apply {
                        VideoExoPlayerViewer(
                            uri=it,
                            exoPlayer = this,
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            isPlay = it
                        }
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .zIndex(4f)
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(103.sdp)
                .background(Color(0xE5333333))
                .padding(bottom = 13.sdp, start = 15.sdp, end = 15.sdp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .size(40.sdp)
                    .clip(RoundedCornerShape(2.sdp))
            ) {
                contentUri?.let {
                    AsyncImage(
                        model = it,
                        contentDescription = "缩略图",
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.Center,
                        imageLoader = imageLoader,
                        modifier = Modifier
                            .fillMaxSize()
                    )
                }
            }

            if (type == FileType.Video) {
                if (isPlay) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(60.sdp)
                            .background(Color.White, CircleShape)
                            .drawBehind {
                                drawRect(
                                    color = Color.Black,
                                    size = size / 2f,
                                    topLeft = Offset(size.width / 2f, size.height / 2f).div(2f)
                                )
                            }
                            .clickable {
                                exoPlayer?.pause()
                            }
                    ){}
                } else {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(60.sdp)
                            .background(Color.White, CircleShape)
                            .clickable {
                                contentUri?.let {
                                    exoPlayer?.apply {
                                        replay(it)
                                    }
                                }
                            }
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "播放",
                            modifier = Modifier
                                .size(34.sdp)
                        )
                    }
                }
            }

            Text(
                text = "确认",
                color= Color.White,
                fontSize=14.ssp,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .clickable {
                        (context as? Activity)?.apply {
                            val info = getFileInfo(contentUri!!)!!
                            val data = Intent().apply {
                                putExtra("uri", contentUri.toString())
                                putExtra("size", info.size)
                                putExtra("type", info.type)
                                putExtra("ext", info.ext)
                                putExtra("duration", info.duration)
                            }
                            setResult(Activity.RESULT_OK, data)
                            finish()
                        }
                    }
            )
        }
    }
}