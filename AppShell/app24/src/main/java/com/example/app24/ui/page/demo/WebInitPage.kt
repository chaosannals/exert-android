package com.example.app24.ui.page.demo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.app24.ui.DesignPreview
import com.example.app24.ui.widget.X5WebView2
import com.example.app24.ui.widget.rememberX5WebView2Controller

@Composable
fun WebInitPage() {
    val controller = rememberX5WebView2Controller()

    val url = "file:///android_asset/webviewinit/index.html"

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxSize()
    ) {
        X5WebView2(
            controller = controller,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
        Row (
            modifier = Modifier
                .fillMaxWidth(),
        ){
            Button(
                onClick =
                {
                    controller.loadUrl(url)
                },
            ) {
                Text("init")
            }
        }
    }
}

@Preview
@Composable
fun WebInitPagePreview() {
    DesignPreview {
        WebInitPage()
    }
}