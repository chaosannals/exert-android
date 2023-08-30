package com.example.bootdemo.ui.page.lock

import android.view.ViewTreeObserver
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CancelPage() {
    val context = LocalContext.current

    var tick by remember {
        mutableStateOf(0)
    }
    var number by remember {
        mutableStateOf(0)
    }
    var counter by remember {
        mutableStateOf(0)
    }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            ++tick
        }
    }

    LaunchedEffect(tick) {
        delay(800)
        ++number
        launch(Dispatchers.Main) {
            delay(1000) // 开始后 1800ms
            // 因为 tick 是每秒发生的，所以此处被 LaunchedEffect 机制 取消没有执行。
            ++counter
        }
    }

//    var preDraw
//    var preDrawCounter by remember {
//        mutableStateOf(0)
//    }
//
//    LaunchedEffect(key1 = , block = )
//
//    DisposableEffect(Unit) {
//        val listener = ViewTreeObserver.OnPreDrawListener {
//            ++preDrawCounter
//            true
//        }
//        onDispose {
//
//        }
//    }

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxSize()
    ) {
        Text("tick: $tick")
        Text("number: $number")
        Text("counter: $counter")
    }
}

@Preview
@Composable
fun CancelPagePreview() {
    CancelPage()
}