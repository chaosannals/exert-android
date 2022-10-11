package com.example.jcmdemo.ui

import android.content.res.Resources
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.*
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
        SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
    }

    val datetimeMonthDayFormator: SimpleDateFormat by lazy {
        SimpleDateFormat("MM-dd", Locale.CHINA)
    }

    val datetimeYearMonthFormator: SimpleDateFormat by lazy {
        SimpleDateFormat("yyyy年MM月", Locale.CHINA)
    }

    val datetimeYearMonthDateFormator: SimpleDateFormat by lazy {
        SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
    }

    val datetimeYearFormator: SimpleDateFormat by lazy {
        SimpleDateFormat("yyyy年", Locale.CHINA)
    }
    val datetimeMonthFormator: SimpleDateFormat by lazy {
        SimpleDateFormat("M月", Locale.CHINA)
    }
}

inline val Int.sdp : Dp get() = run {
    return (this.toFloat() * U.ratio).dp
}

inline val Float.sdp : Dp get() = run {
    return (this * U.ratio).dp
}

inline val Double.sdp : Dp get() = run {
    return (this.toFloat() * U.ratio).dp
}

inline val Int.ssp : TextUnit get() = run {
    return (this * U.ratio).sp
}

inline val Float.ssp : TextUnit get() = run {
    return (this * U.ratio).sp
}

inline val Double.ssp : TextUnit get() = run {
    return (this * U.ratio).sp
}

inline val Int.si : Int get() = run {
    return (this * U.ratio * U.density).toInt()
}

inline val Float.sf : Float get() = run {
    return this * U.ratio * U.density
}

inline val Double.sd : Double get() = run {
    return this * U.ratio * U.density
}

inline val String.dt : Date? get() = run {
    U.datetimeFormator.parse(this)
}

inline val Date.d2dt : String get() = run {
    U.datetimeFormator.format(this)
}

inline val Date.d2md : String get() = run {
    U.datetimeMonthDayFormator.format(this)
}

inline val Date.d2y : String get() = run {
    U.datetimeYearFormator.format(this)
}

inline val Date.d2ym : String get() = run {
    U.datetimeYearMonthFormator.format(this)
}

inline val Date.d2m : String get() = run {
    U.datetimeMonthFormator.format(this)
}

inline val Date.d2ymd : String get() = run {
    U.datetimeYearMonthDateFormator.format(this)
}

fun Date.de(other: Date) : Boolean {
    return this.year == other.year &&
            this.month == other.month &&
            this.date == other.date
}

inline val Date.dateOnly : Date get() = run {
    return Date(this.year, this.month, this.date)
}

inline val Date.endOfDate : Date get() = run {
    return Date(this.year, this.month, this.date, 23, 59, 59)
}

// 周一是第一天
inline val Date.firstOfWeek : Date get() = run {
    val cd = Calendar.getInstance()
    cd.time = this
    cd.add(Calendar.DATE, cd.firstDayOfWeek - cd.get(Calendar.DAY_OF_WEEK))
    val sunday = cd.time
    if (!this.de(sunday)) {
        cd.add(Calendar.DATE, 1)
        return cd.time
    }
    cd.add(Calendar.DATE, -6)
    return cd.time
}

// 周日是最后一天
inline val Date.endOfWeek : Date get() = run {
    val cd = Calendar.getInstance()
    cd.time = this.firstOfWeek
    cd.add(Calendar.DATE, 6)
    return cd.time
}

inline val Date.firstOfMonth : Date get() = run {
    return Date(this.year, this.month, 1)
}

inline val Date.endOfMonth : Date get() = run {
    val cd = Calendar.getInstance()
    cd.time = this.firstOfMonth
    cd.add(Calendar.MONTH, 1)
    cd.add(Calendar.DATE, -1)
    return cd.time
}

inline val Date.firstOfYear : Date get() = run {
    return Date(this.year, 0, 1)
}