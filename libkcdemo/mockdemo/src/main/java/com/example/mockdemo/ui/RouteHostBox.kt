package com.example.mockdemo.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController

val LocalNavController = staticCompositionLocalOf<NavController> {
    error("no nav controller")
}

@Composable
fun RouteHostBox() {

}

@Preview
@Composable
fun RouteHostBoxPreview() {

}