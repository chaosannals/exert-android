package com.example.calendardemo.ui.page

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.calendardemo.CalendarAccount
import com.example.calendardemo.CalendarEvent
import com.example.calendardemo.addCalendarEvent
import com.example.calendardemo.addCalendarReminder
import com.example.calendardemo.deleteCalendarEvent
import com.example.calendardemo.getDefaultCalendarAccount
import com.example.calendardemo.listCalendarEvent
import java.text.SimpleDateFormat
import java.util.Date

private val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
private val String.date get(): Date? = run {
    df.parse(this)
}
private val Date.text get(): String = run {
    df.format(this)
}

@Composable
fun HomePage() {
    val context = LocalContext.current
    var account: CalendarAccount? by remember {
        mutableStateOf(null)
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
    ) {
        if (it.values.reduce { acc, b -> acc && b }) {
            account = context.getDefaultCalendarAccount()
        }
    }

    LaunchedEffect(permissionLauncher) {
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.READ_CALENDAR,
                Manifest.permission.WRITE_CALENDAR,
            )
        )
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
    ) {
        var startAt by remember {
            mutableStateOf("2024-07-09 17:08:00")
        }
        var endAt by remember {
            mutableStateOf("2024-07-10  17:00:00")
        }
        TextField(value = startAt, onValueChange = {startAt=it})
        TextField(value = endAt, onValueChange = {endAt=it})

        account?.let {
            Text(text = "${it.id}")
            Text(text = it.name)
            Text(text = it.accountName)
            Text(text = it.accountType)
            Text(text = it.displayName)
            Button(onClick = {
                context.run {
                    addCalendarEvent(CalendarEvent(startAt.date!!, endAt.date!!, "test", "测试"))?.let {
                        addCalendarReminder(it)
                    }
                }
            }) {
                Text("添加事件")
            }

            var events: List<CalendarEvent> by remember {
                mutableStateOf(listOf())
            }

            Button(onClick = {
                context.run {
                    events = listCalendarEvent()
                }
            }) {
                Text("获取事件")
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                state = rememberLazyListState()
            ) {
                itemsIndexed(events) {i, event ->
                    Column {
                        Row(
                            modifier = Modifier.clickable {
                                context.run {
                                    val c = deleteCalendarEvent(event.id!!)
                                    Toast.makeText(
                                        this,
                                        "delete: ${event.id} count: ${c}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    events = listCalendarEvent()
                                }
                            }
                        ) {
                            Text(text = "${i} [${event.id}]")
                            Text(text = event.startAt.text)
                            Text(text = event.endAt.text)
                        }
                        Row {
                            Text(text = event.title ?: "-")
                        }
                        Row {
                            Text(text = event.description ?: "+")
                        }
                    }
                }
            }
        }
    }
}