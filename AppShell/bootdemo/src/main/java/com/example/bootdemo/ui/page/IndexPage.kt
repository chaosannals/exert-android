package com.example.bootdemo.ui.page

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bootdemo.ui.LocalRouter
import kotlinx.coroutines.launch

@Composable
fun IndexButton(
    text: String,
    route: String,
) {
    val mode = LocalInspectionMode.current
    val router = if (mode) null else LocalRouter.current

    Button(
        onClick = { router?.navigate(route) },
        ) {
        Text(
            text = text,
            fontSize = 14.sp,
            )
    }
}

@Composable
fun IndexPage() {
    Column(
        modifier = Modifier
            .safeContentPadding()
            .fillMaxSize(),
    ) {
        IndexButton(text = "返回键", route = "can-back")
        IndexButton(text = "绘制", route = "drawing")
        IndexButton(text = "网页", route = "web-view")
        IndexButton(text = "文件", route = "file-system")
        IndexButton(text = "锁", route = "lock")
        IndexButton(text = "附带效应（Side Effect）", route = "effect")
        IndexButton(text = "存储", route = "storage")
        IndexButton(text = "布局", route = "layout")
        IndexButton(text = "路由", route = "route-lv1")
    }
}

@Preview
@Composable
fun IndexPagePreview() {
    IndexPage()
}