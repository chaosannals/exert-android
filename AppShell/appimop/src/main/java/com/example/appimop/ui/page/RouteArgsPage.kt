package com.example.appimop.ui.page

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.appimop.ui.DesignPreview
import com.example.appimop.ui.LocalNavController

@Composable
fun RouteArgsPage() {
    val navController = LocalNavController.current
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val s by remember(navBackStackEntry) {
        derivedStateOf {
            navBackStackEntry?.arguments?.getString("s")
        }
    }

    val b by remember(navBackStackEntry) {
        derivedStateOf {
            navBackStackEntry?.arguments?.getBoolean("b")
        }
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
    ){
        Text(
            text = "字符参数 s = $s",
        )
        Text(
            text = "布尔参数 b = $b",
        )
    }
}

@Preview
@Composable
fun RouteArgsPagePreview() {
    DesignPreview() {
        RouteArgsPage()
    }
}