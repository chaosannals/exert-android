package com.example.bootdemo.ui.page.drawing

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun WheelPage() {
    val tags = remember {
        mutableStateListOf(
            "0",
            "1",
            "2",
            "3",
            "4",
            "5",
            "6",
            "7",
            "8",
            "9",
            "10",
            "11",
            "12",
            "13",
            "14",
            "15",
            "16",
            "17",
            "18",
            "19",
            "20",
            "21",
            "22",
            "23",
        )
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize(),
    ) {
        val count = remember { 30 } // 一圈的个数
        val pi2 = remember {PI * 2} // 一周2pi
        val start by remember(count) {
            derivedStateOf { count / 4 } // 偏转 90° 正对着
        }
        val radius by remember(count) {
            derivedStateOf { count * 10f }
        }
        val radiusHalf by remember(radius) {
            derivedStateOf { radius / 8 }
        }

        tags.forEachIndexed { i, tag ->
            val radian = remember {
                ((i + start) * pi2 / count).toFloat()
            }
            val s by remember(radian) {
                derivedStateOf { sin(radian) }
            }
            if (s > 0) {
                Text(
                    text = tag,
                    modifier = Modifier
                        .graphicsLayer {
                            // TODO 没有 直接的 3D 函数（没有 translationZ），
                            // 只能整体转轴，设置不了中心，只能靠模拟，相机没有使用透视矩阵所以没透视。
                            // 有 rotationZ 又不给 translationZ （应该是因为用的正交矩阵，正交 Z 意义不大），缺斤短两。。
                            // 正交矩阵的相机 3D 看着有点怪。
//                        translationX = -sin(radian) * radiusHalf
                            translationY = -cos(radian) * radius
                            scaleX = 1f + sin(radian) * 0.5f
                            scaleY = 1f + sin(radian) * 0.5f
                            rotationX = (cos(radian) / pi2).toFloat() * 360f
                        }
                )
            }
        }
    }
}

@Preview
@Composable
fun WheelPagePreview() {
    WheelPage()
}