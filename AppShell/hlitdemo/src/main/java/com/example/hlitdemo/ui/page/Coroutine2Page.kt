package com.example.hlitdemo.ui.page

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

data class TaskController<T> (
    val value: T?,
    val newValueState: () -> Job,
)

// rememberUpdatedState 的值启用了【异步】时不应该被 remember 当作 key , 会导致循环 ReComposable
// 其在 Composable 阶段使用 apply 设置值。
// 此为 官方源码 copy 后 Log.d
@Composable
fun <T> rememberUpdatedState2(newValue: T): State<T> = remember {
    Log.d("hiltdemo", "rememberUpdatedState2 init $newValue")
    mutableStateOf(newValue)
}.apply {
    Log.d("hiltdemo", "rememberUpdatedState2 apply $newValue")
    value = newValue // 此处是在 remember 后接上，所以是在 Composable 阶段执行的，会导致一次 ReComposable
}

@Composable
fun <T> rememberTaskController(
    newValue: suspend () -> T?,
): TaskController<T> {
    val coroutineScope = rememberCoroutineScope()
    val stateFlow: MutableStateFlow<Deferred<T?>> = remember {
        MutableStateFlow(CompletableDeferred(null))
    }
    val deferred by stateFlow.collectAsState()
    var resultValue: T? by remember {
        mutableStateOf(null)
    }

    val newValueState by rememberUpdatedState2 {
        Log.d("hlitdemo","rememberTaskController coroutineScope.launch")
        coroutineScope.launch {
            stateFlow.emit(async { newValue() })
        }
    }

    LaunchedEffect(deferred) {
        launch(Dispatchers.IO) {
            resultValue = deferred.await()
        }
    }

    return remember(
        resultValue,
//        newValueState,
    ) {
        Log.d("hlitdemo","rememberTaskController return reset $deferred  $newValueState")
        TaskController(resultValue, newValueState)
    }
}

@Composable
fun Coroutine2Page() {
    Column() {
        val scope = rememberCoroutineScope()

        val updateA by rememberUpdatedState {
            scope.launch {  } // 1 只要这里使用了异步
        }
        val a by remember(updateA) { // 2 设置为 key
            Log.d("hlitdemo", "此处不会有问题")
            mutableStateOf("1234")
        }

        var b: String? by remember {
            mutableStateOf(null)
        }
        val updateB by rememberUpdatedState {
            // 此处使用异步，且被当作 key
            scope.launch { b = "时间: ${System.currentTimeMillis()}" }
//            b = "时间: ${System.currentTimeMillis()}"
        }
//        val bb by remember(updateB) {
//            mutableStateOf(123)
//        }
        Button(
            onClick = { updateB() },
        ) {
            Text("异步导致无限 ReComposable: $b")
        }


        val task = rememberTaskController {
            System.currentTimeMillis()
        }
        Text("${task.value}")
        Button(
            onClick = { task.newValueState() },
        ) {
            Text("重置")
        }

        Log.d("hlitdemo", "rememberTaskController Coroutine2Page")
        val task2 = rememberTaskController {
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
fun Coroutine2PagePreview() {
    Coroutine2Page()
}