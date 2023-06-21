package com.example.appimop.ui.page

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.ui.tooling.preview.Preview
import com.example.appimop.LoadUrlEvent
import com.example.appimop.X5WebMultiViewKit
import com.example.appimop.ui.DesignPreview
import com.example.appimop.ui.widget.X5WebMultiViewBox
import io.reactivex.rxjava3.subjects.BehaviorSubject

private val currentUrl: BehaviorSubject<String> = BehaviorSubject.create()
private val key = "web-3"

@Composable
fun Web3Page() {
    val url by currentUrl.subscribeAsState(initial = "")

    LaunchedEffect(url) {
        if (url.isEmpty()) {
            val target = "https://developer.mozilla.org/zh-CN/"
            X5WebMultiViewKit.onLoadUrlPublisher.onNext(
                LoadUrlEvent(key, target)
            )
            currentUrl.onNext(target)
        }
    }

    X5WebMultiViewBox(
        key = key,
    )
}

@Preview
@Composable
fun Web3PagePreview() {
    DesignPreview {
        Web3Page()
    }
}