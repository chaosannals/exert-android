package com.example.jcm3wv.ui

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.jcm3wv.ui.page.HomePage

fun NavGraphBuilder.buildRootGraph() {
    composable("home") {
        HomePage()
    }
}