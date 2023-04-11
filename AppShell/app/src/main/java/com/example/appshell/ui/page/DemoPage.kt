package com.example.appshell.ui.page

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.appshell.ui.LocalNavController
import com.example.appshell.ui.ssp
import com.example.appshell.ui.widget.DesignPreview

@Composable
fun DemoRouteButton(
    text: String,
    path: String,
) {
    val navController = LocalNavController.current

    Text(
        text = text,
        fontSize = 14.ssp,
        modifier = Modifier
            .clickable {
                navController.navigate(path)
            }
    )
}

@Composable
fun DemoPage() {
    Column(
        modifier = Modifier
            .statusBarsPadding(),
    ) {
        DemoRouteButton("Lazy Drag", "lazy-drag-page")
        DemoRouteButton("Lazy Nested Pre Column", "lazy-nested-pre-column-page")
        DemoRouteButton("Lazy Nested Column", "lazy-nested-column-page")
        DemoRouteButton("Lazy", "lazy-page")
        DemoRouteButton("Nested Post Scroll Lazy Column", "nested-post-scroll-lazy-column-page")
        DemoRouteButton("Nested Pre Scroll Lazy Column", "nested-pre-scroll-lazy-column-page")
        DemoRouteButton("Pull Refresh", "pull-refresh-page")
        DemoRouteButton("Swipe Refresh", "swipe-refresh-page")
    }
}

@Preview
@Composable
fun DemoPagePreview() {
    DesignPreview {
        DemoPage()
    }
}