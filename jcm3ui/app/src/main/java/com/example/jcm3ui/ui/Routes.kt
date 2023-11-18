package com.example.jcm3ui.ui

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.jcm3ui.ui.page.HomePage

fun NavGraphBuilder.buildRootGraph() {
    composable("home") {
        HomePage()
    }
}