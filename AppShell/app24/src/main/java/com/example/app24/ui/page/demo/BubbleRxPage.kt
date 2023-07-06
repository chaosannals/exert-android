package com.example.app24.ui.page.demo

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.app24.BubbleRxProvider
import com.example.app24.LocalBubbleRx
import com.example.app24.ui.DesignPreview
import com.example.app24.ui.sdp
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

@Composable
fun BubbleRxPageLv1() {
    var fromDeep1 by remember { mutableStateOf("") }
    var fromDeep2 by remember { mutableStateOf("") }

    BubbleRxProvider (
        onBubbleUp=
        {
            when (it.tag) {
                "deep1" -> {
                    fromDeep1 = it.data?.toString() ?: "null"
                    true // 虽然处理了事件，但是返回 true 让事件继续冒泡
                }
                "deep2" -> {
                    fromDeep2 = it.data?.toString() ?: "null"
                    false // 虽然处理了事件，但是返回 false 让事件停止冒泡
                }
                else -> true
            }
        },
    ) {
        Column(
            modifier = Modifier
                .border(1.sdp, Color.Red)
                .padding(4.sdp)
        ) {
            Text("fromDeep1: $fromDeep1")
            Text("fromDeep2: $fromDeep2")
            BubbleRxPageDeep1()
        }
    }
}

@Composable
fun BubbleRxPageDeep1() {
    val bubble = LocalBubbleRx.current

    Column(
        modifier = Modifier
            .border(1.sdp, Color.Green)
            .padding(4.sdp)
    ) {
        Button(
            onClick =
            {
                val d = UUID.randomUUID()
                bubble.bubbleUp("deep1", d)
            },
        ) {
            Text("bubble deep 1")
        }
        BubbleRxPageLv2()
    }
}

@Composable
fun BubbleRxPageLv2() {
    var fromDeep1 by remember { mutableStateOf("") }
    var fromDeep2 by remember { mutableStateOf("") }
    BubbleRxProvider (
        onBubbleUp=
        {
            when (it.tag) {
                "deep1" -> {
                    fromDeep1 = it.data?.toString() ?: "null"
                }
                "deep2" -> {
                    fromDeep2 = it.data?.toString() ?: "null"
                }
            }

            true // 虽然处理了事件，但是返回 true 让事件继续冒泡
        },
    ) {
        Column(
            modifier = Modifier
                .border(1.sdp, Color.Blue)
                .padding(4.sdp)
        ) {
            Text("fromDeep1: $fromDeep1") // 此处按目前接口是不应该收到且改变的。
            Text("fromDeep2: $fromDeep2")
            BubbleRxPageDeep2()
        }
    }
}

@Composable
fun BubbleRxPageDeep2() {
    val bubble = LocalBubbleRx.current

    Button(
        onClick =
        {
            val ts = System.currentTimeMillis()
            bubble.bubbleUp("deep2", ts)
        },
    ) {
        Text("bubble deep 2")
    }
}

@Composable
fun BubbleRxPage() {
    var isHookRoot by remember { mutableStateOf(false) }
    val events = remember {
        mutableStateListOf<String>()
    }

    if (isHookRoot) {
        val root = LocalBubbleRx.current
        DisposableEffect(Unit) {
            val rootDisposable = root.onBubbleUp.subscribe {
                val now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))
                events.add("[$now] root: ${it.tag}")
            }
            onDispose {
                rootDisposable.dispose()
            }
        }
    }


    BubbleRxProvider (
        onBubbleUp = {
            val now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))
            events.add("[$now] ${it.tag} : ${it.data}")
            true// 还是往顶层冒泡
        }
    ) {
        Column(
            modifier = Modifier
                .border(1.sdp, Color.Yellow)
                .padding(4.sdp)
        ) {
            Button(onClick = { isHookRoot = !isHookRoot }) {
                Text("isHookRoot: $isHookRoot")
            }
            events.forEach {
                Text(it)
            }
            BubbleRxPageLv1()
        }
    }
}

@Preview
@Composable
fun BubbleRxPagePreview() {
    DesignPreview {
        BubbleRxPage()
    }
}