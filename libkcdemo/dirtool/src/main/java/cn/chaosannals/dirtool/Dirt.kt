package cn.chaosannals.dirtool

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import android.util.DisplayMetrics
import androidx.annotation.NonNull
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlin.math.floor

object Dirt {
    private val displayMetrics: DisplayMetrics by lazy {
        Resources.getSystem().displayMetrics
    }
    var designWidthDp: Dp = 375.dp
    var designHeightDp: Dp = 667.dp

    val density: Float by lazy {
        displayMetrics.density
    }

    val screenWidthPx: Int by lazy {
        displayMetrics.widthPixels
    }

    val screenHeightPx: Int by lazy {
        displayMetrics.heightPixels
    }

    // 一般真机屏幕的 Dp 是整数，预览设备不是整数。
    // 所以预览窗口向下取整（floor）会有误差
    // 但是真机如果不取整会和真机的整DP不符。
    val screenWidthDp: Dp by lazy {
        floor(screenWidthPx / density).dp
    }

    val screenHeightDp: Dp by lazy {
        floor(screenHeightPx / density).dp
    }

    // 基于宽度
    val ratio: Float = screenWidthDp / designWidthDp
}

// scale dp

inline val Int.sdp : Dp get() = run {
    return (this.toFloat() * Dirt.ratio).dp
}

inline val Float.sdp : Dp get() = run {
    return (this * Dirt.ratio).dp
}

inline val Double.sdp : Dp get() = run {
    return (this.toFloat() * Dirt.ratio).dp
}

// scale sp

inline val Int.ssp : TextUnit
    get() = run {
    return (this * Dirt.ratio).sp
}

inline val Float.ssp : TextUnit
    get() = run {
    return (this * Dirt.ratio).sp
}

inline val Double.ssp : TextUnit
    get() = run {
    return (this * Dirt.ratio).sp
}

// scale dp to px （名字有点迷惑，但是也想不出比较好的命名）
// 同样使用设计稿的 dp 值，但是得到的是给需要像素值的方法使用的。

inline val Int.spx : Int get() = run {
    return (this * Dirt.ratio * Dirt.density).toInt()
}

inline val Float.spx : Float get() = run {
    return this * Dirt.ratio * Dirt.density
}

inline val Double.spx : Double get() = run {
    return this * Dirt.ratio * Dirt.density
}

fun Context.ensurePermission(permission: String, requestCode: Int) {
    val sp = ContextCompat.checkSelfPermission(this, permission)
    if (sp != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(
            this as Activity,
            arrayOf(permission),
            requestCode // 自定标识
        )
    }
}

@Preview
@Composable
fun DirtInfoPreview() {
    DirtPreview() {
        Column (
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text="Design: (${Dirt.designWidthDp}, ${Dirt.designHeightDp})",
            )
            Text(
                text="Screen: (${Dirt.screenWidthPx}, ${Dirt.screenHeightPx}) px",
            )
            Text(
                text="Screen: (${Dirt.screenWidthDp}, ${Dirt.screenHeightDp})",
            )
            Text(
                text="ratio: ${Dirt.ratio} density: ${Dirt.density}",
            )
            Text(
                text="375.sdp = ${375.sdp}, 375f.spx = ${375f.spx})",
            )
        }
    }
}