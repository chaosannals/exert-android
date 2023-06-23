package com.example.appimop.ui.page

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.appimop.LoadUrlEvent
import com.example.appimop.X5WebMultiViewKit
import com.example.appimop.ui.DesignPreview
import com.example.appimop.ui.LocalNavController
import com.example.appimop.ui.widget.X5WebMultiViewBox
import io.reactivex.rxjava3.subjects.BehaviorSubject

private val currentUrl: BehaviorSubject<String> = BehaviorSubject.create()
private val key = "web-3"

@Composable
fun Web3Page() {
    val navController = LocalNavController.current
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val argUrl by remember(navBackStackEntry) {
        derivedStateOf {
            navBackStackEntry?.arguments?.getString("url")?.let {
                Uri.decode(it)
            }
        }
    }

    val url by currentUrl.subscribeAsState(initial = "")

    LaunchedEffect(url, argUrl) {
        val au = navBackStackEntry?.arguments?.getString("url")
        Log.d("web-3", "au: $au target url: ${argUrl}")

        if (url.isEmpty()) {
            val target = "https://developer.mozilla.org/zh-CN/"
            X5WebMultiViewKit.onLoadUrlPublisher.onNext(
                LoadUrlEvent(key, target)
            )
            currentUrl.onNext(target)
        }
        if (argUrl != null && url != argUrl) {
            X5WebMultiViewKit.onLoadUrlPublisher.onNext(
                LoadUrlEvent(key, argUrl!!)
            )
            currentUrl.onNext(argUrl!!)
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