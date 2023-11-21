package com.example.jcm3ui.ui

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.jcm3ui.ui.page.HomePage
import com.example.jcm3ui.ui.page.demo.FilePickPage
import com.example.jcm3ui.ui.page.demo.FileViewPage

fun NavGraphBuilder.buildRootGraph() {
    composable("home") {
        HomePage()
    }
}

fun NavGraphBuilder.buildDemoGraph() {
    navigation("demo/file-pick", "demo") {
        composable("demo/file-pick") {
            FilePickPage()
        }
        composable("demo/file-view") {
            FileViewPage()
        }
    }
}