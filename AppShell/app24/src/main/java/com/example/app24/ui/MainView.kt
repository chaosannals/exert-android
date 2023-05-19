package com.example.app24.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.*
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.zIndex
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.app24.ScaffoldKit
import com.example.app24.ui.widget.BottomBar
import com.example.app24.ui.widget.FloatBall
import com.example.app24.ui.widget.X5WebView
import kotlin.math.roundToInt

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

    var floatX by remember {
        mutableStateOf(Design.displayWidth - 40f.sf)
    }
    var floatY by remember {
        mutableStateOf(Design.displayHeight - 140f.sf)
    }

    CompositionLocalProvider(
        LocalNavController provides navController
    ) {
        Box(
            modifier= Modifier
                .navigationBarsPadding()
                .fillMaxSize()
        ) {
            Column(
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .fillMaxSize()
            ) {
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

            // 悬浮球
            FloatBall(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .offset {
                        IntOffset(
                            floatX.roundToInt(),
                            floatY.roundToInt(),
                        )
                    }.pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            floatX = (floatX + dragAmount.x).coerceIn(0f, Design.displayWidth - 40f.sf)
                            floatY = (floatY + dragAmount.y).coerceIn(0f, Design.displayHeight - 40f.sf)
                        }
                    },
            )
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