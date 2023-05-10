package com.example.appshell.ui.page.state

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.appshell.LocalTotalStatus
import com.example.appshell.ui.widget.DesignPreview

object OnlyLiveDataState {
    val stringLive: MutableLiveData<String> = MutableLiveData()
    val stringData: LiveData<String> get() = stringLive
}

@Composable
fun OnlyLiveDataPage() {
    val totalStatus = LocalTotalStatus.current
    val text: String? by OnlyLiveDataState.stringData.observeAsState()
    var intValue by rememberSaveable() {
        mutableStateOf(0)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        item {
            text?.let {
                Text(it)
            }
        }

        item {
            Button(
                onClick =
                {
                    totalStatus.router.navigate("only-live-data-lv2-page")
                },
            ) {
                Text("Lv2 Page")
            }
        }

        item {
            Button(
                onClick =
                {
                    intValue += 1
                    OnlyLiveDataState.stringLive.value = "+ $intValue"
                },
            ) {
                Text("+")
            }
        }
    }
}

@Preview
@Composable
fun OnlyLiveDataPagePreview() {
    DesignPreview {
        OnlyLiveDataPage()
    }
}