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
    composable("can-back-2-lv1") {
        CanBack2Lv1Page()

        navigation(
            startDestination = "can-back-2-lv2-n1",
            route = "can-back-2-lv2-n",) {
            composable("can-back-2-lv2-n1") {
                CanBack2Lv2N1Page()
            }
            composable("can-back-2-lv2-n2") {
                CanBack2Lv2N2Page()
            }
        }
    }

    composable("can-back-3-lv1") {
        CanBack3Lv1Page()
    }
}