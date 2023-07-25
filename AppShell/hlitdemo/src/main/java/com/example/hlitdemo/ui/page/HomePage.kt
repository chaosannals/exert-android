package com.example.hlitdemo.ui.page

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import com.example.hlitdemo.MainViewModel
import com.example.hlitdemo.hiltActivityViewModel
import kotlinx.coroutines.launch

@Composable
fun HomePage(
    mainViewModel: MainViewModel=hiltActivityViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val statusBarVisible by mainViewModel.collectStatusBarVisible()

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
}

@Preview
@Composable
fun HomePagePreview() {
    HomePage()
}