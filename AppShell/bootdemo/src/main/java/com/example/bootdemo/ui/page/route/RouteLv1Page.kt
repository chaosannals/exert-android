package com.example.bootdemo.ui.page.route

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.bootdemo.ui.LocalRouter
import com.example.bootdemo.ui.ROUTE_ROUTE_LV1
import kotlinx.coroutines.flow.MutableStateFlow

private val tickSf = MutableStateFlow(0L)
private val tickSfi = MutableStateFlow(0L)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteLv1Page() {
    val mode = LocalInspectionMode.current
    val router = if (mode) rememberNavController() else LocalRouter.current

    val entry by router.currentBackStackEntryAsState()
    val path by remember(entry) {
        derivedStateOf {
            entry?.arguments?.getString("path")
        }
    }
    var tick by remember {
        mutableStateOf(System.currentTimeMillis().toString())
    }
    // 起始时 tick 只触发一次，可见 currentBackStackEntryAsState 起始触发 2 次是比较特殊的。
    LaunchedEffect(tick) {
        Log.d("Lv1", "[Route] Lv1 tick: $tick")
    }

    // State Flow 如果 collectAsState 给定 initial 就会导致触发 2 次，而且值非 initial 的值
    val tickSfValue by tickSf.collectAsState()
    LaunchedEffect(tickSfValue) {
        Log.d("Lv1", "[Route] Lv1 tickSf: $tickSfValue")
    }

    val tickSfiValue by tickSf.collectAsState(initial = System.currentTimeMillis())
    LaunchedEffect(tickSfiValue) {
        Log.d("Lv1", "[Route] Lv1 tickSf set initial: $tickSfiValue")
    }

    // 如果以当前路由参数为 key 切换路由时会先触发此项。
    // 可以判定离开和进入，但是进入时该页面会触发 2 次。
    // 参考 Lv2N1 的做法 分开处理 进入 和 离开 可各触发 1 次。
    LaunchedEffect(entry) {
        if (entry?.destination?.route == ROUTE_ROUTE_LV1) {
            Log.d("Lv1", "[Route] 进入 Lv1 $path") // 2 次
        } else {
            Log.d("Lv1", "[Route] 离开 Lv1 $path") // 1 次
        }
    }

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxSize()
    ) {
        var text by remember {
            mutableStateOf("fromLv1")
        }
        TextField(value = text, onValueChange = {text = it})
        Button(onClick = {
            router.navigate("route-lv2-n1?path=$text")
        }) {
            Text("Lv2N1")
        }
        Button(onClick = {
            router.navigate("route-lv2-n2?path=$text")
        }) {
            Text("Lv2N2")
        }

        Button(onClick = { tick = System.currentTimeMillis().toString() }) {
            Text(text = "时间：$tick")
        }

        Button(onClick = { tickSf.value = System.currentTimeMillis() }) {
            Text(text = "时间 State Flow：$tickSfValue")
        }

        Button(onClick = { tickSfi.value = System.currentTimeMillis() }) {
            Text(text = "时间 State Flow set initial：$tickSfiValue")
        }
    }
}

@Preview
@Composable
fun RouteLv1PagePreview() {
    RouteLv1Page()
}