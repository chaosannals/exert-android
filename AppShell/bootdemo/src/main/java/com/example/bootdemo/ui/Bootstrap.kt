package com.example.bootdemo.ui

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import androidx.compose.animation.core.withInfiniteAnimationFrameMillis
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.bootdemo.MainActivity
import com.example.bootdemo.R
import kotlinx.coroutines.delay

fun Modifier.drawSome(
    image:  ImageBitmap,
    duration: Float = 2.0f,
): Modifier = composed {
    // 用于动画的递增时间
    val time by produceState(0f) {
        while (true) {
            withInfiniteAnimationFrameMillis {// it 是毫秒
                value = it / 1000f // 单位 秒
            }
        }
    }

    Modifier
        .graphicsLayer {
            rotationY = (time % duration) * 360f
        }
        .drawWithCache {
        val center = Offset(size.width / 2, size.height / 2)
        onDrawBehind {
            withTransform({
                rotate((time % duration) * 360f, center) // 2D 旋转角度
                // 会提示安卓不能支持任意变换
//                transform(
//                    matrix = Matrix(
//                        values = floatArrayOf(
//                            1.0f,0.0f,0.0f, 0.0f,
//                            0.0f,1.0f,0.0f, 0.0f,
//                            0.0f,0.0f,1.0f, 0.0f,
//                            1.0f,1.0f,1.0f, 0.0f,
//                        ),
//                    )
//                )
            }) {
                drawCircle(
                    color = Color.Cyan,
                    center = center,
                    radius = ((time + duration - 0.1f) % duration) / duration * size.width / 2,
                )
                drawLine(
                    color = Color.Black,
                    start = Offset.Zero,
                    end = Offset(size.width, size.height),
                )
                drawImage(
                    image = image,
                    topLeft = center.minus(Offset(image.width / 2.0f, image.height / 2.0f))
                )
            }
        }
    }
}

@Composable
fun Bootstrap() {
    val context = LocalContext.current
    val image = ImageBitmap.imageResource(R.drawable.boot)

    LaunchedEffect(Unit) {
        delay(400)
        val intent = Intent()
        intent.setClass(context, MainActivity::class.java)
        context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(context as Activity).toBundle())
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .drawSome(image)
    ) {

    }
}

@Preview
@Composable
fun BootstrapPreview() {
    Bootstrap()
}