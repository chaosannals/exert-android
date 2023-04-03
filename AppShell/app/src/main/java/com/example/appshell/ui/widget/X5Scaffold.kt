package com.example.appshell.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.window.Popup
import androidx.compose.ui.zIndex
import com.example.appshell.ui.sdp
import kotlin.math.roundToInt

@Composable
fun X5Scaffold(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    var isShowDebugger by remember {
        mutableStateOf(false)
    }

    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    Box(
        modifier = modifier
            .fillMaxSize(),
    ) {
        content()
        Box(
            modifier = Modifier
                .zIndex(10f)
                .align(Alignment.BottomEnd)
                .padding(bottom = 100.sdp, end = 10.sdp),
        ) {
            FloatingBall(
                modifier = Modifier
                    .size(44.sdp)
                    .offset {
                        IntOffset(
                            offsetX.roundToInt(),
                            offsetY.roundToInt(),
                        )
                    }
                    // 这种只能单轴
//                    .draggable(
//                        orientation = Orientation.Vertical,
//                        state = rememberDraggableState() {
//                            offsetY += it
//                        }
//                    )
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            offsetX += dragAmount.x
                            offsetY += dragAmount.y
                        }
                    }
                ,
                onClick = {
                    isShowDebugger = true
                },
            )
        }

//        if (isShowDebugger) {
        //   Popup 不接收输入事件
//            Popup(
//                alignment = Alignment.BottomCenter,
//                onDismissRequest = { isShowDebugger = false }
//            ) {
//                DebugView()
//            }
//        }
        if (isShowDebugger) {
            Box(
                modifier = Modifier
                    .zIndex(100f)
                    .fillMaxSize()
                    .background(Color(0x1A000000))
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                    ) {
                        isShowDebugger = false
                    },
            ) {
                DebugView(
                    modifier = Modifier
                        .zIndex(100f)
                        .align(Alignment.BottomCenter)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                    ) {},
                )
            }
        }
    }
}

@Preview
@Composable
fun X5ScaffoldPreview() {
    DesignPreview {
        X5Scaffold {

        }
    }
}