package com.example.app24.ui.page.demo

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.app24.ui.LocalNavController

/// BackHandler 以更深入的层为最终的处理。
/// 只有一个 BackHandler 起效。
/// 只要 通过 enable=false 或 不 compose 深层级别的 BackHandler 浅层的 BackHandler 就会起效。

@Composable
fun BackHandleLv1() {
    var isEnable by remember { mutableStateOf(true) }
    var isShowLv2 by remember { mutableStateOf(true) }
    var count by remember {
        mutableStateOf(0)
    }

    Column() {
        BackHandler(isEnable) {
            count += 1
        }
        Row() {
            Text("Lv1: $count")
            Button(onClick = { isEnable = !isEnable }) {
                Text("$isEnable")
            }

            Button(onClick = { isShowLv2=!isShowLv2 }) {
                Text("Lv2 Show: $isShowLv2")
            }
        }
        if (isShowLv2) {
            BackHandleLv2()
            BackHandleLv2Two()
        }
    }
}

@Composable
fun BackHandleLv2() {
    var isEnable by remember { mutableStateOf(true) }
    var count by remember {
        mutableStateOf(0)
    }

    Row() {
        Text("Lv2: $count")
        BackHandler(isEnable) {
            count += 1
        }
        Button(onClick = { isEnable = !isEnable }) {
            Text("$isEnable")
        }
    }
}

@Composable
fun BackHandleLv2Two() {
    var isEnable by remember { mutableStateOf(true) }
    var count by remember {
        mutableStateOf(0)
    }

    Row() {
        Text("Lv2Two: $count")
        BackHandler(isEnable) {
            count += 1
        }
        Button(onClick = { isEnable = !isEnable }) {
            Text("$isEnable")
        }
    }
}

@Composable
fun BackHandlePage() {
    val navController = LocalNavController.current
    var isEnable by remember { mutableStateOf(true) }
    var count by remember {
        mutableStateOf(0)
    }

    Column(
        modifier = Modifier.statusBarsPadding(),
    ) {
        Row() {
            Text("Page: $count")
            BackHandler(isEnable) {
                count += 1
            }
            Button(onClick = { isEnable = !isEnable }) {
                Text("$isEnable")
            }
        }
        BackHandleLv1()
        Button(onClick = { navController.navigateUp() }) {
            Text("返回")
        }
    }
}