package com.example.jcmdemo.ui.page.carousel

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.jcmdemo.ui.DesignPreview
import com.example.jcmdemo.ui.sdp
import com.example.jcmdemo.ui.widget.Carousel
import com.example.jcmdemo.ui.widget.CarouselItem

@Composable
fun CarouselPage() {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Carousel(
            modifier = Modifier
                .height(280.sdp),
            items = listOf(
                CarouselItem(Color.Red),
                CarouselItem(Color.Blue),
                CarouselItem(Color.Yellow),
                CarouselItem(Color.Cyan),
            ),
        )
    }
}

@Preview
@Composable
fun CarouselPagePreview() {
    DesignPreview {
        CarouselPage()
    }
}