package com.example.layoutdemo.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.layoutdemo.ui.theme.BuildScriptDemoTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainLayout() {
    val navController = rememberNavController()

    CompositionLocalProvider(
        LocalOverscrollConfiguration provides null,
        LocalNavController provides navController,
    ) {
        BuildScriptDemoTheme {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                NavHost(
                    navController = navController,
                    startDestination = "home"
                ) {
                    rootGraph()
                }
            }
        }
    }
}

@Preview
@Composable
fun MainLayoutPreview() {

}