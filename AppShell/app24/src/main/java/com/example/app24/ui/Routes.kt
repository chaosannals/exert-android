package com.example.app24.ui

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.app24.ui.page.BootPage
import com.example.app24.ui.page.DemoPage
import com.example.app24.ui.page.HomePage
import com.example.app24.ui.page.demo.ThreeTenABPPage
import com.example.app24.ui.page.demo.WebFirstPage
import com.example.app24.ui.page.demo.WebSecondPage

val LocalNavController = staticCompositionLocalOf<NavHostController> {
    error("nav controller null!!")
}

fun NavGraphBuilder.rootGraph() {
    composable("boot-page") {
        BootPage()
    }
    composable("demo-page") {
        DemoPage()
    }
    composable("home-page") {
        HomePage()
    }
}

fun NavGraphBuilder.demoGraph() {
    navigation("three-ten-abp-page", "demo") {
        composable("three-ten-abp-page") {
            ThreeTenABPPage()
        }
        composable("web-first-page") {
            WebFirstPage()
        }
        composable("web-second-page") {
            WebSecondPage()
        }
    }
}