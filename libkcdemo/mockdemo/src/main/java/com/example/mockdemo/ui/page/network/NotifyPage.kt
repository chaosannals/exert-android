package com.example.mockdemo.ui.page.network

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.mockdemo.NotifyPageReceiver
import com.example.mockdemo.PeriodicToastWorker
import com.example.mockdemo.R
import com.example.mockdemo.ui.DesignPreview
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.concurrent.TimeUnit

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

                    val nid = 100
                    // 点击取消按钮的 Intent 添加一个 Action 附带 Intent
                    val intent = Intent(
                        context,
                        NotifyPageReceiver::class.java
                    ).apply {
                        action = "close-notify"
                        putExtra("Nid", nid)
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
                        .setContentTitle("标题")
                        .setContentText("内容")
                        .setContentIntent(contentIntent)
                        .setAutoCancel(true) // 自动关闭通知栏，只有设置了 ContentIntent 才起效。
                        .setDeleteIntent(cancelIntent) // 自定义滑动取消 Intent
                        // .setOngoing(true) // 固定通知栏，导致没办法通过滑动取消
                        .setTicker("通知栏到达时，状态栏突显的文字")
                        .setWhen(timestamp)
                        //.setShowWhen(false)
                        .setSmallIcon(R.drawable.ic_notifications_active)
                        .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.ic_notifications_active))
                        .setNumber(234)
                        .setOnlyAlertOnce(true) // 唯一提醒，通过 ID 来防止重复。
                        .setProgress(100, 44, false)
                        .setVibrate(longArrayOf(1000L, 1000L, 400L, 400L)) // 震动间隔
                        .setUsesChronometer(true) // 设置计时器
                        // .setFullScreenIntent(contentIntent, true) // 直接跳进 Intent 指向的应用，一般电话才这么搞。。
                        .addAction(R.drawable.ic_notifications_active, "取消", pendingIntent)
                        .build()
                    manager.notify(nid, notification)
//                    manager.cancel(nid) # 关闭指定 id 的通知栏

                }
        )

        val uniName = "唯一标志名"
        Text(
            text="注册周期性 Work",
            modifier = Modifier
                .clickable {
                    // 最短 15分钟。
                    val request = PeriodicWorkRequestBuilder<PeriodicToastWorker>(15, TimeUnit.MINUTES)
                        .build()
                    WorkManager.getInstance(context)
                        .enqueueUniquePeriodicWork(
                            uniName,
                            ExistingPeriodicWorkPolicy.KEEP, // 如果上一个存在，保留上次，这次跳过,
                            request
                        )
                    Toast.makeText(context, "register", Toast.LENGTH_SHORT).show()
                }
        )
        Text(
            text="取消周期性 Work",
            modifier = Modifier
                .clickable {
                    WorkManager.getInstance(context)
                        .cancelUniqueWork(uniName)
                    Toast.makeText(context, "unregister", Toast.LENGTH_SHORT).show()
                },
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