package com.example.mockdemo

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.work.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.jetty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

class MockServerWorker(appContext: Context, workerParames: WorkerParameters)
    : CoroutineWorker(appContext, workerParames){
    override suspend fun doWork(): Result {
        setForegroundAsync(createForegroundInfo("progress"))
        return Result.success()
    }

    private fun createForegroundInfo(progress: String): ForegroundInfo {
        val id = "applicationContext.getString(R.string.notification_channel_id)"
        val title = "applicationContext.getString(R.string.notification_title)"
        val cancel = "applicationContext.getString(R.string.cancel_download)"
        // This PendingIntent can be used to cancel the worker
        val intent = WorkManager.getInstance(applicationContext)
            .createCancelPendingIntent(getId())

        // Create a Notification channel if necessary
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel()
        }

        val notification = NotificationCompat.Builder(applicationContext, id)
            .setContentTitle(title)
            .setTicker(title)
            .setContentText(progress)
//            .setSmallIcon(R.drawable.ic_work_notification)
            .setSmallIcon(R.drawable.ic_notifications_active)
            .setOngoing(true)
            // Add the cancel action to the notification which can
            // be used to cancel the worker
            .addAction(android.R.drawable.ic_delete, cancel, intent)
            .build()

        val notificationId = 1
        return ForegroundInfo(notificationId, notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        // Create a Notification channel
    }

    suspend fun serve() {
        embeddedServer(Jetty, port=18080) {
            routing {
                get("/") {
                    call.respondText { "Hello" }
                }
            }
        }.start(wait = false)
    }
}