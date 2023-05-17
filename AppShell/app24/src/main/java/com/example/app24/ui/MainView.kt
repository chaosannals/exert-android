package com.example.app24.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.*
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.zIndex
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.app24.ScaffoldKit
import com.example.app24.ui.widget.BottomBar
import com.example.app24.ui.widget.X5WebView

@Composable
fun MainView() {
    val navController = rememberNavController()
    val isShowWebView by ScaffoldKit.isShowWebView.subscribeAsState(initial = false)
    val isShowBottomBar by ScaffoldKit.isShowBottomBar.subscribeAsState(initial = true)

    val navZIndex by remember(isShowWebView) {
        derivedStateOf {
            if (isShowWebView) 1f else 10f
        }
    }

    CompositionLocalProvider(
        LocalNavController provides navController
    ) {
        Column (
            verticalArrangement=Arrangement.Top,
            modifier = Modifier
                .navigationBarsPadding()
                .fillMaxSize()
        ){
            Box(
                modifier = Modifier
                    .weight(1f)
            ) {
                // 路由框
                NavHost(
                    navController,
                    modifier = Modifier
                        .zIndex(navZIndex)
                        .fillMaxSize()
                        .background(Color.White),
                    startDestination = "boot-page",
                )
                {
                    rootGraph()
                    demoGraph()
                }

                // 浏览器
                X5WebView(
                    modifier = Modifier
                        .zIndex(4f)
                        .fillMaxSize(),
                )
            }

            // 底部栏
            if (isShowBottomBar) {
                BottomBar(
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }
    }
}

@Preview
@Composable
fun MainViewPreview() {
    DesignPreview {
        MainView()
    }
}