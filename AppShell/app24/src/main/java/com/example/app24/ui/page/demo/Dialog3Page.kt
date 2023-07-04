package com.example.app24.ui.page.demo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import com.example.app24.ui.DesignPreview
import com.example.app24.ui.sdp

/// 弹出窗的前后只和弹出显示的先后有关，和弹出代码放置顺序无关。
@Composable
fun Dialog3Page() {
    var isShowLv1 by remember { mutableStateOf(false) }
    var isShowLv2 by remember { mutableStateOf(false) }
    var isShowLv3 by remember { mutableStateOf(false) }

    Button(onClick = { isShowLv1 = true }) {
        Text("Show Lv1")
    }


    if (isShowLv3) {
        Dialog(onDismissRequest = { isShowLv3 = false }) {
            Box(
                modifier = Modifier
                    .size(240.sdp)
                    .background(Color.White, RoundedCornerShape(4.sdp))
            ) {
                Button(onClick = { }) {
                    Text("This is Lv3")
                }
            }
        }
    }

    if (isShowLv1) {
        Dialog(onDismissRequest = { isShowLv1 = false }) {
            Box(
                modifier = Modifier
                    .size(280.sdp)
                    .background(Color.White, RoundedCornerShape(4.sdp))
            ) {
                Button(onClick = { isShowLv2 = true }) {
                    Text("Show Lv2")
                }
            }
        }
    }

    if (isShowLv2) {
        Dialog(onDismissRequest = { isShowLv2 = false }) {
            Box(
                modifier = Modifier
                    .size(260.sdp)
                    .background(Color.White, RoundedCornerShape(4.sdp))
            ) {
                Button(onClick = { isShowLv3 = true }) {
                    Text("Show Lv3")
                }
            }
        }
    }
}

@Preview
@Composable
fun Dialog3PagePreview() {
    DesignPreview {
        Dialog3Page()
    }
}