package com.example.appshell.ui.page.demo

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import com.example.appshell.ui.widget.DesignPreview
import com.example.appshell.ui.widget.VideoBox
import com.example.appshell.ui.widget.VideoPicker
import com.example.appshell.ui.widget.VideoPickerItem
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@Composable
fun VideoPickPage() {
    var isShowPicker by remember {
        mutableStateOf(false)
    }

    val videos = remember {
        mutableStateListOf<VideoPickerItem>()
    }

    Box(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding()
            .fillMaxSize()
    ) {
        Column(
            verticalArrangement= Arrangement.Top,
            horizontalAlignment= Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Button(
                onClick = { isShowPicker = true },
            ) {
                Text(
                    text = "浏览",
                )
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                itemsIndexed(videos) {i, video ->
                    VideoBox(videoUrl =video.uri)
//                    if (video.thumb != null) {
//                        Image(
//                            bitmap = video.thumb,
//                            contentDescription = "图片 $i",
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .aspectRatio(1f)
//                        )
//                    } else {
//                        Box(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .aspectRatio(1f)
//                        ) {
//                            Text(
//                                text = "视频 $i",
//                                modifier = Modifier
//                                    .align(Alignment.Center)
//                            )
//                        }
//                    }
                }
            }
        }
        VideoPicker(
            visible = isShowPicker,
            pickCount = 9,
            videoMaxDuration = 30.toDuration(DurationUnit.SECONDS),
            modifier = Modifier
                .zIndex(100f),
            onConfirm = { yes, items ->
                isShowPicker = false
                if (yes) {
                    videos.clear()
                    videos.addAll(items)
                }
            }
        )
    }
}

@Preview
@Composable
fun VideoPickPagePreview() {
    DesignPreview {
        VideoPickPage()
    }
}