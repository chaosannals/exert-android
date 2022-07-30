package com.example.jcm3demo.ui

import android.content.res.Resources
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.floor

object U {
    val displayDp: Dp by lazy {
        val sdm = Resources.getSystem().displayMetrics
        val w = sdm.widthPixels / sdm.density
        floor(w).dp
    }
    val designDp: Dp = 375.dp
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