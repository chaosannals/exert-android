package com.example.bootdemo.ui.page.lock

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex

// Mutex 是 java 类，
// 如果使用了 kotlin 的调度器，不应该使用，有一定概率和 kotlin 调度器发生锁死。
@Composable
fun MutexPage() {
    val mutex = remember {
        Mutex(false)
    }

    LaunchedEffect(mutex) {
        Log.d("锁", "0")
        mutex.lock()
        launch(Dispatchers.IO) {
            Log.d("锁", "1")
            delay(1000)
            mutex.unlock()
            Log.d("锁", "2")
        }
        mutex.lock()
        mutex.unlock()
        Log.d("锁", "3")
    }
}

@Preview
@Composable
fun MutexPagePreview() {
    MutexPage()
}