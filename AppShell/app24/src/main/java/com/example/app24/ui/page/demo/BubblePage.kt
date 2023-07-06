package com.example.app24.ui.page.demo

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.app24.BubbleProvider
import com.example.app24.LocalBubble
import com.example.app24.ui.DesignPreview
import com.example.app24.ui.sdp
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

@Composable
fun BubblePageLv1() {
    var fromDeep1 by remember { mutableStateOf("") }
    var fromDeep2 by remember { mutableStateOf("") }

    BubbleProvider (
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
            BubblePageDeep1()
        }
    }
}

@Composable
fun BubblePageDeep1() {
    val bubble = LocalBubble.current

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
        BubblePageLv2()
    }
}

@Composable
fun BubblePageLv2() {
    var fromDeep1 by remember { mutableStateOf("") }
    var fromDeep2 by remember { mutableStateOf("") }
    BubbleProvider (
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
            BubblePageDeep2()
        }
    }
}

@Composable
fun BubblePageDeep2() {
    val bubble = LocalBubble.current

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
fun BubblePage() {
    val events = remember {
        mutableStateListOf<String>()
    }
    BubbleProvider (
        onBubbleUp = {
            val now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))
            events.add("[$now] ${it.tag} : ${it.data}")
            false
        }
    ) {
        Column(
            modifier = Modifier
                .border(1.sdp, Color.Yellow)
                .padding(4.sdp)
        ) {
            events.forEach {
                Text(it)
            }
            BubblePageLv1()
        }
    }
}

@Preview
@Composable
fun BubblePagePreview() {
    DesignPreview {
        BubblePage()
    }
}