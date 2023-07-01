package com.example.app24.ui.page.demo

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.SecureFlagPolicy
import com.example.app24.ui.DesignPreview
import com.example.app24.ui.sdp

// Dialog 无法撑起屏幕宽度，和高度，宽度被限制。
// 使用 实验性 usePlatformDefaultWidth 属性可以撑到宽度，高度仍然不行（状态栏和导航栏）。不过这勉强满足一般需求。
// 使用 usePlatformDefaultWidth  预览是有问题的，应该是预览功能没有支持，毕竟这个属性是后加的。
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DialogPage() {
    var isShow by remember { mutableStateOf(true) }
    
    Column(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding()
//            .fillMaxSize(0.5f)
            .fillMaxSize()
    ) {
        Button(onClick = { isShow=true }) {
            Text(text = "显示")
        }

        if (isShow) {
            Dialog(
                onDismissRequest = { isShow = false },
                properties = DialogProperties(
                    dismissOnBackPress=true,
                    dismissOnClickOutside=false,
                    securePolicy= SecureFlagPolicy.Inherit,
                    usePlatformDefaultWidth = false,
                )
            ) {
                Column (
                    verticalArrangement=Arrangement.Bottom,
                    horizontalAlignment=Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                        .border(1.sdp, Color.Red)
                ) {
                    Column (
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(400.sdp)
                            .background(Color.White)
                    ) {
                        
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun DialogPagePreview() {
//    DesignPreview {
        DialogPage()
//    }
}