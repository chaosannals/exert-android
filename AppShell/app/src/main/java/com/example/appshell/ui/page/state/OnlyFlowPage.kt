package com.example.appshell.ui.page.state

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.tooling.preview.Preview
import com.example.appshell.LocalTotalStatus
import com.example.appshell.ui.widget.DesignPreview
import com.example.appshell.ui.widget.ScrollColumn
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield

// android 官方实现的类似 Rx 的数据流类库
// emit 相关方法类似 Rx 的 onNext ;
// 一般的 Flow 现在在 生成器函数 里面才有 emit ;
// MutableSharedFlow 暴露 emit 给外部 类似 Rx PublishSubject 没有获取当前值的方法
// 没有类似 Rx 的 BehaviorSubject 这种东西，建议是使用 LiveData
// collect 相关方法类似 Rx 的 subject
object OnlyFlowState {
    val stringFlow: Flow<String> = flow {
        var i = 0;
        while(true) {
            emit("无尽字符串 ${i++}")
//            yield() // 间歇太短，数据如果被用到 UI 会被无限取，导致UI无限刷新卡死。
            delay(400)
        }
    }.flowOn(Dispatchers.IO)
    val intFlow: MutableSharedFlow<Int> = MutableSharedFlow<Int>()
}

@Composable
fun OnlyFlowPage() {
    // collectAsState 一些列官方给 Compose 提供的函数都会随 Compose 周期重新调用 生成器 其内部是 remember 封装不是 rememberSaveable。
    val text by OnlyFlowState.stringFlow.collectAsState(initial = "字符0")

//    val intValue by OnlyFlowState.intFlow.collectAsState(initial = 100)
    var intValue by rememberSaveable() { mutableStateOf(0) }
    val intHistory = rememberSaveable() {
        mutableStateListOf<Int>()
    }
    val totalStatus = LocalTotalStatus.current
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        // 可能因为先后问题， initial=100 的值是不会被此次 采集到。
        OnlyFlowState.intFlow.collect {
            intValue = it
            intHistory.add(it)
        }
    }

    ScrollColumn() {
        Text(
            text = text,
        )
        Button(
            onClick = {
                totalStatus.router.navigate("only-flow-lv2-page")
            }
        ) {
            Text(text="Only Flow Lv2")
        }
        Button(
            onClick =
            {
                coroutineScope.launch {
                    OnlyFlowState.intFlow.emit(intValue + 1)
                }
            },
        ) {
            Text(text = "$intValue")
        }
        intHistory.forEach {
            Text(text = "$it")
        }
    }
}

@Preview
@Composable
fun OnlyFlowPagePreview() {
    DesignPreview {
        OnlyFlowPage()
    }
}