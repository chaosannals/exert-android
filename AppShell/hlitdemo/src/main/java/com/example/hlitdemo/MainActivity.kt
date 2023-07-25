package com.example.hlitdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.*
import androidx.navigation.compose.rememberNavController
import com.example.hlitdemo.ui.LocalNavController
import com.example.hlitdemo.ui.MainScaffold
import com.example.hlitdemo.ui.theme.AppShellTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint

// Hilt 仅支持扩展 ComponentActivity 的 activity，如 AppCompatActivity。
// Hilt 仅支持扩展 androidx.Fragment 的 Fragment。
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppShellTheme {
                // 谷歌扩展库
                val systemUiController = rememberSystemUiController()
                val statusBarVisible by mainViewModel.collectStatusBarVisible()
                LaunchedEffect(statusBarVisible){
                    systemUiController.isSystemBarsVisible = statusBarVisible
                }

                val navController = rememberNavController()
                CompositionLocalProvider(
                    LocalNavController provides navController,
                ) {
                    MainScaffold()
                }
            }
        }
    }
}
