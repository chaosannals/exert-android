package com.example.bootdemo.ui.page.lock

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

// runBlocking 有被 suspend 染色，可在一般作用域使用
// 大部分纯 kotlin 示例都用这个函数开头
// 该函数自己产生了一个 coroutineScope
// Jetpack Compose 一般用 rememberCoroutineScope 生成一个，或用 LaunchedEffect
@Composable
fun CoroutinePage() {
    var r1 by remember {
        mutableStateOf(0L)
    }
    var r1IsReject by remember {
        mutableStateOf(false)
    }
    var r1Reject by remember(r1IsReject) {
        mutableStateOf("")
    }

    var r2 by remember {
        mutableStateOf(0L)
    }
    var r2IsReject by remember {
        mutableStateOf(false)
    }
    var r2Reject by remember(r2IsReject) {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxSize()
    ) {
        Button(
            onClick = {
                try {
                    r1 = runBlocking {
                        suspendCoroutine {
                            if (r1IsReject) {
                                it.resumeWithException(Exception("r1 reject"))
                            } else {
                                it.resume(System.currentTimeMillis()) // resolve
                            }
                        }
                    }
                }
                catch (t: Throwable) {
                    r1Reject = t.message ?: "not message"
                }
            }
        ) {
            Text(text = "suspendCoroutine: $r1")
        }
        Button(onClick = { r1IsReject = !r1IsReject }) {
            Text(text = "suspendCoroutine: throw $r1IsReject")
        }
        Text(text = r1Reject)

        Button(
            onClick = {
                try {
                    r2 = runBlocking {
                        supervisorScope {
                            if (r2IsReject) {
                                throw Exception("r2 reject")
                            } else {
                                System.currentTimeMillis()
                            }
                        }
                    }
                }
                catch (t: Throwable) {
                    r2Reject = t.message ?: "not message"
                }
            }
        ) {
            Text(text = "supervisorScope: $r2")
        }

        Button(onClick = { r2IsReject = !r2IsReject }) {
            Text(text = "supervisorScope: throw $r2IsReject")
        }
        Text(text = r2Reject)

        // TODO
        // 这个好像没啥用，控制取消的 CancellableContinuation 是在闭包内部。
        // 不像 job 可以在外部取消操作。
        Button(
            onClick =
            {
                runBlocking {
                    suspendCancellableCoroutine {

                    }
                }
            }
        ) {

        }
    }
}

@Preview
@Composable
fun CoroutinePagePreview() {
    CoroutinePage()
}