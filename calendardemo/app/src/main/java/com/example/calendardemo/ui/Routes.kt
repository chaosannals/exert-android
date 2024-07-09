package com.example.calendardemo.ui

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.calendardemo.ui.page.HomePage

fun NavGraphBuilder.rootGraph() {
    composable("/") {
        HomePage()
    }
}