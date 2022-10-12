package com.example.jcm3demo.ui.page.tool

import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.video.*
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.jcm3demo.R

@Composable
fun CameraViewport(
    cameraAction: (
        event: CameraViewportEvent,
        imageCapture: ImageCapture,
        videoCapture: VideoCapture<Recorder>,
        lensFacing: Int,
    ) -> Unit,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var lensFacing by remember { mutableStateOf(CameraSelector.LENS_FACING_BACK) }
    val imageCapture: ImageCapture = remember {
        ImageCapture.Builder().build()
    }
    val videoCapture: VideoCapture<Recorder> = remember {
        val recorder = Recorder.Builder()
            .setQualitySelector(QualitySelector.from(Quality.HIGHEST))
            .build()
        VideoCapture.withOutput(recorder)
    }

    val preview = androidx.camera.core.Preview.Builder().build()
    val cameraSelector = CameraSelector.Builder()
        .requireLensFacing(lensFacing)
        .build()

    var isVideo by remember { mutableStateOf(false) }
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

    val iconSize = 50.dp
    Column(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            { previewView },
            modifier = Modifier
                .weight(1.0f)
                .fillMaxWidth()
        ) {

        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(colorResource(id = R.color.black))
                .padding(10.dp, 10.dp, 10.dp, 100.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                enabled = !isVideo,
                onClick = {
                    lensFacing = if (lensFacing == CameraSelector.LENS_FACING_BACK) {
                        CameraSelector.LENS_FACING_FRONT
                    } else {
                        CameraSelector.LENS_FACING_BACK
                    }
                    cameraAction(
                        CameraViewportEvent.LensSwitched,
                        imageCapture,
                        videoCapture,
                        lensFacing,
                    )
                },
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_flip_camera_ios),
                    contentDescription = "切换相机",
                    modifier= Modifier.size(iconSize),
                    tint = colorResource(id = R.color.white)
                )
            }

            IconButton(
                enabled = !isVideo,
                onClick = {
                    cameraAction(
                        CameraViewportEvent.ShutterClicked,
                        imageCapture,
                        videoCapture,
                        lensFacing,
                    )
                },
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_lens),
                    contentDescription = "拍照",
                    modifier= Modifier.size(iconSize),
                    tint = colorResource(id = R.color.white)
                )
            }

            IconButton(
                onClick = {
                    isVideo = !isVideo
                    val e = if (isVideo) {
                        CameraViewportEvent.VideoRecordStart
                    } else {
                        CameraViewportEvent.VideoRecordEnd
                    }
                    cameraAction(
                        e,
                        imageCapture,
                        videoCapture,
                        lensFacing,
                    )
                },
            ) {
                Icon(
                    painter = painterResource(
                        id = if (isVideo) {
                            R.drawable.ic_videocam_off
                        } else {
                            R.drawable.ic_videocam
                        }),
                    contentDescription = "录像",
                    modifier= Modifier.size(iconSize),
                    tint = colorResource(id = R.color.white)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CameraViewportPreview() {
    CameraViewport { e, i, v, lf -> }
}