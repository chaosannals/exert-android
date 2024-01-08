package com.example.jcm3wv.ui.page

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.jcm3wv.ui.NavTarget
import com.example.jcm3wv.ui.nav
import com.example.jcm3wv.ui.tryTo

@Preview
@Composable
fun HomePage() {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Button(
            onClick = {
                NavTarget.DemoWebView.nav()
            }
        ) {
            Text("WebView")
        }
    }
}
