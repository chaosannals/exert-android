package com.example.hlitdemo.ui.page

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// 此实现用 Rx 同步的方式，放弃 rememberUpdateState 闭包异步操作
// 缺点，响应阶段必然会有 null 值被 rememberRxTaskController 的 remember 返回。
data class RxTaskController<T>(
    val value: T?,
    val newValueState: () -> Job?, // Job 可能是空，一般在初始阶段 null
)

@Composable
fun <T> rememberRxTaskController(
    newValue: suspend () -> T?,
): RxTaskController<T> {
    val coroutineScope = rememberCoroutineScope()
    val subject: PublishSubject<Unit> = remember {
        PublishSubject.create()
    }

    var resultValue: T? by remember {
        mutableStateOf(null)
    }
    var resultJob: Job? by remember {
        mutableStateOf(null)
    }

    DisposableEffect(subject) {
        val subjectDisposable = subject.subscribe {
            resultJob = coroutineScope.launch(Dispatchers.IO) {
                resultValue = newValue()
            }
        }
        onDispose {
            subjectDisposable.dispose()
        }
    }

    return remember(
        resultValue,
        resultJob,
    ) {
        Log.d("hlitdemo", "rememberRxTaskController init $resultValue $resultJob")
        RxTaskController(
            resultValue,
        ) {
            subject.onNext(Unit)
            resultJob
        }
    }
}

@Composable
fun Coroutine4Page() {
    Column {
        Log.d("hlitdemo", "rememberRxTaskController Coroutine4Page")
        val task2 = rememberRxTaskController {
            delay(1000)
            Log.d("hlitdemo", "call task2")
            // rememberUpdatedState 的协程会执行，所以重复的协程没有取消。手动保留 task2Job , 进行取消。
            "当前时间：${System.currentTimeMillis()}"
        }
        var task2Job: Job? by remember {
            mutableStateOf(null)
        }
        Text("${task2.value}")
        Button(
            onClick =
            {
                // 可以拿到 job 取消。
                task2Job?.cancel()
                task2Job = task2.newValueState()
            },
        ) {
            Text("重置2")
        }
    }
}

@Preview
@Composable
fun Coroutine4PagePreview() {
    Coroutine4Page()
}