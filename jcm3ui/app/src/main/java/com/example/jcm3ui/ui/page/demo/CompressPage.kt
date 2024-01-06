package com.example.jcm3ui.ui.page.demo

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaCodec
import android.media.MediaCodecInfo
import android.media.MediaCodecList
import android.media.MediaExtractor
import android.media.MediaFormat
import android.media.MediaMetadataRetriever
import android.media.MediaMuxer
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
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
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import com.hw.videoprocessor.VideoProcessor
import com.hw.videoprocessor.util.InputSurface
import com.hw.videoprocessor.util.OutputSurface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.nio.ByteBuffer
import java.util.UUID

private const val MIME_TYPE = "video/avc"

enum class MediaTrackType(
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

// mime 转 MediaCodecInfo
private fun getMediaCodecInfo(mimeType: String=MIME_TYPE): MediaCodecInfo? {
    val mcl = MediaCodecList(MediaCodecList.ALL_CODECS)
    var result: MediaCodecInfo? = null
    for (mci in mcl.codecInfos) {
        if (!mci.isEncoder) {
            continue
        }
        for (type in mci.supportedTypes) {
            if (type.equals(mimeType, true)) {
                result = mci // 如果不是下面这2种，这个备用。
                if (mci.name.equals("OMX.SEC.avc.enc")) {
                    return mci
                }
                if (mci.name.equals("OMX.SEC.AVC.Encoder")) {
                    return mci
                }
            }
        }
    }
    return result
}

//private val RECOGNIIZED_FORMAT = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//    setOf(
//        MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420Planar,
//        MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420PackedPlanar,
//        MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420SemiPlanar,
//        MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420PackedSemiPlanar,
//        MediaCodecInfo.CodecCapabilities.COLOR_TI_FormatYUV420PackedSemiPlanar,
//    )
//} else {
//    setOf(
//        MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420Flexible,
//    )
//}

// 目标颜色
private fun isRecognizedFormat(colorFormat: Int): Boolean {
//    return RECOGNIIZED_FORMAT.contains(colorFormat)
    return colorFormat == MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420Flexible
}

// 颜色类型
private fun MediaCodecInfo.getColorFormat(mimeType: String= MIME_TYPE): Int {
    val capabilities = getCapabilitiesForType(mimeType)
    var result = 0
    for (f in capabilities.colorFormats) {
        if (isRecognizedFormat(f)) {
            result = f // 如果不是以下的备用

            // 此项优先
            if (name.equals("OMX.SEC.AVC.Encoder") && f == 19) {
                return f
            }
        }
    }
    return result
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

// 只把 视频的 声轨 和 视频轨 拷贝到新文件
// 如果只有这2轨道，等于复制
private fun Context.copyVideoAndAudio(source: Uri) {
    val cachePath = "${UUID.randomUUID()}.mp4"
    File(externalCacheDir, cachePath).outputStream().use {
        val mediaMuxer = MediaMuxer(it.fd, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4)

        // 视频轨
        val videoExtractor = MediaExtractor().apply {
            setDataSource(this@copyVideoAndAudio, source, mapOf())
        }
        val videoTrack = mediaMuxer.addTrackFrom(videoExtractor, MediaTrackType.Video)

        // 声轨
        val audioExtractor = MediaExtractor().apply {
            setDataSource(this@copyVideoAndAudio, source, mapOf())
        }
        val audioTrack = mediaMuxer.addTrackFrom(audioExtractor, MediaTrackType.Audio)
        mediaMuxer.start() // 必须在轨道啥都配置好才能开始。

        mediaMuxer.copySample(videoExtractor, videoTrack)
        mediaMuxer.copySample(audioExtractor, audioTrack)

        audioExtractor.release()
        videoExtractor.release()
        mediaMuxer.stop()
        mediaMuxer.release()
    }
}

private fun MediaMuxer.addTrackFormat(extractor: MediaExtractor, type: MediaTrackType, format: MediaFormat): Int {
    val trackId = extractor.getTrackFirstIndex(type)
    if (trackId < 0) {
        return -1
    }
    val trackFormat = extractor.getTrackFormat(trackId).apply {
        setInteger(MediaFormat.KEY_WIDTH, 400)
        setInteger(MediaFormat.KEY_HEIGHT, 225)
        setInteger(MediaFormat.KEY_BIT_RATE, 30* 400* 225)
        setInteger(MediaFormat.KEY_FRAME_RATE, 30)
    }
    extractor.selectTrack(trackId)
    //Log.d("format", trackFormat.toString())
    return addTrack(trackFormat)
//    return addTrack(format)
}

@SuppressLint("WrongConstant")
private fun MediaMuxer.resample(extractor: MediaExtractor, index: Int) {
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
//            flags = MediaCodec.BUFFER_FLAG_KEY_FRAME
            // extractor.sampleTime 是个状态，每次读取都不同。
            presentationTimeUs = extractor.sampleTime
        }
        writeSampleData(index, byteBuffer, bufferInfo)
        extractor.advance()
    }
}

private fun MediaMuxer.codeVideo(extractor: MediaExtractor, w: Int, h: Int) {
    val trackId = extractor.getTrackFirstIndex(MediaTrackType.Video)
    val inputFormat = extractor.getTrackFormat(trackId)

    val fr = 30 // 30 帧
    val br = w * h * fr // 30 帧
    val videoFormat = MediaFormat.createVideoFormat(MediaFormat.MIMETYPE_VIDEO_AVC, w, h).apply {
        setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface)
        setInteger(MediaFormat.KEY_BIT_RATE, br)
        setInteger(MediaFormat.KEY_FRAME_RATE, fr)
//            setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 1) // 间隔单位秒
    }
    val encoder = MediaCodec.createEncoderByType(MediaFormat.MIMETYPE_VIDEO_AVC).apply {
        configure(videoFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE)
    }
    val trackIndex = addTrack(videoFormat)
    val inputSurface = InputSurface(encoder.createInputSurface()).apply {
        makeCurrent()
    }

    val outputSurface = OutputSurface()
    val decoder = MediaCodec.createDecoderByType(inputFormat.getString(MediaFormat.KEY_MIME)!!).apply {
        configure(inputFormat, outputSurface.surface, null, 0)
    }

    ioScope.launch {
        decoder.start()
        val inputBufferIndex = decoder.dequeueInputBuffer(2500)
        val inputBuffer = decoder.getInputBuffer(inputBufferIndex)
        while (true) {
            val chunkSize = extractor.readSampleData(inputBuffer!!, 0)
            if (chunkSize < 0) {
                decoder.queueInputBuffer(
                    inputBufferIndex,
                    0,
                    0,
                    0L,
                    MediaCodec.BUFFER_FLAG_END_OF_STREAM
                )
                break
            }
            decoder.queueInputBuffer(inputBufferIndex, 0, chunkSize, extractor.sampleTime, 0)
            extractor.advance()
        }
    }

    ioScope.launch {
        encoder.start()
        val bufferInfo = MediaCodec.BufferInfo()
        val outputBufferIndex = encoder.dequeueOutputBuffer(bufferInfo, 2500)
        val outputBuffer = encoder.getOutputBuffer(outputBufferIndex)
    }
}

// 压缩视频
private fun Context.compressVideo(
    source: Uri,
    sizeLimit: Int,
) {
    val retriever = MediaMetadataRetriever()
    retriever.setDataSource(this, source)
    val width = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)!!.toInt()
    val height = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)!!.toInt()
    val rotation = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION)!!.toInt()
    val duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)!!.toLong()

    val ratio = width.toFloat() / height.toFloat()

    // 在压缩尺寸内
    val (w , h) = if (width < sizeLimit && height < sizeLimit) { // 范围内
        Pair(width, height)
    } else if (ratio >= 1.0f) { // 宽比长 大 以宽为准
        Pair(sizeLimit, (sizeLimit / ratio).toInt())
    } else { // 长比宽大 以长为准
        Pair((sizeLimit * ratio).toInt(), sizeLimit)
    }
    val fr = 30 // 30 帧
    val br = w * h * fr // 30 帧

    val cachePath = "${UUID.randomUUID()}.mp4"
    File(externalCacheDir, cachePath).outputStream().use {
        val mediaMuxer = MediaMuxer(it.fd, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4)

        // 视频轨
        val videoExtractor = MediaExtractor().apply {
            setDataSource(this@compressVideo, source, mapOf())
        }
        // 重新定义视频轨的格式，压缩就是使用低配置再录制这轨
        val videoFormat = MediaFormat.createVideoFormat(MediaFormat.MIMETYPE_VIDEO_AVC, w, h).apply {
            setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface)
            setInteger(MediaFormat.KEY_BIT_RATE, br)
            setInteger(MediaFormat.KEY_FRAME_RATE, fr)
//            setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 1) // 间隔单位秒
        }
//        val videoTrack = mediaMuxer.addTrackFormat(videoExtractor, MediaTrackType.Video, videoFormat)

        // 声轨
        val audioExtractor = MediaExtractor().apply {
            setDataSource(this@compressVideo, source, mapOf())
        }
        val audioTrack = mediaMuxer.addTrackFrom(audioExtractor, MediaTrackType.Audio)
        mediaMuxer.start() // 必须在轨道啥都配置好才能开始。

//        mediaMuxer.copySample(videoExtractor, videoTrack)
        mediaMuxer.copySample(audioExtractor, audioTrack)

        mediaMuxer.stop()
        mediaMuxer.release()
        audioExtractor.release()
        videoExtractor.release()
    }
}




// 这种方式好像已经很少被使用了。
private fun Context.makeCache(name: String): Uri {
    val dir = File(cacheDir, "caches") // caches 是 file_paths.xml 里 path 值
    val temp = File(dir, name)
    return FileProvider.getUriForFile(this, "com.example.jcm3ui.fileprovider", temp)
}

private fun Context.makePhoto(name: String): Uri? {
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, name)
        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
        }
    }
    val contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    return contentResolver.insert(contentUri, contentValues)
}

private fun Context.makeVideo(name: String): Uri? {
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, name)
        put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4")
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            put(MediaStore.Video.Media.RELATIVE_PATH, "Movies/CameraX-Video")
        }
    }
    val contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
    return contentResolver.insert(contentUri, contentValues)
}

private fun Context.makeVideoPath(name: String): String? {
    return makeVideo(name)?.let {
        val projection = arrayOf(MediaStore.Video.Media.DATA)
        contentResolver.query(it, projection, null, null, null)?.use {
            val dataIndex = it.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
            it.moveToFirst()
            getString(dataIndex)
        }
    }
}

data class VideoInfo(
    val id: Long,
    val size: Long,
    val width: Long,
    val height: Long,
    val durationMs: Long,
    val bitrate: Long,
    val name: String?, // 和 Java 交互时，Java 可能返回空，所以和 java 对接的 object 类型都应该声明可空
    val path: String?, // 和 Java 交互时，Java 可能返回空，所以和 java 对接的 object 类型都应该声明可空
    val realPath: String?=null, // 和 Java 交互时，Java 可能返回空，所以和 java 对接的 object 类型都应该声明可空
)

// 这个依赖 系统数据库，没有存的信息都是 0 或 空
// size width height duration 不能稳定获得
private fun Context.queryVideoInfo(uri: Uri): VideoInfo? {
    val projection = arrayOf(
        MediaStore.Video.Media._ID,
        MediaStore.Video.Media.DISPLAY_NAME,
        MediaStore.Video.Media.RELATIVE_PATH,
        MediaStore.Video.Media.DURATION,
        MediaStore.Video.Media.SIZE,
        MediaStore.Video.Media.WIDTH,
        MediaStore.Video.Media.HEIGHT,
        MediaStore.Video.Media.DATA,
    )
    return contentResolver.query(uri, projection, null, null, null)?.run {
        val idIndex = getColumnIndexOrThrow(MediaStore.Video.Media._ID)
        val nameIndex = getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
        val pathIndex = getColumnIndexOrThrow(MediaStore.Video.Media.RELATIVE_PATH)
        val durationIndex = getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)
        val sizeIndex = getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)
        val widthIndex = getColumnIndexOrThrow(MediaStore.Video.Media.WIDTH)
        val heightIndex = getColumnIndexOrThrow(MediaStore.Video.Media.HEIGHT)
        val dataIndex = getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
        moveToFirst()
        val id = getLong(idIndex)
        val name = getString(nameIndex)
        val path = getString(pathIndex)
        val durationMs = getLong(durationIndex)
        val size = getLong(sizeIndex)
        val width = getLong(widthIndex)
        val height = getLong(heightIndex)
        val realPath = getString(dataIndex)
        return VideoInfo(
            id = id,
            name = name,
            path = path,
            durationMs = durationMs,
            size = size,
            width = width,
            height = height,
            realPath = realPath,
            bitrate = 0,
        )
    }
}

// 通过视频解码器获取信息
private fun Context.getVideoInfo(uri: Uri): VideoInfo? {
    val info = queryVideoInfo(uri)
    val retriever = MediaMetadataRetriever()
    retriever.setDataSource(this, uri)
    val width = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)?.toLong() ?: 0
    val height = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)?.toLong() ?: 0
    val duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLong() ?: 0
    val bitrate = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE)?.toLong() ?: 0
    retriever.release()
    return info?.copy(
        width = width,
        height = height,
        durationMs = duration,
        bitrate = bitrate,
    )
}

// 获取图像大小
private fun Context.getPhotoSize(uri: Uri): Pair<Int, Int> {
//    val options = BitmapFactory.Options().apply {
//        inJustDecodeBounds = true
//    }
    val stream = contentResolver.openInputStream(uri)
//    val bitmap = BitmapFactory.decodeStream(stream, null, options) // 中间的参数不知道传啥，null 会导致失败。
    val bitmap = BitmapFactory.decodeStream(stream)
    return Pair(bitmap?.width ?: 0, bitmap?.height ?: 0)
}

private val ioScope = CoroutineScope(Dispatchers.IO)

@Preview
@Composable
fun CompressPage() {
    val context = LocalContext.current

    var hasPermissions by remember() {
        mutableStateOf(false)
    }

    var photoUri by remember(context) {
        mutableStateOf(context.makePhoto(UUID.randomUUID().toString()))
    }

    var videoUri by remember(context) {
        mutableStateOf(context.makeVideo(UUID.randomUUID().toString()))
    }

    var bitmap by remember {
        mutableStateOf<Bitmap?>(null)
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) {
        hasPermissions = it.map { it.value }.reduce { a, b -> a && b }
    }

    val videoPickLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ){
        val inputInfo = context.getVideoInfo(it!!)
        Toast.makeText(context, "GetContent video: ${inputInfo?.width} x ${inputInfo?.height}", Toast.LENGTH_SHORT).show()
    }

    val videoCaptureLauncher = rememberLauncherForActivityResult(
        contract =  ActivityResultContracts.CaptureVideo()
    ) {
        Toast.makeText(context, "CaptureVideo: ${it}", Toast.LENGTH_SHORT).show()
        if (it && videoUri != null) {
            val inputInfo = context.getVideoInfo(videoUri!!)
            Toast.makeText(context, "CaptureVideo: ${inputInfo?.width} x ${inputInfo?.height}", Toast.LENGTH_SHORT).show()
            val output = context.makeVideoPath(UUID.randomUUID().toString())
//            File(output).apply {
//                createNewFile()
//                deleteOnExit()
//            }
//            Toast.makeText(context, "CaptureVideo: ${output}", Toast.LENGTH_SHORT).show()
            VideoProcessor.processor(context)
                .input(videoUri)
                .output(output)
                .outWidth(400)
                .outHeight(400)
                .process()
        }
    }

    val photoPickLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) {

    }

    val photoTakeLauncher = rememberLauncherForActivityResult(
        contract =  ActivityResultContracts.TakePicture()
    ) {
        if (it) {
            val (w, h) = context.getPhotoSize(photoUri!!)
            Toast.makeText(context, "TakePicture: ${w} x ${h}", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "TakePicture: ${it}", Toast.LENGTH_SHORT).show()
        }
    }

    val photoPreviewPickLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview() // 这个图片太小。
    ) {
        Toast.makeText(context, "TakePicturePreview: ${it?.width} x ${it?.height}", Toast.LENGTH_SHORT).show()
        bitmap = it
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Button(
            enabled = hasPermissions,
            onClick = {
//                val uri = context.makeCache("${UUID.randomUUID()}.mp4") // 这个是可以的
//                val uri = context.makeVideo(UUID.randomUUID().toString()) // 官方比较推荐这种方式
//                videoProcessorPickLauncher.launch(uri)
                videoCaptureLauncher.launch(videoUri)
            }
        ) {
            Text(
                text = "拍视频"
            )
        }

        Button(
            enabled = hasPermissions,
            onClick = {
                videoPickLauncher.launch("video/mp4")
            }
        ) {
            Text(
                text = "选择视频"
            )
        }

        Button(
            enabled = hasPermissions,
            onClick = {
//                val uri = context.makeCache("${UUID.randomUUID()}.jpg") // 高版本安卓结果一直是失败
//                val uri = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY) // 结果一直是失败, 这个权限很高，是个目录
//                val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI // 是个目录
//                val uri = context.makePhoto(UUID.randomUUID().toString()) // 生成文件路径， 上面2个目录只有 EXTERNAL_CONTENT_URI 权限够。
//                photoTakeLauncher.launch(uri)
                photoTakeLauncher.launch(photoUri)
            }
        ) {
            Text(
                text = "拍照片"
            )
        }

        Button(
            enabled = hasPermissions,
            onClick = {
                photoPreviewPickLauncher.launch(null)
            }
        ) {
            Text(
                text = "拍照片（预览 Bitmap）"
            )
        }

        AsyncImage(
            model = bitmap,
            contentDescription = "图片"
        )

        val copyVideoAndAudioLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) {
            it?.let {
                Toast.makeText(context, "start", Toast.LENGTH_SHORT).show()
                ioScope.launch {
                    context.copyVideoAndAudio(it)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "end", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        Button(
            onClick = {
                copyVideoAndAudioLauncher.launch("video/mp4")
            }
        ) {
            Text(text = "复制双轨")
        }

        val videoCompressLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) {
            it?.let {
                Toast.makeText(context, "start", Toast.LENGTH_SHORT).show()
                ioScope.launch {
                    context.compressVideo(it, 400)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "end", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        Button(
            onClick = {
                videoCompressLauncher.launch("video/mp4")
            }
        ) {
            Text(text = "压缩")
        }
    }
}