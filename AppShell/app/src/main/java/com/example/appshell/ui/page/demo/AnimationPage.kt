package com.example.appshell.ui.page.demo

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.appshell.ui.sdp
import com.example.appshell.ui.widget.DesignPreview

@Composable
fun AnimationPage() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        item {
            var visible by remember { mutableStateOf(false) }
            Button(onClick = { visible = !visible }) {
                Text("切换1")
            }
            AnimatedVisibility(visible = visible) {
                Box(
                    modifier = Modifier
                        .size(100.sdp)
                        .background(Color.Red)
                )
            }
        }

        item {
            var visible by remember { mutableStateOf(false) }
            Button(onClick = { visible = !visible }) {
                Text("切换2")
            }
            AnimatedVisibility(
                visible = visible,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it })
            ) {
                Box(
                    modifier = Modifier
                        .size(100.sdp)
                        .background(Color.Red)
                )
            }
        }

        item {
            var visible by remember { mutableStateOf(false) }
            Button(onClick = { visible = !visible }) {
                Text("切换3")
            }
            AnimatedVisibility(
                visible = visible,
//                enter = fadeIn(),
                enter = EnterTransition.None,
                exit = fadeOut(),
            ) {
                Box(
                    modifier = Modifier
                        .size(100.sdp, 200.sdp)
                        .background(Color.White),
                ) {
                    AnimatedVisibility(
                        visible = visible,
                        enter = slideInVertically(initialOffsetY = { it }),
                        exit = slideOutVertically(targetOffsetY = { it }),
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(100.sdp)
                                .background(Color.Red)
                        )
                    }
                }
            }
        }

        item {
            var visible by remember { mutableStateOf(false) }
            Button(onClick = { visible = !visible }) {
                Text("切换4")
            }

            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it })
            ) {
                Box(
                    modifier = Modifier
                        .size(100.sdp)
                        .background(Color.Red)
                )
            }
        }
    }
}

@Preview
@Composable
fun AnimationPagePreview() {
    DesignPreview {
        AnimationPage()
    }
}