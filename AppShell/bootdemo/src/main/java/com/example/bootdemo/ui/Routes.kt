package com.example.bootdemo.ui

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.bootdemo.ui.page.IndexPage
import com.example.bootdemo.ui.page.canback.CanBack2Lv1Page
import com.example.bootdemo.ui.page.canback.CanBack2Lv2N1Page
import com.example.bootdemo.ui.page.canback.CanBack2Lv2N2Page
import com.example.bootdemo.ui.page.canback.CanBack3Lv1Page
import com.example.bootdemo.ui.page.canback.CanBackLv1Page
import com.example.bootdemo.ui.page.canback.CanBackPage
import com.example.bootdemo.ui.page.filesystem.FileSystemPage
import com.example.bootdemo.ui.page.filesystem.PickVisualMediaPage
import com.example.bootdemo.ui.page.lock.LockPage
import com.example.bootdemo.ui.page.web.WebViewPage

val LocalRouter = staticCompositionLocalOf<NavHostController> {
    error("not found router.")
}

fun NavGraphBuilder.rootGraph() {
    composable("index") {
        IndexPage()
    }
}

fun NavGraphBuilder.canBackGraph() {
    composable("can-back") {
        CanBackPage()
    }
    composable("can-back-lv1") {
        CanBackLv1Page()
    }

    // 带有 navigation 多级
    navigation(
        startDestination = "can-back-2-lv1",
        route = "can-back-2"
    ) {
        composable("can-back-2-lv1") {
            CanBack2Lv1Page()
        }
        composable("can-back-2-lv2-n1") {
            CanBack2Lv2N1Page()
        }
        composable("can-back-2-lv2-n2") {
            CanBack2Lv2N2Page()
        }
    }

    composable("can-back-3-lv1") {
        CanBack3Lv1Page()
    }
}

fun NavGraphBuilder.webGraph() {
    composable("web-view") {
        WebViewPage()
    }
}

fun NavGraphBuilder.filesystemGraph() {
    navigation(
        startDestination = "file-system",
        route = "file"
    ) {
        composable("file-system") {
            FileSystemPage()
        }
        composable("pick-visual-media") {
            PickVisualMediaPage()
        }
    }
}

fun NavGraphBuilder.lockGraph() {
    composable("lock") {
        LockPage()
    }
}