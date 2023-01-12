package com.example.mockdemo.ui.page.network

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.NotificationManagerCompat
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import cn.chaosannals.dirtool.ensurePermission
import com.example.mockdemo.OneTimeNotificationWorker
import com.example.mockdemo.ui.DesignPreview

@Composable
fun NotifyWorkPage() {
    val context = LocalContext.current

    val canNotify = NotificationManagerCompat
        .from(context)
        .areNotificationsEnabled()

    context.ensurePermission(Manifest.permission.ACCESS_NOTIFICATION_POLICY, 10)

    var isGranted by remember {
        mutableStateOf(false)
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { ig ->
        isGranted = ig
    }

//    context.ensurePermission(Manifest.permission.CAMERA, 10)
//    context.ensurePermission(Manifest.permission.POST_NOTIFICATIONS, 10)

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            text="canNotify: ${canNotify}",
        )

        Text(
            text = "gggg: ${isGranted}",
            modifier = Modifier
                .clickable {
                    launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
        )

        Text(
            text = "send",
            modifier = Modifier
                .clickable {
                    val request = OneTimeWorkRequestBuilder<OneTimeNotificationWorker>()
//                    .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                        .build()
                    WorkManager
                        .getInstance(context)
                        .enqueue(request)
                },
        )
    }
}

@Preview
@Composable
fun NotifyWorkPagePreview() {
    DesignPreview {
        NotifyWorkPage()
    }
}