package com.example.hlitdemo.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost

@Composable
fun MainScaffold() {
    val navController = LocalNavController.current
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        NavHost(navController, startDestination = "home") {
            rootGraph()
        }
    }
}

@Preview
@Composable
fun MainScaffoldPreview() {
    MainScaffold()
}