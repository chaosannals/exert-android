package com.example.appshell.ui.page

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.appshell.LocalTotalStatus
import com.example.appshell.ui.ssp
import com.example.appshell.ui.widget.DesignPreview

@Composable
fun DemoRouteButton(
    text: String,
    path: String,
) {
    val totalStatus = LocalTotalStatus.current

    Text(
        text = text,
        fontSize = 14.ssp,
        modifier = Modifier
            .clickable {
                totalStatus.router.navigate(path)
            }
    )
}

@Composable
fun DemoPage() {
    Column(
        modifier = Modifier
            .statusBarsPadding(),
    ) {
        DemoRouteButton("Animation", "animation-page")
        DemoRouteButton("Coil Photo", "coil-photo-page")
        DemoRouteButton("Http Client", "http-client-page")
        DemoRouteButton("Lazy Drag", "lazy-drag-page")
        DemoRouteButton("Lazy Nested Pre Column", "lazy-nested-pre-column-page")
        DemoRouteButton("Lazy Nested Column", "lazy-nested-column-page")
        DemoRouteButton("Lazy", "lazy-page")
        DemoRouteButton("Loading Pane", "loading-pane-page")
        DemoRouteButton("Nested Post Scroll Lazy Column", "nested-post-scroll-lazy-column-page")
        DemoRouteButton("Nested Pre Scroll Lazy Column", "nested-pre-scroll-lazy-column-page")
        DemoRouteButton("Pull Refresh", "pull-refresh-page")
        DemoRouteButton("Swipe Refresh", "swipe-refresh-page")
        DemoRouteButton("Tip Message", "tip-message-page")
        DemoRouteButton("TxIM", "tx-im-login-page")
        DemoRouteButton("Pdf Viewer", "pdf-view-page")
        DemoRouteButton("Pdf 2 Viewer", "pdf-view-2-page")
        DemoRouteButton("Pdf 3 Viewer", "pdf-view-3-page")
        DemoRouteButton("Reflection", "reflection-page")
        DemoRouteButton("File", "file-page")
        DemoRouteButton("File Custom Pick Page", "file-custom-pick-page")
        DemoRouteButton("Image Pick Page", "image-pick-page")
        DemoRouteButton("Video Pick Page", "video-pick-page")
        DemoRouteButton("Video Display", "video-display-page")
        DemoRouteButton("Video Display 2", "video-display-2-page")
        DemoRouteButton("Null", "null-page")
        DemoRouteButton("Url QueryString Make Page", "url-query-string-make-page")
    }
}

@Preview
@Composable
fun DemoPagePreview() {
    DesignPreview {
        DemoPage()
    }
}