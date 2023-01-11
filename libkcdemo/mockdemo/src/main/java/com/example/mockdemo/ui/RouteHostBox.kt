package com.example.mockdemo.ui

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
import com.example.mockdemo.ui.layout.MainScaffold
import com.example.mockdemo.ui.page.GistPage
import com.example.mockdemo.ui.page.HomePage
import com.example.mockdemo.ui.page.MinePage
import com.example.mockdemo.ui.page.network.KtorClientPage
import com.example.mockdemo.ui.theme.LibkcdemoTheme

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
    navigation("ktor-client", "network") {
        composable("ktor-client") {
            KtorClientPage()
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