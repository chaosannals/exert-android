package com.example.jcm3ui.ui.page.demo

import android.media.MediaCodec
import android.media.MediaCodecInfo
import android.media.MediaFormat
import androidx.compose.runtime.Composable

private fun make(w: Int=400, h: Int=400) {
    val encoder = MediaCodec.createEncoderByType(MediaFormat.MIMETYPE_VIDEO_AVC)
    val format = MediaFormat.createVideoFormat(
        MediaFormat.MIMETYPE_VIDEO_AVC,
        w,
        h
    ).apply {
        val fr = 30 // 30 帧
        val br = w * h * fr // 30 帧
        setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface)
        setInteger(MediaFormat.KEY_BIT_RATE, br)
        setInteger(MediaFormat.KEY_FRAME_RATE, fr)
    }
    encoder.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE)
    val inputSurface = encoder.createInputSurface()
    encoder.start()

    while (true) {

    }
}

@Composable
fun ImageToMp4Page() {

}