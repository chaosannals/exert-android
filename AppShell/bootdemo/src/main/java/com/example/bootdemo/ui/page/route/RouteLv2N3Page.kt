package com.example.bootdemo.ui.page.route

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.bootdemo.ui.LocalRouter
import kotlinx.coroutines.flow.MutableStateFlow

val timestampSf = MutableStateFlow(0L)

/// currentBackStackEntryAsState 的值做为 key 无论利用间接多少级  key 都会在进入阶段触发 2 次。
@Composable
fun RouteLv2N3Page() {
    val mode = LocalInspectionMode.current
    val router = if (mode) rememberNavController() else LocalRouter.current

    val entry by router.currentBackStackEntryAsState()
    val path by remember(entry) {
        derivedStateOf {
            entry?.arguments?.getString("path")
        }
    }
    val notKeyPath by remember {
        derivedStateOf {
            "not key: ${entry?.arguments?.getString("path")}"
        }
    }

    LaunchedEffect(path) {
        Log.d("Lv2N3", "[Route] path Lv2N3 $path")
    }
    LaunchedEffect(notKeyPath) {
        Log.d("Lv2N3", "[Route] notKeyPath Lv2N3 $notKeyPath")
    }

    val timestamp by timestampSf.collectAsState(System.currentTimeMillis())
    val text by remember(timestamp) {
        derivedStateOf {
            "$timestamp"
        }
    }

    LaunchedEffect(timestamp) {
        Log.d("Lv2N3", "[Route] timestamp Lv2N3 $timestamp")
    }

    LaunchedEffect(text) {
        Log.d("Lv2N3", "[Route] text Lv2N3 $text")
    }

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxSize()
    ) {
        Button(onClick = {
            router.navigate("route-lv1?path=$text")
        }) {
            Text("Lv1")
        }
        Button(onClick = { timestampSf.value = System.currentTimeMillis() }) {
            Text(text = "时间 t：$timestamp  s: $text")
        }
    }
}

@Preview
@Composable
fun RouteLv2N3PagePreview() {
    RouteLv2N3Page()
}