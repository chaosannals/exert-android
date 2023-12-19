package com.example.appshell.ui.page.demo

import android.util.Base64
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.appshell.ui.sdp
import com.example.appshell.ui.widget.DesignPreview
import java.time.format.TextStyle

// 完全正则匹配
private val DATA_URL_PATTERN = Regex("data:(.+?)(;base64)?,(.+)", RegexOption.DOT_MATCHES_ALL)
// 只正则头部信息。
private val DATA_URL_HEAD_PATTERN = Regex("data:(.+?)(;base64)?", RegexOption.DOT_MATCHES_ALL)

data class DataUrlInfo (
    val mime: String,
    val isBase64: Boolean,
    val data: String,
)

fun matchDataUrl(url: String): DataUrlInfo? {
    return DATA_URL_PATTERN.matchEntire(url)?.run {
        DataUrlInfo(
            mime = groups[1]?.value!!,
            isBase64 = groups[2]?.value != null,
            data = groups[3]?.value!!,
        )
    }
}

fun matchDataUrlForComma(url: String): DataUrlInfo? {
    Log.d("DataUrlPage", "match start: $url")
    val firstComma = url.indexOf(',')
    if (firstComma == -1) {
        return null
    }
    Log.d("DataUrlPage", "first comma: $firstComma")
    val head = url.substring(0, firstComma)
    val data = url.substring(firstComma + 1)
    return DATA_URL_HEAD_PATTERN.matchEntire(head)?.run {
        Log.d("DataUrlPage", "return: $head")
        DataUrlInfo(
            mime = groups.get(1)?.value!!,
            isBase64 = if (groups.get(2)?.value == null) false else true,
            data = data,
        )
    }
}

@Composable
fun DataUrlPage() {
    // 如果太长，显示会很卡，直接卡爆。
    var testUrl by remember {
        mutableStateOf("data:text/plain;base64,SGVsbG8sIFdvcmxkIQ==")
    }

    val info: DataUrlInfo? by remember(testUrl) {
        derivedStateOf {
            matchDataUrl(testUrl)
        }
    }
    val dataLength by remember(info) {
        derivedStateOf {
            info?.data?.let { Base64.decode(it, Base64.DEFAULT) }?.size
        }
    }
    
    Column(
        modifier = Modifier
            .statusBarsPadding()
    ) {
        // 如果太长，显示会很卡，直接卡爆。
        Text("如果太长，显示会很卡，直接卡爆。")
        TextField(
            value = testUrl,
            onValueChange = { testUrl = it },
            label = { Text("DataUrl")},
            singleLine = false,
            modifier = Modifier
                .fillMaxWidth(),
        )
        info?.run {
            Text(text = mime)
            Text(text = "$isBase64")
            // 如果太长，显示会很卡，直接卡爆。
            Text(text = data)
            Text(text = "$dataLength")
        }
    }
}

@Preview
@Composable
fun DataUrlPagePreview() {
    DesignPreview {
        DataUrlPage()
    }
}