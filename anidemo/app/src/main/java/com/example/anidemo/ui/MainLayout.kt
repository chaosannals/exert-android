package com.example.anidemo.ui

import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@ExperimentalMaterial3Api
@Composable
fun MainLayout() {
    val scroller = rememberScrollState()

    Scaffold(
        topBar = {},
        bottomBar = {},
        floatingActionButton={
            FloatingActionButton(
                onClick = { },
            ) {

            }
        },
    ) {
        NavGraphRoutes(scroller = scroller, paddingValues = it)
    }
}

@ExperimentalMaterial3Api
@Preview()
@Composable
fun MainLayoutPreview() {
    MainLayout()
}