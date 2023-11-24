package com.example.jcm3ui.ui.page.demo

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties
import com.example.jcm3ui.ui.displayDp
import com.example.jcm3ui.ui.displayHdp
import com.example.jcm3ui.ui.sdp

@Composable
fun PopupMakeDialogPage() {
    var isShow by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Button(
            onClick = {
                isShow = true
            }
        ) {
            Text(text = "显示")
        }
    }

    if (isShow) {
        Popup(
            onDismissRequest = { isShow = false },
            popupPositionProvider = object : PopupPositionProvider {
                override fun calculatePosition(
                    anchorBounds: IntRect,
                    windowSize: IntSize,
                    layoutDirection: LayoutDirection,
                    popupContentSize: IntSize
                ): IntOffset {
                    return IntOffset.Zero
                }
            },
            properties = PopupProperties(
                focusable = true, // 允许获得焦点
                clippingEnabled=false, //  关闭裁剪（允许超出屏幕，只有宽度可以）
                excludeFromSystemGesture=false, // 排除系统手势
            ),
        ) {
            Column(
                verticalArrangement= Arrangement.Center,
                horizontalAlignment= Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    // 最大不可超过客户区大小。(顶部栏和底部栏不可占)
//                    .size(displayDp, displayHdp + 60.sdp)
                    .border(1.sdp, Color.Red, RectangleShape)
            ) {
                Button(
                    onClick={
                        isShow = false
                    }
                ) {
                    Text(text = "隐藏")
                }
            }
        }
    }
}