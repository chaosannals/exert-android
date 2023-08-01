package com.example.hlitdemo.ui.page

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

// 重复调用后会取消，保证唯一任务。
class UniqueTaskController<T>(
    val value: T?,
    private val newValueState: () -> Job,
) {
    var job: Job? = null

    fun invoke() {
        job?.cancel()
        job = newValueState()
    }
}

@Composable
fun <T> rememberUniqueTaskController(
    newValue: suspend () -> T?,
): UniqueTaskController<T> {
    val coroutineScope = rememberCoroutineScope()
    val stateFlow: MutableStateFlow<Deferred<T?>> = remember {
        MutableStateFlow(CompletableDeferred(null))
    }
    val deferred by stateFlow.collectAsState()
    var resultValue: T? by remember {
        mutableStateOf(null)
    }
    val newValueState by rememberUpdatedState {
        coroutineScope.launch {
            stateFlow.emit(async { newValue() })
        }
    }
    LaunchedEffect(deferred) {
        resultValue = deferred.await()
    }
    return remember(
        resultValue,
    ) {
        Log.d("hlitdemo","rememberUniqueTaskController return reset")
        UniqueTaskController(resultValue, newValueState)
    }
}

@Composable
fun Coroutine3Page() {
    val task = rememberUniqueTaskController {
        System.currentTimeMillis()
    }

    val task2 = rememberUniqueTaskController {
        delay(1000)
        Log.d("hlitdemo","rememberUniqueTaskController call task2")
        // rememberUpdatedState 的协程会执行，所以重复的协程没有取消。手动保留 task2Job , 进行取消。
        "当前时间：${System.currentTimeMillis()}"
    }

    Column() {
        Text("${task.value}")
        Button(
            onClick = { task.invoke() },
        ) {
            Text("重置")
        }

        Text("${task2.value}")
        Button(
            onClick =
            {
                task2.invoke()
            },
        ) {
            Text("重置2")
        }
    }
}

@Preview
@Composable
fun Coroutine3PagePreview() {
    Coroutine3Page()
}