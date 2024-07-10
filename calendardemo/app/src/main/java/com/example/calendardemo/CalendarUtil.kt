package com.example.calendardemo

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.graphics.Color
import android.icu.util.Calendar
import android.net.Uri
import android.provider.CalendarContract
import java.util.Date
import java.util.TimeZone

data class CalendarAccount(
    val id: Long,
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

fun Context.addCalendarAccount(
    name: String,
    accountName: String,
    accountType: String="com.example", // 自己定义
    displayName: String,
): CalendarAccount? {
    val values = ContentValues().apply {
        put(CalendarContract.Calendars.NAME, name)
        put(CalendarContract.Calendars.ACCOUNT_NAME, accountName)
        put(CalendarContract.Calendars.ACCOUNT_TYPE, accountType)
        put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, displayName)
        put(CalendarContract.Calendars.VISIBLE, 1)
        put(CalendarContract.Calendars.CALENDAR_COLOR, Color.BLUE)
        put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, CalendarContract.Calendars.CAL_ACCESS_OWNER)
        put(CalendarContract.Calendars.SYNC_EVENTS, 1) // 同步到系统
        put(CalendarContract.Calendars.CALENDAR_TIME_ZONE, TimeZone.getDefault().id)
        put(CalendarContract.Calendars.OWNER_ACCOUNT, accountName)
        put(CalendarContract.Calendars.CAN_ORGANIZER_RESPOND, 0)
    }
    val uri = CalendarContract.Calendars.CONTENT_URI.buildUpon()
        .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
        .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, accountName)
        .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, accountType)
        .build()
    return contentResolver.insert(uri, values)?.let {
        CalendarAccount(
            id = ContentUris.parseId(it),
            name = name,
            accountName = accountName,
            accountType = accountType,
            displayName = displayName
        )
    }
}

fun Context.deleteCalendarAccount(id: Long): Int {
    val uri = ContentUris.withAppendedId(CalendarContract.Calendars.CONTENT_URI, id)
    return contentResolver.delete(uri, null, null)
}

private fun Cursor.getCalendarAccount(): CalendarAccount {
    val idIndex = getColumnIndex(CalendarContract.Calendars._ID)
    val nameIndex = getColumnIndex(CalendarContract.Calendars.NAME)
    val accountNameIndex = getColumnIndex(CalendarContract.Calendars.ACCOUNT_NAME)
    val accountTypeIndex = getColumnIndex(CalendarContract.Calendars.ACCOUNT_TYPE)
    val displayNameIndex = getColumnIndex(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME)
    val id = getLong(idIndex)
    val name = getString(nameIndex)
    val accountName = getString(accountNameIndex)
    val accountType = getString(accountTypeIndex)
    val displayName = getString(displayNameIndex)
    return CalendarAccount(id, name, accountName, accountType, displayName)
}

fun Context.getDefaultCalendarAccount(): CalendarAccount? {
    val cursor = contentResolver.query(CalendarContract.Calendars.CONTENT_URI, null, null, null, null)
    return cursor?.use {
        if (it.count > 0) {
            it.moveToFirst()
            it.getCalendarAccount()
        } else null
    }
}

fun Context.listCalendarAccount(): List<CalendarAccount> {
    val cursor = contentResolver.query(CalendarContract.Calendars.CONTENT_URI, null, null, null, null)
    return cursor?.use {
        mutableListOf<CalendarAccount>().apply {
            while (it.moveToNext()) {
                add(it.getCalendarAccount())
            }
        }
    } ?: listOf()
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
            contentResolver.insert(CalendarContract.Events.CONTENT_URI, event)
        }
    }
}

fun Context.addCalendarReminder(event: Uri, previousMinutes: Int=10): Uri? {
    val reminder = ContentValues().apply {
        put(CalendarContract.Reminders.EVENT_ID, ContentUris.parseId(event))
        put(CalendarContract.Reminders.MINUTES, previousMinutes) // 提前 previousMinutes 分钟提醒
        put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT) // 提醒方式
    }
    return contentResolver.insert(CalendarContract.Reminders.CONTENT_URI, reminder)
}

fun Context.listCalendarEvent(): List<CalendarEvent> {
    return contentResolver.query(CalendarContract.Events.CONTENT_URI, null, null, null,null)?.use {
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
    val uri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, id)
    return contentResolver.delete(uri, null, null)
}