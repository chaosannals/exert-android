package com.example.calendardemo

import java.text.SimpleDateFormat
import java.util.Date

private val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
val String.date get(): Date? = run {
    df.parse(this)
}
val Date.text get(): String = run {
    df.format(this)
}