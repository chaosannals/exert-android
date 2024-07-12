package com.example.calendardemo.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

import com.example.calendardemo.ui.theme.CalendardemoTheme
import com.example.calendardemo.ui.widget.BottomBar
import com.example.calendardemo.ui.widget.rememberSaveableWebViewState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

private val navigateEvents = MutableSharedFlow<String>()
fun CoroutineScope.navigate(path: String) {
    launch {
        navigateEvents.emit(path)
    }
}

@Composable
fun MainBox() {
    CalendardemoTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                val controller = rememberNavController()

                // 这个 saveable 有保存，但是没有让 webview 状态恢复。
                // 因为 WebView restoreState 方法大概率失败。
                val webViewState = rememberSaveableWebViewState()

                LaunchedEffect(controller) {
                    navigateEvents.collect {
                        controller.navigate(it)
                    }
                }

                NavHost(
                    startDestination = "/",
                    navController = controller,
                    modifier = Modifier.weight(1f)
                ) {
                    rootGraph()
                    calendarGraph()
                    webGraph(webViewState)
                }
                BottomBar()
            }
        }
    }
}

@Preview
@Composable
fun MainBoxPreview() {
    MainBox()
}