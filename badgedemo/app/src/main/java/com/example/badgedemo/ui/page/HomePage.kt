package com.example.badgedemo.ui.page

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.NotificationCompat
import com.example.badgedemo.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.reflect.Field

@Composable
fun HomePage() {
    val context = LocalContext.current
    var nid by remember {
        mutableStateOf(100)
    }
    var tip by remember {
        mutableStateOf("")
    }

    val coroutineScope = rememberCoroutineScope()

    Column (
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            text="MIUI6-11 ($nid) $tip",
            modifier = Modifier
                .clickable {
                    coroutineScope.launch {
                        Toast.makeText(context, "10 秒后发送", Toast.LENGTH_SHORT).show()
                        delay(10000)
                        try {
                            val manager =
                                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                            val cid = "badgerDemoNotifyChannelId"
                            val name: CharSequence = "BadgerDemoNotification"
                            val description = "BadgerDemoNotification"
                            val importance = NotificationManager.IMPORTANCE_DEFAULT
                            val channel = NotificationChannel(cid, name, importance)
                            channel.description = description
                            manager.createNotificationChannel(channel)

                            val notification = NotificationCompat.Builder(context, cid)
                                .setContentTitle("")
                                .setContentText("")
                                .setSmallIcon(R.drawable.ic_launcher_foreground)
                                .build()
                            val field = notification.javaClass.getDeclaredField("extraNotification")
                            val extraNotification = field.get(notification)
                            val method = extraNotification.javaClass.getDeclaredMethod(
                                "setMessageCount",
                                Int::class.java
                            )
                            method.invoke(extraNotification, 4)
                            manager.notify(nid++, notification)
                        } catch (e: Exception) {
                            tip = (e.message ?: "no error message.") + e.stackTraceToString()
                        }
                    }
                }
        )

        Text(
            text="MIUI 12",
            modifier = Modifier
                .clickable {
                    coroutineScope.launch {
                        Toast.makeText(context, "10 秒后发送", Toast.LENGTH_SHORT).show()
                        delay(10000)
                        val manager =
                            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                        val cid = "badgerDemoNotifyChannelId"
                        val name: CharSequence = "BadgerDemoNotification"
                        val description = "BadgerDemoNotification"
                        val importance = NotificationManager.IMPORTANCE_DEFAULT
                        val channel = NotificationChannel(cid, name, importance)
                        channel.description = description
                        manager.createNotificationChannel(channel)

                        val notification = NotificationCompat.Builder(context, cid)
                            .setContentTitle("标题")
                            .setContentText("内容")
                            .setSmallIcon(R.drawable.ic_launcher_foreground)
                            .setNumber(14)
                            .build()
                        manager.notify(nid++, notification)
                    }
                }
        )
    }
}