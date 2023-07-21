package com.example.app24.ui.page

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.app24.ui.DesignPreview
import com.example.app24.ui.LocalNavController
import com.example.app24.ui.ssp

@Composable
fun DemoLinkButton(
    text: String,
    route: String,
) {
    val navController = LocalNavController.current

    Text(
        text=text,
        color= Color.Black,
        fontSize= 14.ssp,
        modifier = Modifier
            .clickable
            {
                navController.navigate(route)
            },
    )
}

@Composable
fun DemoPage() {
    LazyColumn(
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxSize()
    ) {
        item {
            DemoLinkButton("jsr-310-date", "three-ten-abp-page")
        }
        item {
            DemoLinkButton("web-1", "web-first-page")
        }
        item {
            DemoLinkButton("web-2", "web-second-page")
        }
        item {
            DemoLinkButton("web-new-1", "web-new-first-page")
        }
        item {
            DemoLinkButton("web-new-2", "web-new-second-page")
        }
        item {
            DemoLinkButton("web-jssdk", "web-jssdk-page")
        }
        item {
            DemoLinkButton("Random Graph", "random-graph-page")
        }
        item {
            DemoLinkButton("Random Graph 2", "random-graph-2-page")
        }
        item {
            DemoLinkButton("Line Graph", "line-graph-page")
        }
        item {
            DemoLinkButton("Dialog", "dialog-page")
        }
        item {
            DemoLinkButton("Dialog 2", "dialog-2-page")
        }
        item {
            DemoLinkButton("Dialog 3", "dialog-3-page")
        }
        item {
            DemoLinkButton("Dialog 4", "dialog-4-page")
        }
        item {
            DemoLinkButton("Custom Shape", "custom-shape-page")
        }
        item {
            DemoLinkButton("Back Handle", "back-handle-page")
        }
        item {
            DemoLinkButton("Json", "json-page")
        }
        item {
            DemoLinkButton("Disposable", "disposable-page")
        }
        item {
            DemoLinkButton("CaptureViewImage", "capture-view-image-page")
        }
        item {
            DemoLinkButton("CaptureViewImage2", "capture-view-image-2-page")
        }
    }
}

@Preview
@Composable
fun DemoPagePreview() {
    DesignPreview {
        DemoPage()
    }
}