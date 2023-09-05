package com.example.bootdemo.ui.page.drawing

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.media.projection.MediaProjectionManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ScreenRecord2Page() {
    val context = LocalContext.current
    val isPreview = LocalInspectionMode.current

    var tip by remember {
        mutableStateOf("")
    }

    val metrics = remember {
        Resources.getSystem().displayMetrics
    }
    val mediaProjectManager = remember(context, isPreview) {
        if (isPreview) null else context.getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
    }

    val permitLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) {

    }

    val captureLauncher = rememberLauncherForActivityResult(
        contract =  ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
//            val intent = Intent(context, ScreenRecordService::class.java)
//            intent.putExtra("code", it.resultCode)
//            intent.putExtra("data", it.data)
//            intent.putExtra("dpi", metrics.densityDpi)
//            intent.putExtra("width", metrics.widthPixels)
//            intent.putExtra("height", metrics.heightPixels)
//            context.startForegroundService(intent)
        }
    }
}

@Preview
@Composable
fun ScreenRecord2PagePreview() {
    ScreenRecord2Page()
}