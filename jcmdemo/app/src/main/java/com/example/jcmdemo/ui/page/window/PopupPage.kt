package com.example.jcmdemo.ui.page.window

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Popup
import com.example.jcmdemo.ui.DesignPreview
import com.example.jcmdemo.ui.sdp

@Composable
fun PopupPage() {
    Box(
        contentAlignment=Alignment.Center,
        modifier = Modifier.fillMaxSize(),
    ) {
        var isShowBottom by remember {
            mutableStateOf(false)
        }
        var isShowTop by remember {
            mutableStateOf(false)
        }

        Column(
            verticalArrangement=Arrangement.Center,
            horizontalAlignment=Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column (
                verticalArrangement=Arrangement.Center,
                horizontalAlignment=Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ){
                Button(
                    onClick = { isShowTop=true },
                ) {
                    Text("Top")
                }
                Button(
                    onClick = { isShowBottom=true },
                ) {
                    Text("Bottom")
                }

                if (isShowTop) {
                    // Popup 总是锚定在父级最外框，
                    // navigationBarsPadding padding 等都是内框。
                    // Compose 没有 margin 外框这种东西。
                    // Popup fillMaxSize 总是屏幕宽高
                    Popup(
                        alignment = Alignment.TopStart,
                        onDismissRequest= { isShowTop=false },
                    ) {
                        Box(
                            modifier = Modifier
                                .size(140.sdp, 40.sdp)
                                .background(Color.Green),
                        ) {
                            Text(text = "Top")
                        }
                    }
                }

                if (isShowBottom) {
                    // Popup 总是锚定在父级最外框，
                    // navigationBarsPadding padding 等都是内框距离。
                    // Compose 没有 margin 外框距离这种东西。
                    Popup(
                        alignment = Alignment.BottomCenter,
                        onDismissRequest= { isShowBottom=false },
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(400.sdp)
                                .background(Color.Cyan),
                        ) {
                            Text(text = "Bottom")
                        }
                    }
                }
            }
            Box(
                modifier = Modifier
                    .navigationBarsPadding()
                    .fillMaxWidth()
                    .height(40.sdp)
                    .background(Color.Red),
            ) {
                Text(text = "navbar")
            }
        }
    }
}

@Preview
@Composable
fun PopupPagePreview() {
    DesignPreview() {
        PopupPage()
    }
}