package com.example.jcm3demo.ui.page.tool

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

@Composable
fun CameraPage() {
    val context = LocalContext.current

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

    CameraViewport() { e, ic, vc, lf ->
        when (e) {
            CameraViewportEvent.ShutterClicked -> {
                ic.takePicture(context, lf, { uri, b ->

                }, {

                })
            }
            CameraViewportEvent.LensSwitched -> {

            }
            CameraViewportEvent.VideoRecordStart -> {

            }
            CameraViewportEvent.VideoRecordEnd -> {

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CameraPagePreview() {
    CameraPage()
}