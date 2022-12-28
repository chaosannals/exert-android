package com.example.jcmdemo.ui.page.carousel

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.jcmdemo.ui.DesignPreview
import com.example.jcmdemo.ui.sdp
import com.example.jcmdemo.ui.widget.PictureViewer

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PictureViewerPage() {
    var isVisible by remember {
        mutableStateOf(true)
    }

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        Button(onClick = { isVisible = true }) {
            Text("show")
        }
        if (isVisible) {
            Dialog(
                onDismissRequest = {
                    isVisible = false
                },
                properties = DialogProperties(
                    dismissOnBackPress=true,
                    dismissOnClickOutside=true,
                    usePlatformDefaultWidth = false, // 去掉两边 Padding, 目前是实验性 API。
                )
            ) {
                PictureViewer(
                    url = "",
                    onClicked={isVisible = false},
                    modifier = Modifier
//                        .size(300.sdp, 300.sdp),
                )
            }
        }
    }
}

@Preview
@Composable
fun PictureViewerPagePreview() {
    DesignPreview() {
        PictureViewerPage()
    }
}