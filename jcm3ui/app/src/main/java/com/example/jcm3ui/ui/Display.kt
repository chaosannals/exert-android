package com.example.jcm3ui.ui

import android.content.res.Resources
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.floor

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

// scale dp 自动适应屏幕的 缩放 dp
inline val Int.sdp : Dp get() = run {
    return (this.toFloat() * ratio).dp
}

// scale dp 自动适应屏幕的 缩放 dp
inline val Float.sdp : Dp get() = run {
    return (this * ratio).dp
}

// scale dp 自动适应屏幕的 缩放 dp
inline val Double.sdp : Dp get() = run {
    return (this.toFloat() * ratio).dp
}

// scale sp 自动适应屏幕的 缩放 sp
inline val Int.ssp : TextUnit
    get() = run {
        return (this * ratio).sp
    }

// scale sp 自动适应屏幕的 缩放 sp
inline val Float.ssp : TextUnit
    get() = run {
    return (this * ratio).sp
}

// scale sp 自动适应屏幕的 缩放 sp
inline val Double.ssp : TextUnit
    get() = run {
    return (this * ratio).sp
}

// scale int 自动适应屏幕的 缩放 int
inline val Int.si : Int get() = run {
    return (this * ratio * density).toInt()
}

// scale float 自动适应屏幕的 缩放 float
inline val Float.sf : Float get() = run {
    return this * ratio * density
}

// scale double 自动适应屏幕的 缩放 double
inline val Double.sd : Double get() = run {
    return this * ratio * density
}

inline val Float.px2dp: Dp get() = run {
    return (this / density / ratio).dp
}
