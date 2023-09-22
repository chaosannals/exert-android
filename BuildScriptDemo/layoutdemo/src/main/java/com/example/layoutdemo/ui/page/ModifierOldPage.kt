package com.example.layoutdemo.ui.page

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.DrawModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// 1.5.0 之前的组合式修改器，此种结构复杂，性能低。
// 1.5.0 后官方组件将用新方式替换提高性能。
// 所以自定义的修改器最好也不要使用此种方式定义。

// 自定义修改器存储结构
class MyOldCustomModifier(
    private val color: Color,
): DrawModifier {
    override fun ContentDrawScope.draw() {
        drawRect(color=color)
        drawContent()
    }
}

// 组合式 remember 修改器信息
fun Modifier.myOldCustom(
    color: Color,
) = composed {
    val modifier = remember(color) {
        MyOldCustomModifier(color)
    }
    return@composed modifier
}

@Composable
fun ModifierOldPage() {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier.size(100.dp)
                .myOldCustom(Color.Red) // 使用
        ) {
            Text("12345612345678945678946789")
        }
    }
}

@Preview
@Composable
fun ModifierOldPagePreview() {
    ModifierOldPage()
}