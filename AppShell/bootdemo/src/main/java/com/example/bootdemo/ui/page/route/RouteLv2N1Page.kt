package com.example.bootdemo.ui.page.route

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.bootdemo.ui.LocalRouter
import com.example.bootdemo.ui.ROUTE_ROUTE_LV1
import com.example.bootdemo.ui.ROUTE_ROUTE_LV2_N1

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteLv2N1Page() {
    val mode = LocalInspectionMode.current
    val router = if (mode) rememberNavController() else LocalRouter.current

    val entry by router.currentBackStackEntryAsState()
    val path by remember(entry) {
        derivedStateOf {
            entry?.arguments?.getString("path")
        }
    }

    // 进入触发 1 次
    LaunchedEffect(Unit) {
        Log.d("Lv2N1", "[Route] 进入 Lv2N1 $path")
    }

    // 通过条件 可以在离开时触发 1 次
    LaunchedEffect(entry) {
        if (entry?.destination?.route != ROUTE_ROUTE_LV2_N1) {
            Log.d("Lv2N1", "[Route] 离开 Lv2N1 $path")
        }
    }

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxSize()
    ) {
        var text by remember {
            mutableStateOf("fromLv2N1")
        }
        TextField(value = text, onValueChange = {text = it})
        Button(onClick = {
            router.navigate("route-lv1?path=$text")
        }) {
            Text("Lv1")
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
fun RouteLv2N1PagePreview() {
    RouteLv2N1Page()
}