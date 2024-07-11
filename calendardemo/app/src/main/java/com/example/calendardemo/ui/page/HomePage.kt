package com.example.calendardemo.ui.page

import android.provider.CalendarContract
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.calendardemo.CalendarContentUrls
import com.example.calendardemo.listCalendarContentUrls
import com.example.calendardemo.ui.navigate

@Composable
fun HomePage() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var calendarContentUrls: CalendarContentUrls? by remember {
        mutableStateOf(null)
    }

    LaunchedEffect(Unit) {
        calendarContentUrls = listCalendarContentUrls()
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
    ) {
        calendarContentUrls?.run {
            Text(text = calendars)
            Text(text = events)
            Text(text = reminders)
            Text(text = instances)
        }

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

        Button(onClick = {
            coroutineScope.navigate("/web")
        }) {
            Text(text = "GOOGLE")
        }
    }
}

@Preview
@Composable
fun HomePagePreview() {
    HomePage()
}