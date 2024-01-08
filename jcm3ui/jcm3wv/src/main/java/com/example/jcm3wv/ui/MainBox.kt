package com.example.jcm3wv.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.jcm3wv.ui.theme.Jcm3uiTheme

@Preview
@Composable
fun MainBox() {
    val navController = rememberNavEventController()

    Jcm3uiTheme {
        NavHost(
            navController=navController,
            startDestination = "home"
        ) {
            buildRootGraph()
            buildDemoGraph()
        }
    }
}