package com.example.anidemo.ui

import android.content.res.Resources
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import kotlin.math.floor

object U {
    val density: Float by lazy {
        Resources.getSystem().displayMetrics.density
    }

    val displayWidth: Int by lazy {
        Resources.getSystem().displayMetrics.widthPixels
    }

    val displayHeight: Int by lazy {
        Resources.getSystem().displayMetrics.heightPixels
    }

    val displayHdp: Dp by lazy {
        floor(displayHeight / density).dp
    }

    val displayDp: Dp by lazy {
        floor(displayWidth / density).dp
    }

    val designInt : Int = 375
    val designDp: Dp = designInt.dp
    val ratio: Float = displayDp / designDp

    val datetimeFormator: SimpleDateFormat by lazy {
        SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    }

    val datetimeMonthDayFormator: SimpleDateFormat by lazy {
        SimpleDateFormat("MM-dd")
    }

    val datetimeYearMonthFormator: SimpleDateFormat by lazy {
        SimpleDateFormat("yyyy年MM月")
    }

    val datetimeYearMonthDateFormator: SimpleDateFormat by lazy {
        SimpleDateFormat("yyyy-MM-dd")
    }

    val datetimeYearFormator: SimpleDateFormat by lazy {
        SimpleDateFormat("yyyy年")
    }
    val datetimeMonthFormator: SimpleDateFormat by lazy {
        SimpleDateFormat("M月")
    }
}

inline val Int.px2dp: Dp get() = run {
    return (this / U.density).dp
}