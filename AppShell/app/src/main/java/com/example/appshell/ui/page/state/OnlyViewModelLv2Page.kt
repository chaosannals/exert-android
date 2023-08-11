package com.example.appshell.ui.page.state

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appshell.ui.widget.DesignPreview
import kotlinx.coroutines.flow.MutableStateFlow

class OnlyViewModelM2 : ViewModel() {
    val textState = MutableStateFlow("")
}

// viewModel 最细划分到路由，所以同路由下 viewModel 是同一个。
// 这是个 反面示例 不应该在重复使用的组件使用 viewModel
// viewModel 一般之应该在路由页级别使用。
@Composable
fun OnlyViewModelBox(
    vm: OnlyViewModelM2 = viewModel()
) {
    val text by vm.textState.collectAsState()
    TextField(
        label = { Text(text = "文本") },
        value = text,
        onValueChange = {vm.textState.value = it},
        modifier = Modifier
            .fillMaxWidth(),
    )
}

@Composable
fun OnlyViewModelLv2Page() {
    var isShow1 by remember {
        mutableStateOf(false)
    }
    var isShow2 by remember {
        mutableStateOf(false)
    }
    var isShow3 by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxSize()
    ) {
        Button(onClick = { isShow1 = !isShow1 }) {
            Text("显示1: $isShow1")
        }
        Button(onClick = { isShow2 = !isShow2 }) {
            Text("显示2: $isShow2")
        }
        Button(onClick = { isShow3 = !isShow3 }) {
            Text("显示3: $isShow3")
        }
        if (isShow1) {
            OnlyViewModelBox()
        }
        if (isShow2) {
            OnlyViewModelBox()
        }
        if (isShow3) {
            OnlyViewModelBox()
        }
    }
}

@Preview
@Composable
fun OnlyViewModelLv2PagePreview() {
    DesignPreview {
        OnlyViewModelLv2Page()
    }
}