package com.example.hlitdemo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() :ViewModel() {
    private val statusBarVisibleFlow: MutableStateFlow<Boolean> = MutableStateFlow(true)

    @Composable
    fun collectStatusBarVisible(): State<Boolean> {
        return statusBarVisibleFlow.collectAsState()
    }

    suspend fun emitStatusBarVisible(visible: Boolean) {
        statusBarVisibleFlow.emit(visible)
    }
}