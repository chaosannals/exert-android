package com.example.jcmdemo.ui.page.carousel

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.jcmdemo.ui.DesignPreview
import com.example.jcmdemo.ui.sdp
import com.example.jcmdemo.ui.widget.Carousel2

@Composable
fun Carousel2Page() {
    Carousel2(
        modifier=Modifier
            .size(234.sdp, 123.sdp),
    ) {
        Box(
            modifier= Modifier
//                    .fillMaxSize()
                .size(234.sdp, 124.sdp)
                .background(Color.Red)
        )
        Box(
            modifier= Modifier
//                    .fillMaxSize()
                .size(234.sdp, 124.sdp)
                .background(Color.Green)
        )
        Box(
            modifier= Modifier
//                    .fillMaxSize()
                .size(234.sdp, 124.sdp)
                .background(Color.Blue)
        )
        Box(
            modifier= Modifier
//                    .fillMaxSize()
                .size(234.sdp, 124.sdp)
                .background(Color.Yellow)
        )
    }
}

@Preview
@Composable
fun Carousel2PagePreview() {
    DesignPreview() {
        Carousel2Page()
    }
}