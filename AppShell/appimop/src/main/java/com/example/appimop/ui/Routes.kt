package com.example.appimop.ui

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.appimop.ui.page.Web1Page
import com.example.appimop.ui.page.Web2Page
import com.example.appimop.ui.page.Web3Page

val LocalNavController = staticCompositionLocalOf<NavHostController> {
    error("nav controller null!!")
}

fun NavGraphBuilder.rootGraph() {

}

fun NavGraphBuilder.webGraph() {
    composable("web-1") {
        Web1Page()
    }
    composable("web-2") {
        Web2Page()
    }
    composable("web-3") {
        Web3Page()
    }
}