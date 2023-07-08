package com.example.app24.ui.page.demo

import androidx.compose.animation.core.Animatable
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import com.example.app24.ui.DesignPreview

// TODO 备一些常用代码
@Composable
fun Animatable1Page() {
    var dbAnimatable = remember {
        Animatable(0f)
    }
}

@Preview
@Composable
fun Animatable1PagePreview() {
    DesignPreview {
        Animatable1Page()
    }
}