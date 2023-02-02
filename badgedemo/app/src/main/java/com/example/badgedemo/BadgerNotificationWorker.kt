package com.example.badgedemo

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import kotlinx.coroutines.delay

class BadgerNotificationWorker(appContext: Context,
                               params: WorkerParameters
) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        while (true) {
            Toast.makeText(applicationContext, "消息", Toast.LENGTH_SHORT).show()
            delay(10000)
            setForeground(getForegroundInfo())
        }

        return Result.success()
    }

    // 使用 setForeground 时必须实现 getForegroundInfo 不然会报没有实现错误。
    override suspend fun getForegroundInfo(): ForegroundInfo {
        val id = "myNotifyChannelId"
        val name: CharSequence = "Mock OneTimeNotification Name"
        val description = "Mock Demo One Time Notification。"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(id, name, importance)
        channel.description = description

        val manager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(applicationContext, id)
            .setContentTitle("OneTime")
            .setTicker("title")
            .setContentText("单次通知栏消息")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()

        return ForegroundInfo(
            1,
            notification,
            FOREGROUND_SERVICE_TYPE_LOCATION,
        )
    }
}