package com.example.anidemo.ui

import android.content.res.Resources
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.LocalDateTime
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

inline val Int.ssp : TextUnit
    get() = run {
    return (this * U.ratio).sp
}

inline val Float.ssp : TextUnit
    get() = run {
    return (this * U.ratio).sp
}

inline val Double.ssp : TextUnit
    get() = run {
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

inline val Int.px2dp: Dp get() = run {
    return (this / U.density).dp
}
inline val Float.px2dp: Dp get() = run {
    return (this / U.density).dp
}

//inline val LocalDateTime.date : LocalDate get() = run {
//    return toLocalDate()
//}