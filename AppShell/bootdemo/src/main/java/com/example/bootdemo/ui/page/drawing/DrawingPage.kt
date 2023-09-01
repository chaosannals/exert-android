package com.example.bootdemo.ui.page.drawing

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.bootdemo.ui.LocalRouter

@Composable
fun DrawingButton(
    text: String,
    route: String,
) {
    val mode = LocalInspectionMode.current
    val router = if (mode) null else LocalRouter.current

    Button(
        onClick = { router?.navigate(route) },
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
        )
    }
}

@Composable
fun DrawingPage() {
    Column(
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxSize()
    ) {
//        DrawingButton("GLSL SKSL", "glsl-sksl")
        DrawingButton("Wheel", "wheel")
        DrawingButton("Coil Gif Svg", "coil-gif")
    }
}

@Preview
@Composable
fun DrawingPagePreview() {
    DrawingPage()
}