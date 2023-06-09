package com.example.app24

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date


@SuppressLint("SimpleDateFormat")
object DateKit {
    val datetimeFormator: SimpleDateFormat by lazy {
        SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    }

    val datetimeYearMonthDateFormator: SimpleDateFormat by lazy {
        SimpleDateFormat("yyyy-MM-dd")
    }

    val datetimeMonthDayFormat: SimpleDateFormat by lazy {
        SimpleDateFormat("MM-dd")
    }

    val yyyyMMddFormat: SimpleDateFormat by lazy {
        SimpleDateFormat("yyyyMMdd")
    }
    val yyyyMMFormat: SimpleDateFormat by lazy {
        SimpleDateFormat("yyyyMM")
    }

    val datetimeYearMonthFormator: SimpleDateFormat by lazy {
        SimpleDateFormat("yyyy年MM月")
    }

    val datetimeYearFormator: SimpleDateFormat by lazy {
        SimpleDateFormat("yyyy年")
    }
    val datetimeMonthFormator: SimpleDateFormat by lazy {
        SimpleDateFormat("M月")
    }
}


inline val String.dt : Date
    get() = run {
        DateKit.datetimeFormator.parse(this)!!
    }

inline val Date.d2dt : String get() = run {
    DateKit.datetimeFormator.format(this)
}

inline val Date.d2md : String get() = run {
    DateKit.datetimeMonthDayFormat.format(this)
}

inline val Date.d2y : String get() = run {
    DateKit.datetimeYearFormator.format(this)
}

inline val Date.d2ym : String get() = run {
    DateKit.datetimeYearMonthFormator.format(this)
}

inline val Date.d2m : String get() = run {
    DateKit.datetimeMonthFormator.format(this)
}

inline val Date.d2ymd : String get() = run {
    DateKit.datetimeYearMonthDateFormator.format(this)
}

inline val Date.d2yyyyMMdd: String get() = run {
    DateKit.yyyyMMddFormat.format(this)
}

inline val Date.d2yyyyMM: String get() = run {
    DateKit.yyyyMMFormat.format(this)
}

@Suppress("deprecation")
fun dateOf(y: Int, m: Int, d: Int, h: Int, i: Int, s: Int): Date {
    return Date(y - 1900, m - 1, d, h, i, s)
}

@Suppress("deprecation")
fun dateOf(y: Int, m: Int, d: Int): Date {
    return Date(y - 1900, m - 1, d)
}

@Suppress("deprecation")
fun Date.de(other: Date) : Boolean {
    return this.year == other.year &&
            this.month == other.month &&
            this.date == other.date
}

@Suppress("deprecation")
inline val Date.dateOnly : Date
    get() = run {
        return Date(this.year, this.month, this.date)
    }

@Suppress("deprecation")
inline val Date.endOfDate : Date
    get() = run {
        return Date(this.year, this.month, this.date, 23, 59, 59)
    }

// 周一是第一天
inline val Date.firstOfWeek : Date
    get() = run {
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
inline val Date.endOfWeek : Date
    get() = run {
        val cd = Calendar.getInstance()
        cd.time = this.firstOfWeek
        cd.add(Calendar.DATE, 6)
        return cd.time
    }

@Suppress("deprecation")
inline val Date.firstOfMonth : Date
    get() = run {
        return Date(this.year, this.month, 1)
    }

inline val Date.endOfMonth : Date
    get() = run {
        val cd = Calendar.getInstance()
        cd.time = this.firstOfMonth
        cd.add(Calendar.MONTH, 1)
        cd.add(Calendar.DATE, -1)
        return cd.time
    }

@Suppress("deprecation")
inline val Date.firstOfYear : Date
    get() = run {
        return Date(this.year, 0, 1)
    }