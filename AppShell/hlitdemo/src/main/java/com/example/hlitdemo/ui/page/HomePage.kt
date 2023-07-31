package com.example.hlitdemo.ui.page

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import com.example.hlitdemo.MainViewModel
import com.example.hlitdemo.hiltActivityViewModel
import com.example.hlitdemo.ui.LocalNavController
import kotlinx.coroutines.launch

@Composable
fun HomePage(
    mainViewModel: MainViewModel=hiltActivityViewModel()
) {
    val inspectionMode = LocalInspectionMode.current
    val navController = if (inspectionMode) null else LocalNavController.current
    val coroutineScope = rememberCoroutineScope()
    val statusBarVisible by mainViewModel.collectStatusBarVisible()

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxSize(),
    ) {
        Button(
            onClick =
            {
                coroutineScope.launch {
                    mainViewModel.emitStatusBarVisible(!statusBarVisible)
                }
            }
        ) {
            Text("状态栏显示：$statusBarVisible")
        }
        Button(
            onClick =
            {
                navController?.navigate("about")
            },
        ) {
            Text("About")
        }

        Button(
            onClick =
            {
                navController?.navigate("coroutine")
            },
        ) {
            Text("Coroutine")
        }
        Button(
            onClick =
            {
                navController?.navigate("coroutine-2")
            },
        ) {
            Text("Coroutine 2")
        }
    }
}

@Preview
@Composable
fun HomePagePreview() {
    HomePage()
}