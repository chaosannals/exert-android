package com.example.anidemo.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.anidemo.LocalMainScroller
import com.example.anidemo.LocalNavController

enum class MainBottomItem(
    val path: String,
    val title: String,
    val icon: ImageVector,
) {
    Home("home", "首页", Icons.Default.Home),
    Gist("gist", "概览", Icons.Default.Menu),
    Info("info", "信息", Icons.Default.Info),
    Employee("employee", "员工", Icons.Default.Person),
}

@Composable
fun MainBottomBar() {
    val nc = LocalNavController.current
    val navBackStackEntry by nc.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    BottomAppBar (
        modifier = Modifier
            .navigationBarsPadding()
            .fillMaxWidth(),
        containerColor = Color.White,
    ){
        MainBottomItem.values().forEach {
            IconButton(onClick = {
                nc.navigate(it.path)
            }) {
                Icon(
                    imageVector = it.icon,
                    contentDescription = it.title,
                    tint = if (currentRoute == it.path) Color.Cyan else Color.Black,
                )
            }
        }
    }
}

@Preview
@Composable
fun MainBottomBarPreview() {
    CompositionLocalProvider(
        LocalNavController provides rememberNavController(),
        LocalMainScroller provides rememberScrollState(),
    ) {
        MainBottomBar()
    }
}