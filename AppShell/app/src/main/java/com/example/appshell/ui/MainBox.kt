package com.example.appshell.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.appshell.LocalNavController
import com.example.appshell.ui.theme.AppShellTheme
import com.example.appshell.ui.widget.DesignPreview
import com.example.appshell.ui.widget.X5Scaffold

@Composable
fun MainBox() {
    AppShellTheme {
        val navController = rememberNavController()
        CompositionLocalProvider(
            LocalNavController provides navController,
        ) {
            X5Scaffold() {
                NavHost(
                    navController = navController,
                    startDestination = "home-page",
                ) {
                    routeRootGraph()
                }
            }
        }
    }
}

@Preview()
@Composable
fun MainBoxPreview() {
    DesignPreview {

    }
}