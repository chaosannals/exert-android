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

    // 如果以当前路由参数为 key 切换路由时会先触发此项。
    LaunchedEffect(entry) {
        Log.d("Lv1", "[Route] Lv1 $path")
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
    }
}

@Preview
@Composable
fun RouteLv1PagePreview() {
    RouteLv1Page()
}