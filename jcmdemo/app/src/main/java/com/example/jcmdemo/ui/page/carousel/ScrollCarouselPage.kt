package com.example.jcmdemo.ui.page.carousel

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.jcmdemo.ui.DesignPreview
import com.example.jcmdemo.ui.sdp
import com.example.jcmdemo.ui.ssp
import com.example.jcmdemo.ui.widget.ScrollCarousel
import com.example.jcmdemo.ui.widget.ScrollDragItem

@Composable
fun ScrollCarouselPage() {
    ScrollCarousel(
        height = 400.sdp,
    ) {
        for (i in 0..8) {
            val color = when(i % 3) {
                0 -> Color.Red
                1 -> Color.Green
                2 -> Color.Blue
                else -> Color.White
            }
            ScrollDragItem(
                color = color,
                title = i.toString(),
                titleColor = Color.White,
                titleSize = 44.ssp,
            )
        }
    }
}

@Preview
@Composable
fun ScrollCarouselPagePreview() {
    DesignPreview() {
        ScrollCarouselPage()
    }
}