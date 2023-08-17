package com.example.bootdemo.ui.page.lock

import android.os.Looper
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex

fun isMainThread(): Boolean {
    return Looper.myLooper() == Looper.getMainLooper()
}

@Composable
fun LockPage() {
    val coroutineScope = rememberCoroutineScope()
    val mutex = remember {
        Mutex(false)
    }

    var isMain by remember {
        mutableStateOf(isMainThread())
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(text = "isMain: $isMain")

        Button(
            onClick = {
                isMain = isMainThread()
            }
        ) {
            Text("Main")
        }

        Button(
            onClick = {
                coroutineScope.launch(Dispatchers.Main) {
                    isMain = isMainThread()
                }
            }
        ) {
            Text("Main(launch)")
        }

        Button(
            onClick = {
                coroutineScope.launch(Dispatchers.IO) {
                    isMain = isMainThread()
                }
            }
        ) {
            Text("IO")
        }
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
fun LockPagePreview() {
    LockPage()
}