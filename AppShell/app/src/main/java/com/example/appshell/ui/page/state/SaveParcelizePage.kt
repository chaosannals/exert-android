package com.example.appshell.ui.page.state

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.appshell.LocalTotalStatus
import com.example.appshell.ui.widget.DesignPreview

@Composable
fun <T: Any> rememberMutableStateListOf(vararg elements: T): SnapshotStateList<T> {
    return rememberSaveable(
        saver = listSaver(
            save = { stateList ->
                if (stateList.isNotEmpty()) {
                    val first = stateList.first()
                    if (!canBeSaved(first)) {
                        throw IllegalStateException("${first::class} cannot be saved. By default only types which can be stored in the Bundle class can be saved.")
                    }
                }
                stateList.toList()
            },
            restore = { it.toMutableStateList() }
        )
    ) {
        elements.toList().toMutableStateList()
    }
}

// 路由上必须是平级（根下或者 同 navigation 下 Saver 才会起效保存）
@Composable
fun SaveParcelizePage() {
    val totalStatus = LocalTotalStatus.current
    val intList = rememberMutableStateListOf<Int>()
    var intState by rememberSaveable() {
        mutableStateOf(0)
    }

    LazyColumn(
        modifier= Modifier
            .statusBarsPadding()
            .navigationBarsPadding()
            .fillMaxSize(),
    ) {
        item {
            Button(onClick = { totalStatus.router.navigate("save-parcelize-lv2-page") }) {
                Text("Lv2 Page")
            }
        }
        item {
            Button(onClick = { intList.add(intState++) }) {
                Text("${intState} +")
            }
        }
        itemsIndexed(intList) {i, it ->
            Text(text="[$i] $it")
        }
    }
}

@Preview
@Composable
fun SaveParcelizePagePreview() {
    DesignPreview {
        SaveParcelizePage()
    }
}