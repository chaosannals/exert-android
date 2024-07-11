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
                    webGraph()
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