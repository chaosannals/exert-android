package com.example.hlitdemo.ui.page

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.job

private val task1Flow = MutableStateFlow<Deferred<Long>>(CompletableDeferred(0))

@Composable
fun CoroutinePage() {
    val coroutineScope = rememberCoroutineScope()

    val task1 by task1Flow.collectAsState()
    var task1Value by remember {
        mutableStateOf(0L)
    }
    LaunchedEffect(task1) {
        task1Value = task1.await()
    }

    val task2 = remember {
        coroutineScope.async { System.currentTimeMillis() }
    }

    LaunchedEffect(task1Value) {
//        awaitAll(task1, task2)
        if (task1Value == 0L) {
            task1Flow.emit(async{ System.currentTimeMillis() })
        }
    }

    Column() {
        Text("$task1Value")
    }
}

@Preview
@Composable
fun CoroutinePagePreview() {
    CoroutinePage()
}