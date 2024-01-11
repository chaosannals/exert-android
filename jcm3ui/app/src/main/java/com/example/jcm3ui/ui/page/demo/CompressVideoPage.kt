package com.example.jcm3ui.ui.page.demo

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaCodec
import android.media.MediaCodecInfo
import android.media.MediaExtractor
import android.media.MediaFormat
import android.media.MediaMetadataRetriever
import android.media.MediaMuxer
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.hw.videoprocessor.util.InputSurface
import com.hw.videoprocessor.util.OutputSurface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileDescriptor
import java.nio.ByteBuffer
import java.util.UUID
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

private enum class MediaTrackType(
    val mimeStart: String,
) {
    Audio("audio/"),
    Video("video/"),
}

// 获取 视频轨 和 音频轨 的索引
private fun MediaExtractor.getTrackFirstIndex(type: MediaTrackType): Int {
    for (i in 0 until  trackCount) {
        val format = getTrackFormat(i)
        val mime = format.getString(MediaFormat.KEY_MIME)
        if (mime?.startsWith(type.mimeStart) == true) {
            return i
        }
    }
    return -5
}

private fun MediaMuxer.addTrackFrom(extractor: MediaExtractor, type: MediaTrackType): Int {
    val trackId = extractor.getTrackFirstIndex(type)
    if (trackId < 0) {
        return -1
    }
    val trackFormat = extractor.getTrackFormat(trackId)
    extractor.selectTrack(trackId)
    return addTrack(trackFormat)
}

//
@SuppressLint("WrongConstant")
private fun MediaMuxer.copySample(extractor: MediaExtractor, index: Int) {
    val bufferInfo = MediaCodec.BufferInfo()
    val byteBuffer = ByteBuffer.allocate(1024 * 1024) // 这个太小的话 readSampleData 会失败
    while (true) {
        val dataCount = extractor.readSampleData(byteBuffer, 0)
        if (dataCount < 0) {
            break
        }
        bufferInfo.apply {
            size = dataCount
            offset = 0
            flags = extractor.sampleFlags
            // extractor.sampleTime 是个状态，每次读取都不同。
            presentationTimeUs = extractor.sampleTime
        }
        writeSampleData(index, byteBuffer, bufferInfo)
        extractor.advance()
    }
}

// 这些里面可能不同设备不一样，主要是 crop-* 和 width 和 height 这几个应该都有。可以作为重试格式的判断依据。
// 如果不同设备有问题，可能就是这里有些平台设备特殊的字段。
private val NEED_RECODE_INT_KEY = arrayOf(
    "max-bitrate",
    "crop-right",
    "profile",
    "bitrate",
    "priority",
    "intra-refresh-period",
    "color-standard",
    "feature-secure-playback",
    "color-transfer",
    "crop-bottom",
    "prepend-sps-pps-to-idr-frames",
    "video-qp-average",
    "crop-left",
    "width",
    "bitrate-mode",
    "color-range",
    "crop-top",
    "frame-rate",
    "height",
)

// mime 这个应该要一样，如果不一样，就是系统不支持，按理说 avc 应该都支持。
private val NEED_RECODE_STRING_KEY = arrayOf(
    "mime",
)

// changed format 就是 codec 设置了 csd-0 csd-1
private val NEED_KEY = arrayOf(
    "csd-1",
    "csd-0"
)
private fun needRecode(old: MediaFormat, changed: MediaFormat): Boolean {
    for (ik in NEED_RECODE_INT_KEY) {
        if (old.containsKey(ik).not()) {
            return true
        }
        if (old.getInteger(ik) != changed.getInteger(ik)) {
            return true
        }
    }
    return false
}

private fun MediaMuxer.recodeVideoTrack(
    extractor: MediaExtractor,
    trackIndex: Int,
    videoFormat: MediaFormat,
    inputFormat: MediaFormat,
//    retry: Boolean,
    durationUs: Long? = null
): MediaFormat? {
    val encoder = MediaCodec.createEncoderByType(MediaFormat.MIMETYPE_VIDEO_AVC).apply {
        // 从 安卓 12 开始 宽高低于 320x240 会报无效参数异常
        configure(videoFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE)
    }
    val inputSurface = InputSurface(encoder.createInputSurface()).apply {
        makeCurrent()
    }
    encoder.start()

    // 必须和 inputSurface 在同个协程初始化，不然无参构造函数会抛出异常。
    // 传递宽高参数，可以不用和 inputSurface 同个协程，但是未验证其可用性，几个参考代码都用的无参。
    val outputSurface = OutputSurface()
    val decoder = MediaCodec.createDecoderByType(inputFormat.getString(MediaFormat.KEY_MIME)!!).apply {
        configure(inputFormat, outputSurface.surface, null, 0)
    }

    decoder.start()
    try {
        val bufferInfo = MediaCodec.BufferInfo()
        val decodeInputDone = AtomicBoolean(false)
        val decodeOutputDone = AtomicBoolean(false)
        val encodeDone = AtomicBoolean(false)
        val encodeBufferInfo = MediaCodec.BufferInfo()
        val encodeTryAgainCount = AtomicInteger(100)

        // 在一个大循环里面做
        // 1.解码输入
        // 2.解码输出
        // 3.编码
        // 使得代码混乱是被逼无奈，
        // 多个变量初始化和之后的使用都必须在同一个协程。
        // 导致必须放在一个大循环里面。
        while (decodeInputDone.get().not() || decodeOutputDone.get().not() || encodeDone.get().not()) {
            // 1. 解码输入
            if (decodeInputDone.get().not()) {
                val inputBufferIndexOrStatus = decoder.dequeueInputBuffer(2500)
                if (inputBufferIndexOrStatus < 0) {
                    continue
                }
                val inputBuffer = decoder.getInputBuffer(inputBufferIndexOrStatus)
                // extractor 构建的协程必须和 readSampleData 的同一个协程，不然报未初始化状态异常
                val chunkSize = extractor.readSampleData(inputBuffer!!, 0)
                if (chunkSize < 0) {
                    decoder.queueInputBuffer(
                        inputBufferIndexOrStatus,
                        0,
                        0,
                        0L,
                        MediaCodec.BUFFER_FLAG_END_OF_STREAM
                    )
                    decodeInputDone.set(true)
                    Log.d("compress video", "decode input end: ${bufferInfo.presentationTimeUs}")
                    continue
                }

                decoder.queueInputBuffer(
                    inputBufferIndexOrStatus,
                    0,
                    chunkSize,
                    extractor.sampleTime,
                    0
                )
                extractor.advance()
                Log.d("compress video", "decode input: ${chunkSize}")
            }

            // 2. 解码输出
            if (decodeOutputDone.get().not()) {
                val outputBufferIndexOrStatus = decoder.dequeueOutputBuffer(bufferInfo, 2500)

                when (outputBufferIndexOrStatus) {
                    MediaCodec.INFO_TRY_AGAIN_LATER -> {
                        continue
                    }

                    MediaCodec.INFO_OUTPUT_FORMAT_CHANGED -> {
                        Log.d("compress video", "decode output format changed: ${decoder.outputFormat}")
                        continue
                    }

                    else -> {
                        // 截取视频
                        durationUs?.let {
                            if (bufferInfo.presentationTimeUs >= it) {
                                bufferInfo.flags = MediaCodec.BUFFER_FLAG_END_OF_STREAM
                            }
                        }
                        if (bufferInfo.flags == MediaCodec.BUFFER_FLAG_END_OF_STREAM) {
                            decoder.releaseOutputBuffer(outputBufferIndexOrStatus, false)
                            decodeOutputDone.set(true)
                            Log.d("compress video", "decode output end: ${bufferInfo.presentationTimeUs}")
                            continue
                        }
                        //
                        try {
                            decoder.releaseOutputBuffer(outputBufferIndexOrStatus, true)
                            // outputSurface 的初始化协程必须和 awaitNewImage 在同一个协程
                            outputSurface.awaitNewImage()
                            outputSurface.drawImage(false)
                            inputSurface.setPresentationTime(bufferInfo.presentationTimeUs * 1000)
                            inputSurface.swapBuffers()
                            Log.d("compress video", "decode output: ${bufferInfo.presentationTimeUs}")
                        } catch (t: Throwable) {
//                        decoder.releaseOutputBuffer(outputBufferIndexOrStatus, false)
                        }
                    }
                }
            }

            // 3. 编码
            if (encodeDone.get().not()) {
                val outputBufferIndexOrStatus = encoder.dequeueOutputBuffer(encodeBufferInfo, 2500)

                when (outputBufferIndexOrStatus) {
                    MediaCodec.INFO_TRY_AGAIN_LATER -> {
                        if (encodeTryAgainCount.getAndDecrement() < 0) {
                            encodeDone.set(true)
                            encoder.signalEndOfInputStream()
                        }
                        continue
                    }
                    MediaCodec.INFO_OUTPUT_FORMAT_CHANGED -> {
                        // 接收到这条信息的时候停止，拿到新的格式（encoder.outputFormat）返回上级，再重新执行一次。
                        Log.d("compress video", "encode output format changed: ${encoder.outputFormat}")
                        Log.d("compress video", "encode output format old: ${videoFormat}")
                        if (needRecode(videoFormat, encoder.outputFormat)) {
                            return encoder.outputFormat
                        }
                    }
                    else -> {
                        encoder.getOutputBuffer(outputBufferIndexOrStatus)?.let {outputBuffer ->
                            writeSampleData(trackIndex, outputBuffer, encodeBufferInfo)
                        }
                        encoder.releaseOutputBuffer(outputBufferIndexOrStatus, false)
                        Log.d("compress video", "encode flag: ${encodeBufferInfo.flags}")
                        if (encodeBufferInfo.flags == MediaCodec.BUFFER_FLAG_END_OF_STREAM) {
                            encodeDone.set(true)
                            encoder.signalEndOfInputStream()
                            break
                        }
                    }
                }
            }
        }
        return null
    } finally {
        decoder.stop()
        decoder.release()
        encoder.stop()
        encoder.release()
        inputSurface.release()
        outputSurface.release()
    }
}

private fun Context.recodeTracks(source: Uri, fd: FileDescriptor, w: Int, h: Int, recodeFormat: MediaFormat?): MediaFormat? {
    val muxer = MediaMuxer(fd, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4)
    // 视频轨
    val videoExtractor = MediaExtractor().apply {
        setDataSource(this@recodeTracks, source, mapOf())
    }
    val trackId = videoExtractor.getTrackFirstIndex(MediaTrackType.Video)
    val inputFormat = videoExtractor.getTrackFormat(trackId)
    videoExtractor.selectTrack(trackId)

    val fr = 30 // 30 帧
    val br = w * h * fr // 30 帧
    val videoFormat = (recodeFormat ?: MediaFormat.createVideoFormat(MediaFormat.MIMETYPE_VIDEO_AVC, w, h)).apply {
        setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface)
        setInteger(MediaFormat.KEY_BIT_RATE, br)
        setInteger(MediaFormat.KEY_FRAME_RATE, fr)
//        setInteger(MediaFormat.KEY_MAX_INPUT_SIZE, 0)
        setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 1) // 间隔单位秒
    }
    val videoTrack = muxer.addTrack(videoFormat)

    // 声轨
    val audioExtractor = MediaExtractor().apply {
        setDataSource(this@recodeTracks, source, mapOf())
    }
    val audioTrack = muxer.addTrackFrom(audioExtractor, MediaTrackType.Audio)
    try {
        muxer.start() // 必须在轨道啥都配置好才能开始。
        muxer.recodeVideoTrack(videoExtractor, videoTrack, videoFormat, inputFormat)?.let {
            return it
        }
        muxer.copySample(audioExtractor, audioTrack)

        return null
    } finally {
        audioExtractor.release()
        videoExtractor.release()
        muxer.release()
    }
}

private fun limitSize(width: Int, height: Int, sizeLimit: Int): Pair<Int, Int> {
    val ratio = width.toFloat() / height.toFloat()
    return if (width < sizeLimit && height < sizeLimit) { // 范围内
        Pair(width, height)
    } else if (ratio >= 1.0f) { // 宽比长 大 以宽为准
        Pair(sizeLimit, (sizeLimit / ratio).toInt())
    } else { // 长比宽大 以长为准
        Pair((sizeLimit * ratio).toInt(), sizeLimit)
    }
}

private fun Context.compressVideo(source: Uri, sizeLimit: Int) {
    val retriever = MediaMetadataRetriever()
    retriever.setDataSource(this, source)
    val width = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)!!.toInt()
    val height = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)!!.toInt()
    val rotation = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION)!!.toInt()

    // 在压缩尺寸内
    val (w , h) = if (rotation == 90 || rotation == 270)
        limitSize(height, width, sizeLimit)
    else limitSize(width, height, sizeLimit)

    val cachePath = "${UUID.randomUUID()}.mp4"
    File(externalCacheDir, cachePath).outputStream().use {
        var vf: MediaFormat? = null
        do {
            vf = recodeTracks(source, it.fd, w, h, vf)
        } while (vf != null)
    }
}

private val ioScope = CoroutineScope(Dispatchers.IO)

@Preview
@Composable
fun CompressVideoPage() {
    val context = LocalContext.current

    var hasPermissions by remember() {
        mutableStateOf(false)
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) {
        hasPermissions = it.map { it.value }.reduce { a, b -> a && b }
    }
    LaunchedEffect(Unit) {
        permissionLauncher.launch(
            mutableListOf(
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO,
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()
        )
    }

    val videoCompressLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ){
        it?.let {
            Toast.makeText(context, "start", Toast.LENGTH_SHORT).show()
            ioScope.launch {
                context.compressVideo(it, 1000)
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "end", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Button(
            enabled = hasPermissions,
            onClick = {
                videoCompressLauncher.launch("video/mp4")
            }
        ) {
            Text(text = "压缩")
        }
    }
}