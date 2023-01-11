package com.example.libkcdemo.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.libkcdemo.ui.layout.MainScaffold
import com.example.libkcdemo.ui.page.GistPage
import com.example.libkcdemo.ui.page.HomePage
import com.example.libkcdemo.ui.page.MinePage
import com.example.libkcdemo.ui.page.network.KtorServerPage
import com.example.libkcdemo.ui.theme.LibkcdemoTheme

val LocalNavController = staticCompositionLocalOf<NavController> {
    error("no nav controller")
}

@Composable
fun RouteHostBox() {
    val navController = rememberNavController()
    CompositionLocalProvider(
        LocalNavController provides navController,
    ) {
        LibkcdemoTheme {
            MainScaffold() {
                NavHost(
                    navController = navController,
                    startDestination = "home",
                    modifier = Modifier.fillMaxSize()
                ) {
                    routeRootGraph()
                }
            }
        }
    }
}

fun NavGraphBuilder.routeRootGraph() {
    composable("home") {
        HomePage()
    }
    composable("gist") {
        GistPage()
    }
    composable("mine") {
        MinePage()
    }
    routeNetworkGraph()
}

fun NavGraphBuilder.routeNetworkGraph() {
    navigation("ktor-server", route="network") {
        composable("ktor-server") {
            KtorServerPage()
        }
    }
}

@Preview
@Composable
fun RouteHostBoxPreview() {
    DesignPreview {
        RouteHostBox()
    }
}