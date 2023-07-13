package com.example.appshell.ui.page.state

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appshell.LocalTotalStatus
import com.example.appshell.MainActivity
import com.example.appshell.ui.widget.DesignPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class OnlyViewModelState(
    val stringValue: String? = null,
)

// Jetpack Compose 的 VM 字段都是 StateFlow 和 LiveData 类型。写起来比较麻烦。
class OnlyViewModel: ViewModel() {
    // StateFlow
    private val customState = MutableStateFlow(OnlyViewModelState())
    val uiCustomState: StateFlow<OnlyViewModelState> = customState.asStateFlow()

    // LiveData
    val intState = MutableLiveData<Int>(0)
    val uiIntState: LiveData<Int> = intState

    suspend fun emitCustomState(v: OnlyViewModelState) {
        customState.emit(v)
    }

    fun setCustomState(v: OnlyViewModelState) {
        customState.value = v
    }
}

// 把生命周期关联到 Activity 上
@Composable
inline fun <reified VM : ViewModel> viewModelEx(): VM {
    val context = LocalContext.current as? MainActivity
    return if (context == null) {
        viewModel()
    } else {
        viewModel(viewModelStoreOwner = context)
    }
}

// 常用的  ViewModelStoreOwner 为 ComponentActivity、Fragment 和 NavBackStackEntry
// ComponentActivity 整个 Activity 周期
// Fragment 这个在 Jetpack Compose 基本用不到
// NavBackStackEntry 路由使用
// 使用不同的拥有者，数据持久化的时间不同。
// 谷歌提议只在页面组件使用，可重复使用的组件不要使用 VM ，因为：
// 1.生命周期默认只提供到了路由页级别，
// 2.按类型获取 VM ，导致同生命周期下同类型是同一个 VM
// 谷歌的 UI 库执行了此类方式，Jetpack Compose 组件源码的几乎不使用 VM
@Composable
fun OnlyViewModelPage(
    vm: OnlyViewModel = viewModelEx(),
//    vm: OnlyViewModel = viewModel(
//        // 使用此上下文使得数据是整个 Activity 周期。
//        // 如果使用预览不能这么写，预览的 Actitiy 是特殊的，无法转成 MainActivity。
//        // viewModelStoreOwner = LocalContext.current as MainActivity,
//    ),
) {
    val totalStatus = LocalTotalStatus.current
    val coroutineScope = rememberCoroutineScope()
    val state by vm.uiCustomState.collectAsState()
    val intValue by vm.uiIntState.observeAsState()

    LazyColumn(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding()
            .fillMaxSize()
    ) {
        item {
            state.stringValue?.let {
                Text(it)
            }
        }

        item {
            Button(
                onClick =
                {
                    totalStatus.router.navigate("only-view-model-lv2-page")
                },
            ) {
                Text(text = "Lv2 Page")
            }
        }

        item {
            Button(
                onClick =
                {
                    vm.setCustomState(OnlyViewModelState("${System.nanoTime()}"))
                },
            ) {
                Text("时间戳(value)")
            }
        }

        item {
            Button(
                onClick =
                {
                    coroutineScope.launch {
                        vm.emitCustomState(OnlyViewModelState("${System.nanoTime()}"))
                    }
                },
            ) {
                Text("时间戳(emit)")
            }
        }

        item {
            Button(
                onClick =
                {
                    vm.intState.value = (vm.intState.value ?: 0) + 1
                },
            ) {
                Text(text="${intValue}")
            }
        }
    }
}

@Preview
@Composable
fun OnlyViewModelPagePreview() {
    DesignPreview {
        OnlyViewModelPage()
    }
}