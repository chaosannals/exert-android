package com.example.libkcdemo.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cn.chaosannals.dirtool.LocalNavController
import com.example.libkcdemo.ui.layout.MainScaffold
import com.example.libkcdemo.ui.page.HomePage
import com.example.libkcdemo.ui.theme.LibkcdemoTheme

@Composable
fun RouteHostBox() {
    val navController = rememberNavController()
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

fun NavGraphBuilder.routeRootGraph() {
    composable("home") {
        HomePage()
    }
}

@Preview
@Composable
fun RouteHostBoxPreview() {
    DesignPreview {
        RouteHostBox()
    }
}