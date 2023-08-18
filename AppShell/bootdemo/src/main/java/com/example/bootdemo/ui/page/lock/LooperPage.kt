package com.example.bootdemo.ui.page.lock

import android.os.Looper
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// 安卓的方法
fun isMainThread(): Boolean {
    return Looper.myLooper() == Looper.getMainLooper()
}

@Composable
fun LooperPage() {
    val coroutineScope = rememberCoroutineScope()

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
}

@Preview
@Composable
fun LooperPagePreview() {
    LooperPage()
}