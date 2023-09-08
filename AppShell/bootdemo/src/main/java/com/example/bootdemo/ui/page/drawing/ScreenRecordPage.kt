package com.example.bootdemo.ui.page.drawing

import android.app.Activity.RESULT_OK
import android.content.ComponentName
import android.content.Context.BIND_AUTO_CREATE
import android.content.Context.MEDIA_PROJECTION_SERVICE
import android.content.Intent
import android.content.ServiceConnection
import android.content.res.Resources
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.os.IBinder
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.bootdemo.HandleRxPublish
import com.example.bootdemo.MainActivity
import com.example.bootdemo.ScreenRecordService
import com.example.bootdemo.screenRecordTip

@Composable
fun ScreenRecordPage() {
    val context = LocalContext.current
    val isPreview = LocalInspectionMode.current

    val screenHeight = remember(context) {
        if (isPreview) 0 else (context as MainActivity).window.decorView.height
    }

    var tip by remember {
        mutableStateOf("")
    }

    val metrics = remember {
        Resources.getSystem().displayMetrics
    }

    var recordService: ScreenRecordService? by remember {
        mutableStateOf(null)
    }

    val mediaProjectManager = remember(context, isPreview) {
        if (isPreview) null else context.getSystemService(MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
    }

    val permitLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) {

    }

    val captureLauncher = rememberLauncherForActivityResult(
        contract =  ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            recordService?.run {
                mediaProjection = mediaProjectManager?.getMediaProjection(it.resultCode, it.data!!)
                dpi = metrics.densityDpi
                width = metrics.widthPixels
                height = screenHeight // metrics.heightPixels
                outDir = context.externalMediaDirs.firstOrNull()?.absolutePath
            }
        }
    }

    val serviceConnection = remember {
        object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                val binder = service as? ScreenRecordService.ScreenRecordBinder
                recordService = binder?.service as? ScreenRecordService
                val captureIntent = mediaProjectManager?.createScreenCaptureIntent()
                captureLauncher.launch(captureIntent)
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                tip = "屏幕录制服务链接失败"
            }
        }
    }

    LaunchedEffect(Unit) {
        permitLauncher.launch(
            arrayOf(
                android.Manifest.permission.RECORD_AUDIO,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        )
    }

    HandleRxPublish(screenRecordTip) {
        tip = it
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_DESTROY -> {
                    context.unbindService(serviceConnection)
                }
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxSize()
    ) {
        Text("Tip: $tip")
        Button(onClick = {
            if(recordService == null) {
                val intent = Intent(context, ScreenRecordService::class.java)
                context.bindService(intent, serviceConnection, BIND_AUTO_CREATE)
                tip = "初始化绑定服务"
            } else if (recordService?.isRunning == true) {
                tip = "已经在录制"
            } else if(recordService?.isRunning == false) {
                recordService?.startRecord()

                tip = "开始录制"

                // 开始录制，切出当前应用
//                val intent = Intent(Intent.ACTION_MAIN)
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                intent.addCategory(Intent.CATEGORY_HOME)
//                context.startActivity(intent)
            }
        }) {
            Text(text = "开始录制")
        }

        Button(onClick = {
            if (recordService?.isRunning == true) {
                recordService?.stopRecord()
            } else {
                tip = "还没有开始录制"
            }
        }) {
            Text("停止录制")
        }
    }
}

@Preview
@Composable
fun ScreenRecordPagePreview() {
    ScreenRecordPage()
}