package com.example.calendardemo

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.icu.util.Calendar
import android.net.Uri
import android.provider.CalendarContract
import java.util.Date

private const val CALENDER_URL = "content://com.android.calendar/calendars"
private const val CALENDER_EVENT_URL = "content://com.android.calendar/events"
private const val CALENDER_REMINDER_URL = "content://com.android.calendar/reminders"

data class CalendarAccount(
    val id: Int,
    val name: String,
    val accountName: String,
    val accountType: String,
    val displayName: String,
)

data class CalendarEvent(
    val startAt: Date,
    val endAt: Date,
    val title: String?,
    val description: String?,
    val id: Long?=null,
)

fun Context.getDefaultCalendarAccount(): CalendarAccount? {
    val cursor = contentResolver.query(Uri.parse(CALENDER_URL), null, null, null, null)
    return cursor?.use {
        if (it.count > 0) {
            it.moveToFirst()
            val idIndex = it.getColumnIndex(CalendarContract.Calendars._ID)
            val nameIndex = it.getColumnIndex(CalendarContract.Calendars.NAME)
            val accountNameIndex = it.getColumnIndex(CalendarContract.Calendars.ACCOUNT_NAME)
            val accountTypeIndex = it.getColumnIndex(CalendarContract.Calendars.ACCOUNT_TYPE)
            val displayNameIndex = it.getColumnIndex(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME)
            val id = it.getInt(idIndex)
            val name = it.getString(nameIndex)
            val accountName = it.getString(accountNameIndex)
            val accountType = it.getString(accountTypeIndex)
            val displayName = it.getString(displayNameIndex)
            CalendarAccount(id, name, accountName, accountType, displayName)
        } else null
    }
}

fun Context.addCalendarEvent(param: CalendarEvent): Uri? {
    return getDefaultCalendarAccount()?.run {
        Calendar.getInstance().let {
            val event = ContentValues().apply {
                put(CalendarContract.Events.TITLE, param.title)
                put(CalendarContract.Events.DESCRIPTION, param.description)
                put(CalendarContract.Events.CALENDAR_ID, id)
                put(CalendarContract.Events.DTSTART, param.startAt.time)
                put(CalendarContract.Events.DTEND, param.endAt.time)
                put(CalendarContract.Events.HAS_ALARM, 1) // 闹钟
                put(CalendarContract.Events.EVENT_TIMEZONE, "Asia/Shanghai")
            }
            contentResolver.insert(Uri.parse(CALENDER_EVENT_URL), event)
        }
    }
}

fun Context.addCalendarReminder(event: Uri, previousMinutes: Int=10): Uri? {
    val reminder = ContentValues().apply {
        put(CalendarContract.Reminders.EVENT_ID, ContentUris.parseId(event))
        put(CalendarContract.Reminders.MINUTES, previousMinutes) // 提前 previousMinutes 分钟提醒
        put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT) // 提醒方式
    }
    return contentResolver.insert(Uri.parse(CALENDER_REMINDER_URL), reminder)
}

fun Context.listCalendarEvent(): List<CalendarEvent> {
    return contentResolver.query(Uri.parse(CALENDER_EVENT_URL), null, null, null,null)?.use {
        mutableListOf<CalendarEvent>().apply {
            if (it.count > 0) {
                while (it.moveToNext()) {
                    val idIndex = it.getColumnIndex(CalendarContract.Events._ID)
                    val startAtIndex = it.getColumnIndex(CalendarContract.Events.DTSTART)
                    val endAtIndex = it.getColumnIndex(CalendarContract.Events.DTEND)
                    val titleIndex = it.getColumnIndex(CalendarContract.Events.TITLE)
                    val descriptionIndex = it.getColumnIndex(CalendarContract.Events.DESCRIPTION)
                    val id = it.getLong(idIndex)
                    val startAt = it.getLong(startAtIndex)
                    val endAt = it.getLong(endAtIndex)
                    val title = it.getString(titleIndex)
                    val description = it.getString(descriptionIndex)
                    add(CalendarEvent(Date(startAt), Date(endAt), title, description, id))
                }
            }
        }
    } ?: listOf()
}

// 删除不是即时的，要等一段时间后才会起效。
fun Context.deleteCalendarEvent(id: Long): Int {
    val uri = ContentUris.withAppendedId(Uri.parse(CALENDER_EVENT_URL), id)
    return contentResolver.delete(uri, null, null)
}