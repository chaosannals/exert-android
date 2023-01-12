package com.example.mockdemo

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import kotlinx.coroutines.delay


class OneTimeNotificationWorker(appContext: Context, workerParames: WorkerParameters)
    : CoroutineWorker(appContext, workerParames) {
    override suspend fun doWork(): Result {
        delay(1000)
        setForeground(getForegroundInfo())
        delay(10000)
        return Result.success() // 返回时会把通知栏关闭
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

        // 点击的时候调用 Intent ，这里使用取消。
        val cancel = "取消"
        val intent = WorkManager.getInstance(applicationContext)
            .createCancelPendingIntent(getId())

        val notification = NotificationCompat.Builder(applicationContext, id)
            .setContentTitle("OneTime")
            .setTicker("title")
            .setContentText("单次通知栏消息")
            .setSmallIcon(R.drawable.ic_notifications_active)
            .setOngoing(true) // 固定通知，使得无法被滑动关闭
            .addAction(android.R.drawable.ic_delete, cancel, intent)
            .build()

        val notificationId = 1
        return ForegroundInfo(notificationId, notification)
    }
}