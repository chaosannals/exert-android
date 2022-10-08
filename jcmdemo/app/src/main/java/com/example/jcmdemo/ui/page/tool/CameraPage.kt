package com.example.jcmdemo.ui.page.tool

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
// import androidx.camera.core.VideoCapture
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.*
import androidx.camera.core.Preview as CameraPreview
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.core.net.toFile
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.jcmdemo.LocalNavController
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import com.example.jcmdemo.R
import com.example.jcmdemo.ui.routeTo
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors

sealed class CameraUIAction {
    object OnCameraClick : CameraUIAction()
    object OnGalleryViewClick : CameraUIAction()
    object OnSwitchCameraClick : CameraUIAction()
    object OnVideocamOpen: CameraUIAction()
    object OnVideocamClose: CameraUIAction()
}

private const val FILENAME = "yyyy-MM-dd-HH-mm-ss-SSS"
private const val PHOTO_EXTENSION = ".jpg"
private const val VIDEO_EXTENSION = ".mp4"

fun ImageCapture.takePicture(
    context: Context,
    lensFacing: Int,
    onImageCaptured: (Uri, Boolean) -> Unit,
    onError: (ImageCaptureException) -> Unit
) {
    val outputDirectory = context.getOutputDirectory()
    // Create output file to hold the image
    val photoFile = createFile(outputDirectory, FILENAME, PHOTO_EXTENSION)
    val outputFileOptions = getOutputFileOptions(lensFacing, photoFile)

    this.takePicture(
        outputFileOptions,
        Executors.newSingleThreadExecutor(),
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                val savedUri = output.savedUri ?: Uri.fromFile(photoFile)
                // If the folder selected is an external media directory, this is
                // unnecessary but otherwise other apps will not be able to access our
                // images unless we scan them using [MediaScannerConnection]
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


fun getOutputFileOptions(
    lensFacing: Int,
    photoFile: File
): ImageCapture.OutputFileOptions {

    // Setup image capture metadata
    val metadata = ImageCapture.Metadata().apply {
        // Mirror image when using the front camera
        isReversedHorizontal = lensFacing == CameraSelector.LENS_FACING_FRONT
    }
    // Create output options object which contains file + metadata
    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile)
        .setMetadata(metadata)
        .build()

    return outputOptions
}

fun createFile(baseFolder: File, format: String, extension: String) =
    File(
        baseFolder, SimpleDateFormat(format, Locale.US)
            .format(System.currentTimeMillis()) + extension
    )


fun Context.getOutputDirectory(): File {
    val mediaDir = this.externalMediaDirs.firstOrNull()?.let {
        File(it, this.resources.getString(R.string.app_name)).apply { mkdirs() }
    }
    return if (mediaDir != null && mediaDir.exists())
        mediaDir else this.filesDir
}

suspend fun Context.getCameraProvider(): ProcessCameraProvider = suspendCoroutine { continuation ->
    ProcessCameraProvider.getInstance(this).also { cameraProvider ->
        cameraProvider.addListener({
            continuation.resume(cameraProvider.get())
        }, ContextCompat.getMainExecutor(this))
    }
}

fun VideoCapture<Recorder>.startRecording(context: Context, listener: (VideoRecordEvent) -> Unit) : Recording {
    // 以下 MediaStore 官网解决方案，实测 安卓 10 以下 不行。还是要使用文件。
    // 媒体本身不需要权限，配置外接需要以下权限。   sdk 28 以下要配置 maxSdkVersion=28
    //<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" android:maxSdkVersion="28" />
//            val name = SimpleDateFormat(FILENAME, Locale.US)
//                .format(System.currentTimeMillis()) + ".mp4"
//            val contentValues = ContentValues().apply {
//                put(MediaStore.MediaColumns.DISPLAY_NAME, name)
//                put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4")
//                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
//                    put(MediaStore.Video.Media.RELATIVE_PATH, "Movies/CameraX-Video")
//                }
//            }
//            val mediaStoreOutputOptions = MediaStoreOutputOptions
//                .Builder(context.contentResolver, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
//                .setContentValues(contentValues)
//                .build()

    val outputDirectory = context.getOutputDirectory()
    val videoFile = createFile(outputDirectory, FILENAME, VIDEO_EXTENSION)
    var fileOutputOptions = FileOutputOptions
        .Builder(videoFile)
        .build()

    return this.output
        .prepareRecording(context, fileOutputOptions)
        //.prepareRecording(context, mediaStoreOutputOptions)
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

@Composable
fun CameraControls(cameraUIAction: (CameraUIAction) -> Unit, isVideo: Boolean) {
    var iconSize = 50.dp
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorResource(id = R.color.black))
            .padding(10.dp, 10.dp, 10.dp, 100.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        CameraControl(
            R.drawable.ic_flip_camera_android,
            R.string.icn_camera_view_switch_camera_content_description,
            modifier= Modifier.size(iconSize),
            enabled = !isVideo,
            onClick = {
                // 录像的时候不能切镜头。
                if (!isVideo) {
                    cameraUIAction(CameraUIAction.OnSwitchCameraClick)
                }
            }
        )

        CameraControl(
            R.drawable.ic_lens,
            R.string.icn_camera_view_camera_shutter_content_description,
            modifier= Modifier
                .size(iconSize)
                .padding(1.dp)
                .border(1.dp, colorResource(id = R.color.white), CircleShape),
            enabled = !isVideo, // 录像的时候不能按快门拍照
            onClick = { cameraUIAction(CameraUIAction.OnCameraClick) }
        )

        if (isVideo) {
            CameraControl(
                R.drawable.ic_videocam_off,
                R.string.icn_camera_view_camera_record_video_close_description,
                modifier= Modifier
                    .size(iconSize)
                    .padding(1.dp)
                    .border(1.dp, colorResource(id = R.color.white), CircleShape),
                onClick = { cameraUIAction(CameraUIAction.OnVideocamClose) }
            )
        } else {
            CameraControl(
                R.drawable.ic_videocam,
                R.string.icn_camera_view_camera_record_video_description,
                modifier = Modifier
                    .size(iconSize)
                    .padding(1.dp)
                    .border(1.dp, colorResource(id = R.color.white), CircleShape),
                onClick = { cameraUIAction(CameraUIAction.OnVideocamOpen) }
            )
        }

        CameraControl(
            R.drawable.ic_photo_library,
            R.string.icn_camera_view_view_gallery_content_description,
            modifier= Modifier.size(iconSize),
            onClick = { cameraUIAction(CameraUIAction.OnGalleryViewClick) }
        )
    }
}


@Composable
fun CameraControl(
    icon: Int,
    contentDescId: Int,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
    ) {
        Icon(
            painterResource(id = icon),
            contentDescription = stringResource(id = contentDescId),
            modifier = modifier,
            tint = colorResource(id = R.color.white)
        )
    }

}

//@SuppressLint("RestrictedApi")
@Composable
private fun CameraPreviewView(
    imageCapture: ImageCapture,
    videoCapture: VideoCapture<Recorder>,
    isVideo: Boolean,
    lensFacing: Int = CameraSelector.LENS_FACING_BACK,
    cameraUIAction: (CameraUIAction) -> Unit
) {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val preview = CameraPreview.Builder().build()
    val cameraSelector = CameraSelector.Builder()
        .requireLensFacing(lensFacing)
        .build()

    // 检查权限，无则申请。
    var sp = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.CAMERA
    )
    if (sp != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO,
            ),
            1 // 自定标识
        )
    }

    var recording : Recording? by remember {
        mutableStateOf(null)
    }
    val previewView = remember { PreviewView(context) }
    val onVideoRecord = { recordEvent: VideoRecordEvent ->
        when(recordEvent) {
            // is VideoRecordEvent.Start -> {}
            is VideoRecordEvent.Finalize -> {
                if (recordEvent.hasError()) {
                    recording?.close()
                    recording = null
                }
            }
        }
    }
    val switchLens = suspend {
        val cameraProvider = context.getCameraProvider()
        val capture = if (isVideo) { videoCapture } else { imageCapture }
        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(
            lifecycleOwner,
            cameraSelector,
            preview,
            capture
        )
        preview.setSurfaceProvider(previewView.surfaceProvider)
    }

    LaunchedEffect(lensFacing) {
        switchLens()
    }

    LaunchedEffect(isVideo) {
        if (recording != null) {
            recording?.stop()
            recording = null
        }

        switchLens()

        if (isVideo) {
            videoCapture.startRecording(context, onVideoRecord)
        }
    }


    Column(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            { previewView },
            modifier = Modifier.weight(1.0f)
                .fillMaxWidth()
        ) {

        }
        Column(
            //modifier = Modifier.align(Alignment.BottomCenter),
            verticalArrangement = Arrangement.Bottom
        ) {
            CameraControls(cameraUIAction, isVideo)
        }
    }
}

@Composable
fun CameraPage () {
    val onImageCaptured =  { uri: Uri,a: Boolean ->
        print(uri)
        //navController.routeTo("gist")
    }
    val onError = { imge: ImageCaptureException ->
        print(imge)
    }

    val context = LocalContext.current
    var lensFacing by remember { mutableStateOf(CameraSelector.LENS_FACING_BACK) }
    val imageCapture: ImageCapture = remember {
        ImageCapture.Builder().build()
    }

    var isVideo by remember { mutableStateOf(false) }
    val videoCapture: VideoCapture<Recorder> = remember {
        val recorder = Recorder.Builder()
            .setQualitySelector(QualitySelector.from(Quality.HIGHEST))
            .build()
        VideoCapture.withOutput(recorder)
    }


    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) onImageCaptured(uri, true)
    }
    CameraPreviewView(
        imageCapture,
        videoCapture,
        isVideo,
        lensFacing
    ) { cameraUIAction ->
        when (cameraUIAction) {
            is CameraUIAction.OnCameraClick -> {
                imageCapture.takePicture(context, lensFacing, onImageCaptured, onError)
            }
            is CameraUIAction.OnSwitchCameraClick -> {
                lensFacing =
                    if (lensFacing == CameraSelector.LENS_FACING_BACK) CameraSelector.LENS_FACING_FRONT
                    else
                        CameraSelector.LENS_FACING_BACK
            }
            is CameraUIAction.OnGalleryViewClick -> {
                if (true == context.getOutputDirectory().listFiles()?.isNotEmpty()) {
                    galleryLauncher.launch("image/*")
                }
            }
            is CameraUIAction.OnVideocamOpen -> {
                isVideo = true
            }
            is CameraUIAction.OnVideocamClose -> {
                isVideo = false
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CameraPagePreview() {
    CompositionLocalProvider(
        LocalNavController provides rememberNavController(),
    ) {
        CameraPage()
    }
}