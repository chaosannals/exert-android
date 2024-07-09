package com.example.calendardemo.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

import com.example.calendardemo.ui.theme.CalendardemoTheme

@Composable
fun MainBox() {
    CalendardemoTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                val controller = rememberNavController()
                NavHost(
                    startDestination = "/",
                    navController = controller,
                ) {
                    rootGraph()
                }
            }
        }
    }
}

@Preview
@Composable
fun MainBoxPreview() {
    MainBox()
}