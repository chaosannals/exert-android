package com.example.appimop.ui

import android.content.res.Resources
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import kotlin.math.floor

object Design {
    val density: Float by lazy {
        Resources.getSystem().displayMetrics.density
    }

    val displayWidth: Int by lazy {
        Resources.getSystem().displayMetrics.widthPixels
    }

    val displayHeight: Int by lazy {
        Resources.getSystem().displayMetrics.heightPixels
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
        return (this.toFloat() * Design.ratio).dp
    }

inline val Float.sdp : Dp
    get() = run {
        return (this * Design.ratio).dp
    }

inline val Double.sdp : Dp
    get() = run {
        return (this.toFloat() * Design.ratio).dp
    }

inline val Int.ssp : TextUnit
    get() = run {
        return (this * Design.ratio).sp
    }

inline val Float.ssp : TextUnit
    get() = run {
        return (this * Design.ratio).sp
    }

inline val Double.ssp : TextUnit
    get() = run {
        return (this * Design.ratio).sp
    }

inline val Int.si : Int get() = run {
    return (this * Design.ratio * Design.density).toInt()
}

inline val Float.sf : Float get() = run {
    return this * Design.ratio * Design.density
}

inline val Double.sd : Double get() = run {
    return this * Design.ratio * Design.density
}

inline val Int.dp2px : Float get() = run {
    return this * Design.ratio * Design.density
}

inline val Float.dp2px : Float get() = run {
    return this * Design.ratio * Design.density
}

inline val Double.dp2px : Float get() = run {
    return (this * Design.ratio * Design.density).toFloat()
}

inline val Int.px2dp: Dp
    get() = run {
    return (this / Design.density / Design.ratio).dp
}
inline val Float.px2dp: Dp
    get() = run {
    return (this / Design.density / Design.ratio).dp
}
inline val Double.px2dp: Dp
    get() = run {
    return (this / Design.density / Design.ratio).dp
}

fun Modifier.shadow2(
    color: Color = Color(0xFFF2F7FA),
    alpha: Float = 1f,
    cornersRadius: Dp = 0.dp,
    shadowBlurRadius: Dp = 0.5.dp,
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

fun DrawScope.drawShadow(
    topLeft: Offset,
    size: Size,
    offset: Offset,
    color: Color = Color(0x44000000),
    cornersRadius: Dp = 5.dp,
    shadowBlurRadius: Dp = 0.5.dp,
) = drawIntoCanvas {
    val shadowColor = color.toArgb()
    val transparentColor = color.copy(alpha = 0f).toArgb()
    val blur = shadowBlurRadius.toPx()

    val paint = Paint()
    val frameworkPaint = paint.asFrameworkPaint()
    frameworkPaint.color = transparentColor
    frameworkPaint.setShadowLayer(
        blur,
        topLeft.x,
        topLeft.y,
        shadowColor
    )
    it.drawRoundRect(
        0f,
        0f,
        size.width + offset.x,
        size.height + offset.y,
        cornersRadius.toPx(),
        cornersRadius.toPx(),
        paint
    )
}

@Composable
fun DesignPreview(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.()->Unit
) {
    CompositionLocalProvider(
        LocalNavController provides rememberNavController()
    ) {
        Box(modifier = modifier) {
            content()
        }
    }
}