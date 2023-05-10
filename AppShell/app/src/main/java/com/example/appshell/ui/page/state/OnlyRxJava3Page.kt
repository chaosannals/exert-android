package com.example.appshell.ui.page.state

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.*
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.ui.tooling.preview.Preview
import com.example.appshell.ui.widget.DesignPreview
import io.reactivex.rxjava3.subjects.BehaviorSubject // 是 rxjava3 不是 kotlin 版本的

// 基本上和 kotlin 版本 rxjava2 版本的一致
object OnlyRxJava3State {
    val intSubjection:BehaviorSubject<Int> = BehaviorSubject.create()
}

@Composable
fun OnlyRxJava3Page() {
    val intValue by OnlyRxJava3State.intSubjection.subscribeAsState(0)

    LazyColumn() {

    }
}

@Preview
@Composable
fun OnlyRxJava3PagePreview() {
    DesignPreview {
        OnlyRxJava3Page()
    }
}