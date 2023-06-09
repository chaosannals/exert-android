package com.example.app24.ui

import android.util.Log
import androidx.compose.animation.core.Animatable
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
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.zIndex
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.app24.ScaffoldKit
import com.example.app24.ui.widget.BottomBar
import com.example.app24.ui.widget.FloatBall
import com.example.app24.ui.widget.X5WebView
import kotlinx.coroutines.launch
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

    // 无法作为 key 的 问题，导致此处只是个保留对比值
    var floatSize by remember {
        Log.d("FloatBall", "floatSize init")
        mutableStateOf(IntSize.Zero)
    }
    var floatRight by remember {
        mutableStateOf(0f)
    }
    var floatBottom by remember {
        mutableStateOf(0f)
    }

    // 日志上看，数值有变动，但是加入 floatSize key 后 floatX 数值改变了不起效 offset
    //var floatX by remember(floatSize) {
//    var floatX by remember {
//        Log.d("FloatBall", "floatX init")
////        mutableStateOf((Design.displayWidth - floatSize.width).toFloat())
//        mutableStateOf(0f)
//    }
    val floatX = remember {
        Animatable(Design.displayWidth.toFloat())
    }
//    var floatY by remember {
//        Log.d("FloatBall", "floatY init")
//        //mutableStateOf(Design.displayHeight - floatSize.height - 40f.sf)
//        mutableStateOf(0f)
//    }
    val floatY = remember {
        Animatable(Design.displayHeight.toFloat())
    }

    // TODO 动画用到，找个好看的写法
    val scope = rememberCoroutineScope()

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
                            floatX.value.roundToInt(),
                            floatY.value.roundToInt(),
                        )
                    }
                    .pointerInput(Unit) {
                        if (floatSize != size) {
                            Log.d("FloatBall", "$floatSize $size")
                            floatSize = size
                            floatX.snapTo((Design.displayWidth - floatSize.width).toFloat())
                            floatY.snapTo(Design.displayHeight - floatSize.height - 40f.sf)
                            floatRight = (Design.displayWidth - size.width).toFloat()
                            floatBottom = Design.displayHeight - size.height - 40f.sf
                        }

                        detectDragGestures(
                            onDragEnd =
                            {
                                scope.launch {
                                    Log.d("FloatBall", "FloatBall End ${floatX.value} $floatRight $size")
                                    if (floatX.value > (floatRight / 2)) {
                                        floatX.animateTo(floatRight)
                                    } else {
                                        floatX.animateTo(0f)
                                    }
                                }
                            },
                            onDrag = { change, dragAmount ->
                                Log.d("FloatBall", "$floatX $floatY")
                                change.consume()
                                scope.launch { // TODO 找一个好看的写法
                                    floatX.snapTo((floatX.value + dragAmount.x).coerceIn(0f, floatRight))
                                    floatY.snapTo((floatY.value + dragAmount.y).coerceIn(0f, floatBottom))
                                }
                            },
                        )
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