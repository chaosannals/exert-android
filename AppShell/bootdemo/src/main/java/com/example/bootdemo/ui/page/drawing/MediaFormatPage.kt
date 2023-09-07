package com.example.bootdemo.ui.page.drawing

import android.media.MediaCodecList
import android.media.MediaExtractor
import android.media.MediaFormat
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun MediaFormatPage() {
    val getFormat by rememberUpdatedState { path: String, isVideo: Boolean ->
        val mediaExtractor = MediaExtractor().apply {
            setDataSource(path)
        }
        val trackCount = mediaExtractor.trackCount
        val mimeHead = if (isVideo) "video/" else "audio/"
        for (i in 0 until trackCount) {
            val trackFormat = mediaExtractor.getTrackFormat(i)
            if (trackFormat.getString(MediaFormat.KEY_MIME)?.startsWith(mimeHead) == true) {
                return@rememberUpdatedState mediaExtractor.getTrackFormat(i)
            }
        }
        return@rememberUpdatedState null
    }

    val supportInfos = remember {
        mutableStateListOf<String>()
    }

    val getSupportFormat by rememberUpdatedState {
        val mcl = MediaCodecList(MediaCodecList.ALL_CODECS)
        mcl.codecInfos.forEach { mci ->
            supportInfos.add("${mci.name} support: ")
            mci.supportedTypes.forEach { t ->
                supportInfos.add(t)
            }
        }
    }

    LaunchedEffect(Unit) {
        getSupportFormat()
    }

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxSize()
    ) {

    }
}

@Preview
@Composable
fun MediaFormatPagePreview() {
    MediaFormatPage()
}