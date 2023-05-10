package com.example.appshell.ui.page.state

import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.rxjava2.subscribeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.appshell.LocalTotalStatus
import com.example.appshell.ui.widget.DesignPreview
import io.reactivex.subjects.BehaviorSubject

// 基本上和 kotlin 版本 rxjava3 版本的一致
object OnlyRxJava2State {
    val intSubject: BehaviorSubject<Int> = BehaviorSubject.create()
}

@Composable
fun OnlyRxJava2Page() {
    val totalStatus = LocalTotalStatus.current

    val intValue by OnlyRxJava2State.intSubject.subscribeAsState(initial = 0)

    LazyColumn(
        modifier = Modifier.statusBarsPadding()
    ) {
        item {
            Button(
                onClick =
                {
                    totalStatus.router.navigate("only-rx-java2-lv2-page")
                },
            ) {
                Text("Lv2 Page")
            }
        }
        item {
            Button(onClick = {
                OnlyRxJava2State.intSubject.onNext(intValue + 1)
            }) {
                Text("$intValue")
            }
        }
    }
}

@Preview
@Composable
fun OnlyRxJava2PagePreview() {
    DesignPreview {
        OnlyRxJava2Page()
    }
}