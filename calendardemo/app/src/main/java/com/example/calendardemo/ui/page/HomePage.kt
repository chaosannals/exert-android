package com.example.calendardemo.ui.page

import android.provider.CalendarContract
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.calendardemo.ui.navigate

@Composable
fun HomePage() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Column (
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(text = CalendarContract.Calendars.CONTENT_URI.toString())
        Text(text = CalendarContract.Events.CONTENT_URI.toString())
        Text(text = CalendarContract.Reminders.CONTENT_URI.toString())
        Text(text = CalendarContract.Instances.CONTENT_URI.toString())

        Button(onClick = {
            coroutineScope.navigate("/calendar/index")
        }) {
            Text(text = "账号")
        }

        Button(onClick = {
            coroutineScope.navigate("/calendar/event")
        }) {
            Text(text = "事件")
        }
    }
}