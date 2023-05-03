package com.example.appshell.ui.page.demo

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.appshell.ui.widget.DesignPreview
import com.example.appshell.ui.widget.VideoBox

// TODO 暂停有问题。
@Composable
fun VideoDisplayPage() {
    var url: Uri? by remember {
        mutableStateOf(null)
    }

    val fileLoader = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
    ){
        url = it
    }

    Column(
        modifier = Modifier
            .statusBarsPadding()
    ) {
        Button(
            onClick =
            {
                fileLoader.launch("video/mp4")
            },
        ) {
            Text("选择")
        }
        VideoBox(
            videoUrl = url,
        )
    }
}

@Preview
@Composable
fun VideoDisplayPagePreview() {
    DesignPreview {
        VideoDisplayPage()
    }
}