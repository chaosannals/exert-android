package com.example.jcm3ui.ui.page.demo

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.core.UseCase
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.MediaStoreOutputOptions
import androidx.camera.video.Quality
import androidx.camera.video.QualitySelector
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.camera.video.VideoRecordEvent
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.lifecycle.LifecycleOwner
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.decode.VideoFrameDecoder
import com.example.jcm3ui.ui.sdp
import com.example.jcm3ui.ui.sf
import com.example.jcm3ui.ui.ssp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale
import java.util.concurrent.Executors

const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"

fun Context.startCamera(
    cameraSelector: CameraSelector,
    lifecycleOwner: LifecycleOwner,
    viewFinder: PreviewView,
    useCase: UseCase,
) {
    val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
    cameraProviderFuture.addListener({
        val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

        val preview = Preview.Builder()
            .build()
            .apply {
                setSurfaceProvider(viewFinder.surfaceProvider)
            }

        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview,
                useCase
            )

        } catch(exc: Exception) {

        }

    }, ContextCompat.getMainExecutor(this))
}

fun Context.takePhoto(imageCapture: ImageCapture, onResult: (Uri?) -> Unit) {
    val name = SimpleDateFormat(FILENAME_FORMAT, Locale.US)
        .format(System.currentTimeMillis())
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, name)
        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
        }
    }
    val outputOptions = ImageCapture.OutputFileOptions
        .Builder(contentResolver,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues)
        .build()

    imageCapture.takePicture(
        outputOptions,
        ContextCompat.getMainExecutor(this),
        object : ImageCapture.OnImageSavedCallback {
            override fun onError(exc: ImageCaptureException) {
                onResult(null)
            }

            override fun
                    onImageSaved(output: ImageCapture.OutputFileResults){
                onResult(output.savedUri)
            }
        }
    )
}

fun Context.takeVideo(
    videoCapture: VideoCapture<Recorder>,
    onResult: (VideoRecordEvent) -> Unit,
): Recording {
    val name = SimpleDateFormat(FILENAME_FORMAT, Locale.US)
        .format(System.currentTimeMillis())
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, name)
        put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4")
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            put(MediaStore.Video.Media.RELATIVE_PATH, "Movies/CameraX-Video")
        }
    }
    val mediaStoreOutputOptions = MediaStoreOutputOptions
        .Builder(contentResolver, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
        .setContentValues(contentValues)
        .build()
    return videoCapture.output
        .prepareRecording(this, mediaStoreOutputOptions)
        .also {
            if (PermissionChecker.checkSelfPermission(this,
                    Manifest.permission.RECORD_AUDIO) ==
                PermissionChecker.PERMISSION_GRANTED)
            {
                it.withAudioEnabled()
            }
        }
        .start(ContextCompat.getMainExecutor(this), onResult)
}

@androidx.compose.ui.tooling.preview.Preview
@Composable
fun CameraShotPage() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

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

    // cameraExecutor 是做图片和视频分析用的，此功能不需要分析。
//    val cameraExecutor = remember {
//        Executors.newSingleThreadExecutor()
//    }
    val imageCapture = remember {
        ImageCapture.Builder().build()
    }

    var durationMs by remember {
        mutableStateOf(0)
    }
    val recorder = remember {
        Recorder.Builder()
            .setQualitySelector(QualitySelector.from(Quality.HIGHEST))
            .build()
    }
    val videoCapture = remember(recorder) {
        VideoCapture.withOutput(recorder)
    }
    var recording by remember {
        mutableStateOf<Recording?>(null)
    }

    DisposableEffect(Unit) {
        onDispose {
            recording?.apply {
                stop()
                close()
            }
        }
    }

//    DisposableEffect(cameraExecutor) {
//        onDispose {
//            cameraExecutor.shutdown()
//        }
//    }

    var mode by remember {
        mutableStateOf(FileType.Image)
    }

    var isRecord by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(isRecord) {
        if (isRecord) {
            durationMs = 0
            launch(Dispatchers.IO) {
                while (true) {
                    delay(1000)
                    durationMs += 1000
                }
            }
        }
    }

    var cameraSelector by remember {
        mutableStateOf(CameraSelector.DEFAULT_BACK_CAMERA)
    }

    val previewView = remember {
        PreviewView(context)
    }

    var hasPermissions by remember {
        mutableStateOf(false)
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) {
        hasPermissions = it
                .map{ it.value }
                .reduce { acc, entry -> acc && entry }
    }

    var thumbUri by remember {
        mutableStateOf<Uri?>(null)
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

    LaunchedEffect(hasPermissions, cameraSelector, mode, lifecycleOwner, previewView) {
        if (hasPermissions) {
            context.startCamera(
                cameraSelector,
                lifecycleOwner,
                previewView,
                if (mode == FileType.Image) imageCapture else videoCapture
            )
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row(
            horizontalArrangement= Arrangement.SpaceBetween,
            verticalAlignment= Alignment.Bottom,
            modifier = Modifier
                .zIndex(4f)
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .height(64.sdp)
                .background(Color(0xE5333333))
                .padding(bottom = 15.sdp, start = 15.sdp, end = 15.sdp)
        ) {
            Text(
                text = "取消",
                color = Color.White,
                fontSize=14.ssp,
            )

            val durationText by remember(durationMs) {
                derivedStateOf {
                    formatDuration(durationMs)
                }
            }

            Text(
                text = durationText,
                color = Color.White,
                fontSize = 12.ssp,
            )
            Spacer(modifier=Modifier)
        }

        AndroidView(
            factory = {
                previewView.apply {
                    layoutParams = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                }
            },
            modifier = Modifier
                .fillMaxSize()
        ) {

        }


        Column (
            modifier = Modifier
                .zIndex(4f)
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ){
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier
                    .padding(bottom = 15.sdp)
                    .fillMaxWidth()
            ) {
                val modes = remember {
                    FileType.values().apply {
                        reverse()
                    }
                }
                modes.forEach {
                    Text(
                        text = it.title,
                        color = if (mode == it) Color(0xFFFF8D1A) else Color.White,
                        fontSize=12.ssp,
                        modifier = Modifier
                            .padding(horizontal = 10.sdp)
                            .clickable {
                                mode = it
                            }
                    )
                }
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(103.sdp)
                    .background(Color(0xFF333333))
                    .padding(bottom = 13.sdp, start = 15.sdp, end = 15.sdp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.sdp)
                        .border(
                            0.5.sdp,
                            Color.White,
                            RoundedCornerShape(2.sdp),
                        )
                ) {
                    thumbUri?.let {
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

                Box(
                    modifier = Modifier
                        .size(60.sdp)
                        .drawBehind {
                            val center = Offset(size.width, size.height).div(2f)
                            drawCircle(
                                color = if (mode == FileType.Video) Color(0xFFF33636) else Color.White,
                                radius = (size.width * if (isRecord) 0.73f else 0.833f) / 2f,
                                center = center,
                            )
                            drawCircle(
                                color = Color.White,
                                radius = size.width / 2f,
                                center = center,
                                style = Stroke(
                                    width = if (isRecord) 4f.sf else 4.9f.sf,
                                )
                            )
                        }
                        .clickable {
                            if (mode == FileType.Image) {
                                context.takePhoto(imageCapture) {
                                    thumbUri = it
                                }
                            } else {
                                if (isRecord) {
                                    recording?.stop()
                                } else {
                                    recording = context.takeVideo(videoCapture) {
                                        when (it) {
                                            is VideoRecordEvent.Start -> {
                                                isRecord = true
                                            }

                                            is VideoRecordEvent.Finalize -> {
                                                if (!it.hasError()) {
                                                    thumbUri = it.outputResults.outputUri
                                                } else {
                                                    recording?.close()
                                                    recording = null
                                                }
                                                isRecord = false
                                            }
                                        }
                                    }
                                }
                            }
                        }
                )
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(30.sdp)
                        .drawBehind {
                            drawCircle(
                                color = Color.White,
                                radius = size.width / 2f,
                                center = Offset(size.width, size.height).div(2f),
                                style = Stroke(
                                    width = 0.5f.sf,
                                )
                            )
                        }
                        .clickable {
                            cameraSelector =
                                if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                                    CameraSelector.DEFAULT_FRONT_CAMERA
                                } else {
                                    CameraSelector.DEFAULT_BACK_CAMERA
                                }
                        }
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        "切换前后摄像头",
                        tint = Color.White,
                    )
                }
            }
        }
    }
}