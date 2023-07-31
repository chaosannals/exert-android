package com.example.hlitdemo.ui.page

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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class TaskController<T> (
    val value: T?,
    val newValueState: () -> Job,
)

@Composable
fun <T> rememberDemoTask(
    newValue: () -> T?,
): TaskController<T> {
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
    return remember(resultValue, newValueState) {
        TaskController(resultValue, newValueState)
    }
}

@Composable
fun Coroutine2Page() {
    val task = rememberDemoTask {
        System.currentTimeMillis()
    }
    val task2 = rememberDemoTask {
        "当前时间：${System.currentTimeMillis()}"
    }
    Column() {
        Text("${task.value}")
        Button(
            onClick = { task.newValueState() },
        ) {
            Text("重置")
        }

        Text("${task2.value}")
        Button(
            onClick = { task2.newValueState() },
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