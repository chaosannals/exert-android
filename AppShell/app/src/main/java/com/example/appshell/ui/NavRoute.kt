package com.example.appshell.ui

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.appshell.ui.page.ConfPage
import com.example.appshell.ui.page.HomePage

fun NavGraphBuilder.routeRootGraph() {
    composable("home-page") {
        HomePage()
    }
    composable("conf-page") {
        ConfPage()
    }
}