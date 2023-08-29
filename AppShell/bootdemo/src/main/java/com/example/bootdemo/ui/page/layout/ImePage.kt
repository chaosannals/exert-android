package com.example.bootdemo.ui.page.layout

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.subjects.BehaviorSubject
import kotlinx.coroutines.flow.MutableStateFlow

private val stateRx: BehaviorSubject<Boolean> = BehaviorSubject.create()
private val stateSf: MutableStateFlow<Boolean> = MutableStateFlow(false)
private val stateLd: MutableLiveData<Boolean> = MutableLiveData()

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImePage() {
    val visibleRx by stateRx.subscribeAsState(initial = false)
    val mRx by remember(visibleRx) {
        derivedStateOf {
            if (visibleRx) {
                Modifier
                    .navigationBarsPadding()
                    .fillMaxSize()
            } else {
                Modifier
                    .navigationBarsPadding()
            }
        }
    }

    val visibleSf by stateSf.collectAsState()
    val mSf by remember(visibleSf) {
        derivedStateOf {
            if (visibleSf) {
                Modifier
                    .navigationBarsPadding()
                    .fillMaxSize()
            } else {
                Modifier
                    .navigationBarsPadding()
            }
        }
    }

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxSize()
    ) {
        var text by remember { mutableStateOf("") }
        TextField(
            value = text,
            onValueChange = {text = it}
        )
        Column(
            modifier = mRx // TODO 测试 切换 mRx 会导致程序卡死
                .border(1.dp, Color.Red)
                .imePadding()
        ) {
            Button(
                onClick = {
                    stateRx.onNext(!visibleRx)
                },
            ) {
                Text(text = "Rx")
            }
        }

        Column(
            modifier = mSf // TODO 测试是否与 RX 一样
                .border(1.dp, Color.Blue)
                .imePadding()
        ) {
            Button(
                onClick = {
                    stateSf.value = !visibleSf
                },
            ) {
                Text(text = "Sf")
            }
        }
    }
}

@Preview
@Composable
fun ImePagePreview() {
    ImePage()
}