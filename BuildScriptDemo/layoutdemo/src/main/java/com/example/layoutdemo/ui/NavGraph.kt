package com.example.layoutdemo.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.layoutdemo.ui.page.DrawerButtonPage
import com.example.layoutdemo.ui.page.IndexPage

val LocalNavController = staticCompositionLocalOf<NavHostController?> {
    null
}

fun NavGraphBuilder.rootGraph() {
    composable("home") {
        IndexPage()
    }
    composable("drawer-button") {
        DrawerButtonPage()
    }
}