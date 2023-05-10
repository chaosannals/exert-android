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
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appshell.LocalTotalStatus
import com.example.appshell.ui.widget.DesignPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class OnlyViewModelState(
    val stringValue: String? = null,
)

class OnlyViewModel: ViewModel() {
    val state = MutableStateFlow(OnlyViewModelState())
    val uiState: StateFlow<OnlyViewModelState> = state.asStateFlow()

    val intState = MutableLiveData<Int>()
    val uiIntState: LiveData<Int> = intState
}

@Composable
fun OnlyViewModelPage(
    vm: OnlyViewModel = viewModel(),
) {
    val totalStatus = LocalTotalStatus.current
    val coroutineScope = rememberCoroutineScope()
    val state by vm.uiState.collectAsState()
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
                    coroutineScope.launch {
                        vm.state.emit(OnlyViewModelState("${System.nanoTime()}"))
                    }
                },
            ) {
                Text("时间戳")
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