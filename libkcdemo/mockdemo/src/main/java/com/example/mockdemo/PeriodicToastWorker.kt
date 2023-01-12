package com.example.mockdemo

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import java.time.LocalDateTime

class PeriodicToastWorker(appContext: Context,
                          params: WorkerParameters
) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        val now = LocalDateTime.now()
        Toast.makeText(applicationContext, "${now}", Toast.LENGTH_SHORT).show()
        setForeground(getForegroundInfo())
        return Result.success()
    }

    override suspend fun getForegroundInfo(): ForegroundInfo {
        val id = "Periodic Demo Toast Work"
        val name: CharSequence = "Periodic Demo Toast Name"
        val description = "Mock Demo Periodic Demo Toast。"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(id, name, importance)
        channel.description = description

        val manager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(applicationContext, id)
            .setTicker("状态栏闪现消息")
            .setContentTitle("周期通知")
            .setContentText("消息")
            .setAutoCancel(true)
            .setSmallIcon(R.drawable.ic_notifications_active)
            .build()

        return ForegroundInfo(1, notification)
    }
}