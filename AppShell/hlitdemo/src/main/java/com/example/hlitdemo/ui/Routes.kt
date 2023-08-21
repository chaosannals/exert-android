package com.example.hlitdemo.ui

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.hlitdemo.ui.page.AboutPage
import com.example.hlitdemo.ui.page.Coroutine2Page
import com.example.hlitdemo.ui.page.Coroutine3Page
import com.example.hlitdemo.ui.page.Coroutine4Page
import com.example.hlitdemo.ui.page.CoroutinePage
import com.example.hlitdemo.ui.page.DataStoreProtoPage
import com.example.hlitdemo.ui.page.HomePage
import com.example.hlitdemo.ui.page.XWebViewProxyPage

val LocalNavController = staticCompositionLocalOf<NavHostController> {
    error ("Not Nav Controller")
}

fun NavGraphBuilder.rootGraph() {
    composable("home") {
        HomePage()
    }
    composable("about") {
        AboutPage()
    }
    composable("coroutine") {
        CoroutinePage()
    }
    composable("coroutine-2") {
        Coroutine2Page()
    }
    composable("coroutine-3") {
        Coroutine3Page()
    }
    composable("coroutine-4") {
        Coroutine4Page()
    }
    composable("x-web-view-proxy") {
        XWebViewProxyPage()
    }
    composable("data-store-proto") {
        DataStoreProtoPage()
    }
}