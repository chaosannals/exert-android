package com.example.hlitdemo.ui

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.hlitdemo.ui.page.AboutPage
import com.example.hlitdemo.ui.page.HomePage

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
}