package com.example.mockdemo.ui.page.network

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.WorkManager
import com.example.mockdemo.NotifyPageReceiver
import com.example.mockdemo.R
import com.example.mockdemo.ui.DesignPreview
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

@Composable
fun NotifyPage() {
    val context = LocalContext.current
    val canNotify = NotificationManagerCompat
        .from(context)
        .areNotificationsEnabled()

    Column (
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            text = "通知栏权限设置",
            modifier = Modifier
                .clickable {
                    val localIntent = Intent()
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS")
                        localIntent.setData(Uri.fromParts("package", context.packageName, null))
                    } else {
                        localIntent.setAction("android.settings.APP_NOTIFICATION_SETTINGS")
                        localIntent.putExtra("app_package", context.packageName)
                        localIntent.putExtra("app_uid", context.applicationInfo.uid)
                    }
                    context.startActivity(localIntent)
                }
        )
        Text(
            text="发送",
            modifier = Modifier
                .clickable {
                    val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    val id = "myNotifyChannelId"
                    val name: CharSequence = "getString(R.string.channel_name)"
                    val description = "getString(R.string.channel_description)"
                    val importance = NotificationManager.IMPORTANCE_DEFAULT
                    val channel = NotificationChannel(id, name, importance)
                    channel.description = description
                    manager.createNotificationChannel(channel)

                    // 点击取消按钮的 Intent 添加一个 Action 附带 Intent
                    val intent = Intent(
                        context,
                        NotifyPageReceiver::class.java
                    ).apply {
                        action = "close-notify"
                        putExtra("Nid", 1)
                    }
                    val pendingIntent = PendingIntent
                        .getBroadcast(context, 0, intent, 0)

                    // 点击了内容 Intent
                    val contentIntent = PendingIntent
                        .getActivity(context, 0, (context as Activity).intent, 0)

                    // 滑动取消的 Intent
                    val cancelIntent = PendingIntent
                        .getBroadcast(context, 0, intent, 0)

                    val timestamp = LocalDateTime
                        .now()
                        .atZone(ZoneId.of("PRC"))
                        .toInstant()
                        .toEpochMilli()

                    val notification = NotificationCompat.Builder(context, id)
                        .setContentTitle("title")
                        .setContentText("progress")
                        .setContentIntent(contentIntent)
                        .setAutoCancel(true) // 自动关闭通知栏，只有设置了 ContentIntent 才起效。
//                        .setDeleteIntent(cancelIntent) // 滑动取消
                        // .setOngoing(true) // 固定通知栏，导致没办法通过滑动取消
                        .setTicker("title")
                        .setWhen(timestamp)
                        //.setShowWhen(false)
                        .setSmallIcon(R.drawable.ic_notifications_active)
                        .addAction(R.drawable.ic_notifications_active, "取消", pendingIntent)
                        .build()
                    manager.notify(1, notification)
//                    manager.cancel(1) # 关闭指定 id 的通知栏
                }
        )
    }
}

@Preview
@Composable
fun NotifyPagePreview() {
    DesignPreview {
        NotifyPage()
    }
}