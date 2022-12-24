package com.example.anidemo.ui.page

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.anidemo.LocalMainScroller
import com.example.anidemo.LocalNavController
import com.example.anidemo.ShortcutUtil
import com.example.anidemo.SystemInfoUtil
import com.example.anidemo.ui.widget.PullDownBox

@Composable
fun GistPage() {
    val nc = LocalNavController.current
    val context = LocalContext.current

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxWidth()
    ) {
        Text(
            text = "PullDown",
            modifier = Modifier
                .clickable {
                    nc.navigate("pulldown")
                }
        )
        Text(
            text = "PullDown2",
            modifier = Modifier
                .clickable {
                    nc.navigate("pulldown2")
                }
        )
        Text(
            text = "PullDownPushUp",
            modifier = Modifier
                .clickable {
                    nc.navigate("pulldownpushup")
                }
        )
        Text(
            text="init Shortcut Scan2",
            modifier = Modifier
                .clickable {
                    ShortcutUtil.init(context)
                }
        )
        Text(
            text="update Shortcut Scan2",
            modifier = Modifier
                .clickable {
                    ShortcutUtil.update(context)
                }
        )
        Text(
            text="remove Shortcut Scan2",
            modifier = Modifier
                .clickable {
                    ShortcutUtil.remove(context)
                }
        )
        Text(
            text="init Shortcut Scan3",
            modifier = Modifier
                .clickable {
                    ShortcutUtil.init3(context)
                }
        )
        Text(
            text="update Shortcut Scan3",
            modifier = Modifier
                .clickable {
                    ShortcutUtil.update3(context)
                }
        )
        Text(
            text="remove Shortcut Scan3",
            modifier = Modifier
                .clickable {
                    ShortcutUtil.remove3(context)
                }
        )
        Column () {
            val sysInfo = SystemInfoUtil.getInfo(context)
            Text("手机厂商：${sysInfo.brand}")
            Text("手机型号：${sysInfo.model}")
            Text("手机语言：${sysInfo.lang}")
            Text("系统版本：${sysInfo.version}")
            Text("IMEI: ${sysInfo.imei}")
            Text("序列化串：${sysInfo.serial}")
            Text("MAC：${sysInfo.macAddress}")
            Text("ANDROID ID:${sysInfo.androidId}")
            for (e in sysInfo.errors) {
                Text("错误: ${e}")
            }
        }
    }
}

@Preview
@Composable
fun GistPagePreview() {
    CompositionLocalProvider(
        LocalNavController provides rememberNavController(),
        LocalMainScroller provides rememberScrollState(),
    ) {
        GistPage()
    }
}