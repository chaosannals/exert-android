package com.example.appimop.ui

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavArgument
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.appimop.ui.page.LocationPage
import com.example.appimop.ui.page.ReentryPage
import com.example.appimop.ui.page.Web1Page
import com.example.appimop.ui.page.Web2Page
import com.example.appimop.ui.page.Web3Page

val LocalNavController = staticCompositionLocalOf<NavHostController> {
    error("nav controller null!!")
}

fun NavGraphBuilder.rootGraph() {
    composable("reentry") {
        ReentryPage()
    }
}

fun NavGraphBuilder.webGraph() {
    composable("web-1") {
        Web1Page()
    }
    composable("web-2") {
        Web2Page()
    }
    composable(
        route = "web-3?url={url}",
        arguments = listOf(
            navArgument("url") {
                type = NavType.StringType
                nullable = true
            },
        ),
    ) {
        Web3Page()
    }

    composable("location") {
        LocationPage()
    }
}