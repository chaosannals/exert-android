package com.example.app24.ui.page.demo

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import com.example.app24.ui.DesignPreview
import com.example.app24.ui.sdp


/// Dialog 下的第一个子组件，显示后 大小被锁死不能改变大小,
/// 解决方案1是：多套一层，第一个子组件撑满整个布局
/// 解决方案2是： 改变大小后先隐藏再显示，会刷新。
@Composable
fun Dialog4Page() {
    var isShow1 by remember() {
        mutableStateOf(false)
    }
    var isShow2 by remember() {
        mutableStateOf(false)
    }
    var size1 by remember {
        mutableStateOf(100.sdp)
    }
    var size2 by remember {
        mutableStateOf(100.sdp)
    }
    Column(
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxSize()
    ) {
        Button(onClick = { isShow1 = true }) {
            Text("弹窗1")
        }
        Button(onClick = { isShow2 = true }) {
            Text("弹窗2")
        }
    }

    // 第一个窗口，子组件即使改变 height1 也不会改变大小。
    // 关闭后显示，他会是加大后的大小。
    if (isShow1) {
        Dialog(
            onDismissRequest = { isShow1 = false },
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(size1)
                    .background(Color.Blue)
                    .clickable {
                        size1 += 10.sdp
                    }
            ) {
                Text(
                    text="${size1.value}"
                )
            }
        }
    }

    // 多套了一层 全撑大的，之后的组件在里面就可以改变大小了。
    if (isShow2) {
        Dialog(
            onDismissRequest = { isShow2 = false },
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize(),
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(size2)
                        .background(Color.Red)
                        .clickable {
                            size2 += 10.sdp
                        }
                ) {
                    Text(
                        text="${size2.value}"
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun Dialog4PagePreview() {
    DesignPreview {
        Dialog4Page()
    }
}