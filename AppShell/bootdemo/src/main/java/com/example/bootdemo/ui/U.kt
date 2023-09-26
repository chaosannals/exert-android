package com.example.bootdemo.ui

import android.app.Activity
import android.content.pm.PackageManager
import android.content.res.Resources
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlin.math.floor
import kotlin.math.log10

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

inline val Int.sdp : Dp
    get() = run {
        return (this.toFloat() * U.ratio).dp
    }

inline val Float.sdp : Dp
    get() = run {
        return (this * U.ratio).dp
    }

inline val Double.sdp : Dp
    get() = run {
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

inline val Int.dp2px : Float get() = run {
    return this * U.ratio * U.density
}

inline val Float.dp2px : Float get() = run {
    return this * U.ratio * U.density
}

inline val Double.dp2px : Float get() = run {
    return (this * U.ratio * U.density).toFloat()
}

inline val Int.px2dp: Dp get() = run {
    return (this / U.density / U.ratio).dp
}
inline val Float.px2dp: Dp get() = run {
    return (this / U.density / U.ratio).dp
}
inline val Double.px2dp: Dp get() = run {
    return (this / U.density / U.ratio).dp
}

fun Modifier.shadow2(
    color: Color = Color(0xFFF2F7FA),
    alpha: Float = 1f,
    cornersRadius: Dp = 0.dp,
    shadowBlurRadius: Dp = 0.dp,
    offsetY: Dp = 0.dp,
    offsetX: Dp = 0.dp
) = drawBehind {

    val shadowColor = color.copy(alpha = alpha).toArgb()
    val transparentColor = color.copy(alpha = 0f).toArgb()

    drawIntoCanvas {
        val paint = Paint()
        val frameworkPaint = paint.asFrameworkPaint()
        frameworkPaint.color = transparentColor
        frameworkPaint.setShadowLayer(
            shadowBlurRadius.toPx(),
            offsetX.toPx(),
            offsetY.toPx(),
            shadowColor
        )
        it.drawRoundRect(
            0f,
            0f,
            this.size.width,
            this.size.height,
            cornersRadius.toPx(),
            cornersRadius.toPx(),
            paint
        )
    }
}

// 检查权限，无则申请。
fun ensurePermit(context: Activity, permission: String, code: Int = 1) {
    val sp = ContextCompat.checkSelfPermission(
        context,
        permission
    )
    if (sp != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(
            context,
            arrayOf(permission),
            code // 自定标识
        )
    }
}

fun ByteArray.toHex(): String = joinToString(separator = "") {
    "%02x".format(it)
}

fun Int.sizeText(): String {
    val lv = log10(this.toFloat())
    return when (lv) {
        in 0f..3f -> "${this}B"
        in 3f..6f -> "${this / 1000f}KB"
        in 6f..9f -> "${this / 1000000f}MB"
        in 9f..12f -> "${this / 1000000000f}GB"
        else -> "NaN"
    }
}

@Composable
fun Modifier.click(onClick: () -> Unit) = clickable(
    indication = null,
    interactionSource = remember { MutableInteractionSource() },
    onClick =  onClick,
)