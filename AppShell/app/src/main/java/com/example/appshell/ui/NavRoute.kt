package com.example.appshell.ui

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.appshell.ui.page.ConfPage
import com.example.appshell.ui.page.HomePage
import com.example.appshell.ui.page.TbsPage

fun NavGraphBuilder.routeRootGraph() {
    composable("home-page") {
        HomePage()
    }
    composable("tbs-page") {
        TbsPage()
    }
    composable("conf-page") {
        ConfPage()
    }
}