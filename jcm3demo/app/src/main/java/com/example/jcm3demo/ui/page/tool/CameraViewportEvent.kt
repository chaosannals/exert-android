package com.example.jcm3demo.ui.page.tool

import android.Manifest
import android.content.Context
import android.media.MediaScannerConnection
import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.*
import androidx.camera.video.VideoCapture
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.core.net.toFile
import androidx.lifecycle.LifecycleOwner
import com.example.jcm3demo.R
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

private const val FILENAME = "yyyy-MM-dd-HH-mm-ss-SSS"
private const val PHOTO_EXTENSION = ".jpg"
private const val VIDEO_EXTENSION = ".mp4"

// 相机视口事件
enum class CameraViewportEvent {
    ShutterClicked,
    LensSwitched,
    VideoRecordStart,
    VideoRecordEnd,
}

// 获取输出目录
fun Context.getOutputDirectory(): File {
    val mediaDir = this.externalMediaDirs.firstOrNull()?.let {
        File(it, this.resources.getString(R.string.app_name)).apply { mkdirs() }
    }
    return if (mediaDir != null && mediaDir.exists())
        mediaDir else this.filesDir
}

// 获取相机提供者
suspend fun Context.getCameraProvider(): ProcessCameraProvider = suspendCoroutine { continuation ->
    ProcessCameraProvider.getInstance(this).also { cameraProvider ->
        cameraProvider.addListener({
            continuation.resume(cameraProvider.get())
        }, ContextCompat.getMainExecutor(this))
    }
}

// 创建文件
fun createFile(baseFolder: File, format: String, extension: String) =
    File(
        baseFolder, SimpleDateFormat(format, Locale.US)
            .format(System.currentTimeMillis()) + extension
    )

// 获取输出文件信息
fun getOutputFileOptions(
    lensFacing: Int,
    photoFile: File
): ImageCapture.OutputFileOptions {
    val metadata = ImageCapture.Metadata().apply {
//        isReversedHorizontal = lensFacing == CameraSelector.LENS_FACING_FRONT
    }
    val outputOptions = ImageCapture
        .OutputFileOptions
        .Builder(photoFile)
        .setMetadata(metadata)
        .build()

    return outputOptions
}

// 拍照
fun ImageCapture.takePicture(
    context: Context,
    lensFacing: Int,
    onImageCaptured: (Uri, Boolean) -> Unit,
    onError: (ImageCaptureException) -> Unit
) {
    val outputDirectory = context.getOutputDirectory()
    val photoFile = createFile(outputDirectory, FILENAME, PHOTO_EXTENSION)
    val outputFileOptions = getOutputFileOptions(lensFacing, photoFile)

    this.takePicture(
        outputFileOptions,
        Executors.newSingleThreadExecutor(),
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                val savedUri = output.savedUri ?: Uri.fromFile(photoFile)

                // 刷新使得系统画册可以看到
                val mimeType = MimeTypeMap.getSingleton()
                    .getMimeTypeFromExtension(savedUri.toFile().extension)
                MediaScannerConnection.scanFile(
                    context,
                    arrayOf(savedUri.toFile().absolutePath),
                    arrayOf(mimeType)
                ) { _, uri ->

                }


                onImageCaptured(savedUri, false)
            }
            override fun onError(exception: ImageCaptureException) {
                onError(exception)
            }
        })
}

// 开始录像
fun VideoCapture<Recorder>.startRecording(context: Context, listener: (VideoRecordEvent) -> Unit) : Recording {
    val outputDirectory = context.getOutputDirectory()
    val videoFile = createFile(outputDirectory, FILENAME, VIDEO_EXTENSION)
    val fileOutputOptions = FileOutputOptions
        .Builder(videoFile)
        .build()

    return this.output
        .prepareRecording(context, fileOutputOptions)
        .apply {
            if (PermissionChecker.checkSelfPermission(
                    context,
                    Manifest.permission.RECORD_AUDIO) ==
                PermissionChecker.PERMISSION_GRANTED)
            {
                withAudioEnabled()
            }
        }
        .start(ContextCompat.getMainExecutor(context), listener)
}